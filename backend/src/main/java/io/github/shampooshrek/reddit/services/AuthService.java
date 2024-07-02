package io.github.shampooshrek.reddit.services;

import io.github.shampooshrek.reddit.models.Group;
import io.github.shampooshrek.reddit.models.RefreshToken;
import io.github.shampooshrek.reddit.models.User;
import io.github.shampooshrek.reddit.models.UserDetailsImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import io.github.shampooshrek.reddit.config.Jwt.JwtUtils;
import io.github.shampooshrek.reddit.exceptions.InvalidValueException;
import io.github.shampooshrek.reddit.models.dto.AccessDTO;
import io.github.shampooshrek.reddit.models.dto.AuthGroupsResponseDto;
import io.github.shampooshrek.reddit.models.dto.AuthPostsResponseDto;
import io.github.shampooshrek.reddit.models.dto.AuthenticationDto;
import io.github.shampooshrek.reddit.models.dto.CommentDto;
import io.github.shampooshrek.reddit.models.dto.GroupDto;
import io.github.shampooshrek.reddit.models.dto.PostDto;
import io.github.shampooshrek.reddit.repositories.UserRepository;

@Service
public class AuthService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private RefreshTokenService refreshTokenService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtils jwtUtils;

  public AccessDTO login(AuthenticationDto authDto) throws InvalidValueException {
    try {
      UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(authDto.getEmail(),
          authDto.getPassword());

      Authentication authentication = authenticationManager.authenticate(userAuth);

      UserDetailsImpl userAuthenticate = (UserDetailsImpl) authentication.getPrincipal();
      System.out.println("UserDetais: " + userAuthenticate.getEmail());

      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userAuthenticate.getId());
      String token = generateToken(userAuthenticate);

      AccessDTO accessDTO = new AccessDTO(token, refreshToken.getToken());
      return accessDTO;

    } catch (BadCredentialsException e) {
      throw new InvalidValueException("E-mail e/ou Senha incorretos!");
    }
  };

  public User getAuth() throws InvalidValueException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
    User user = userRepository.findById(userDetails.getId())
        .orElseThrow(() -> new InvalidValueException("Usuário não encontrado"));

    return user;
  }

  public String generateToken(UserDetails user) {
    String token = jwtUtils.generateTokenFromUserDetailsImpl(user);
    return token;
  }

  public AuthPostsResponseDto getAuthPosts() throws InvalidValueException {
    User user = getAuth();
    List<PostDto> posts = user.getPosts().stream().map(p -> PostDto.FromEntity(p).setGroup(p.getGroup())).toList();
    return new AuthPostsResponseDto(posts);
  }

  public AuthGroupsResponseDto getAuthGroups() throws InvalidValueException {
    User user = getAuth();

    List<Group> groups = user.getGroups();
    List<GroupDto> userGroups = new ArrayList<>();
    List<GroupDto> adminGroups = new ArrayList<>();

    groups.forEach(g -> {
      boolean isGroupAdmin = g.getAdmins().contains(user);
      if (isGroupAdmin)
        adminGroups.add(GroupDto.FromEntity(g));
      else
        userGroups.add(GroupDto.FromEntity(g));
    });

    return new AuthGroupsResponseDto(userGroups, adminGroups);
  }
}
