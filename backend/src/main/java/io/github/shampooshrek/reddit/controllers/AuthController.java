package io.github.shampooshrek.reddit.controllers;

import io.github.shampooshrek.reddit.enums.ResponseType;
import io.github.shampooshrek.reddit.exceptions.InvalidValueException;
import io.github.shampooshrek.reddit.models.ApiResponse;
import io.github.shampooshrek.reddit.models.RefreshToken;
import io.github.shampooshrek.reddit.models.dto.AccessDTO;
import io.github.shampooshrek.reddit.models.dto.AuthGroupsResponseDto;
import io.github.shampooshrek.reddit.models.dto.AuthPostsResponseDto;
import io.github.shampooshrek.reddit.models.dto.AuthenticationDto;
import io.github.shampooshrek.reddit.models.dto.RegisterDto;
import io.github.shampooshrek.reddit.models.dto.TokenRefreshRequestDto;
import io.github.shampooshrek.reddit.models.dto.TokenRefreshResponseDto;
import io.github.shampooshrek.reddit.models.dto.UserDto;
import io.github.shampooshrek.reddit.services.AuthService;
import io.github.shampooshrek.reddit.services.RefreshTokenService;
import io.github.shampooshrek.reddit.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

  @Autowired
  private AuthService authService;

  @Autowired
  private UserService userService;

  @Autowired
  private RefreshTokenService refreshTokenService;

  @PostMapping("/signIn")
  public ResponseEntity<ApiResponse> login(@RequestBody AuthenticationDto authDto) {
    try {
      System.out.println(authDto.getPassword() + authDto.getEmail());
      AccessDTO token = authService.login(authDto);
      ApiResponse response = new ApiResponse(token, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @PostMapping("/signUp")
  public ResponseEntity<ApiResponse> createUser(@RequestBody RegisterDto user) {
    try {
      UserDto userResponse = userService.createUser(user);
      ApiResponse response = new ApiResponse(userResponse, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @PostMapping("/refresh_token")
  public ResponseEntity<ApiResponse> refreshToken(@RequestBody TokenRefreshRequestDto request) {
    try {
      String requestRefreshToken = request.getRefreshToken();
      TokenRefreshResponseDto refreshToken = refreshTokenService.findByToken(requestRefreshToken)
          .map(refreshTokenService::verifyExpiration)
          .map(RefreshToken::getUser)
          .map(user -> {
            String token = authService.generateToken(user);
            return new TokenRefreshResponseDto(token, requestRefreshToken);
          }).orElseThrow(() -> new InvalidValueException("Token inválido"));

      ApiResponse response = new ApiResponse(refreshToken, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @PostMapping("/get_auth_by_token")
  public ResponseEntity<ApiResponse> refreshToken() {
    try {
      UserDto user = UserDto.FromEntity(authService.getAuth());
      ApiResponse response = new ApiResponse(user, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @GetMapping("/get_auth_posts")
  public ResponseEntity<ApiResponse> getAuthPosts() {
    try {
      AuthPostsResponseDto userPosts = authService.getAuthPosts();
      ApiResponse response = new ApiResponse(userPosts, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @GetMapping("/get_auth_groups")
  public ResponseEntity<ApiResponse> getAuthGroups() {
    try {
      AuthGroupsResponseDto userGroups = authService.getAuthGroups();
      ApiResponse response = new ApiResponse(userGroups, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }
}
