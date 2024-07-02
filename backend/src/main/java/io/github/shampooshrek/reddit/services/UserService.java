package io.github.shampooshrek.reddit.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.github.shampooshrek.reddit.exceptions.InvalidValueException;
import io.github.shampooshrek.reddit.models.File;
import io.github.shampooshrek.reddit.models.User;
import io.github.shampooshrek.reddit.models.UserImage;
import io.github.shampooshrek.reddit.models.dto.RegisterDto;
import io.github.shampooshrek.reddit.models.dto.UserDto;
import io.github.shampooshrek.reddit.repositories.UserImageRepository;
import io.github.shampooshrek.reddit.repositories.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository repository;

  @Autowired
  private UserImageRepository userImageRepository;

  @Autowired
  private AuthService authService;

  @Autowired
  private ImageService imageService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public List<UserDto> getUsers() {
    List<UserDto> users = new ArrayList<>();
    repository.findAll().forEach(u -> {
      UserDto uDto = UserDto.FromEntity(u)
          .setPosts(u.getPosts());
      users.add(uDto);
    });
    return users;
  }

  public UserDto getUserById(long id) throws InvalidValueException {
    User user = repository.findById(id).orElseThrow(() -> new InvalidValueException("Usuário não encontrado!"));
    return UserDto.FromEntity(user)
        .setPosts(user.getPosts())
        .setGroups(user.getGroups()).setGroups(user.getGroups()).setAdminGroups(user.getAdminGroups());
  }

  public UserDto createUser(RegisterDto register) throws InvalidValueException {
    if (!validValue(register.getName())) {
      throw new InvalidValueException("Nome de usuário inválido");
    }
    if (!validValue(register.getNickname())) {
      throw new InvalidValueException("Nickname Inválido");
    }
    if (!validValue(register.getEmail())) {
      throw new InvalidValueException("E-mail inválido!");
    }
    if (!userNotExists(register)) {
      throw new InvalidValueException("E-mail em uso!");
    }
    if (!validValue(register.getPassword())) {
      throw new InvalidValueException("Senha inválida");
    } else if (!register.getPassword().equals(register.getConfPassword())) {
      throw new InvalidValueException("Senhas não conferem");
    }

    String hashPassword = passwordEncoder.encode(register.getPassword());
    register.setPassword(hashPassword);

    User user = new User(
        register.getName(),
        register.getNickname(),
        register.getEmail(),
        register.getPassword());

    return UserDto.FromEntity(repository.save(user));
  }

  public UserDto updateUser(User user, Long id) throws InvalidValueException {
    Optional<User> userExists = repository.findById(id);
    if (userExists.isEmpty()) {
      throw new InvalidValueException("Usuário não encontrado!");
    }

    if (!validValue(user.getName())) {
      throw new InvalidValueException("Nome de usuário inválido");
    }
    if (!validValue(user.getNickname())) {
      throw new InvalidValueException("Nickname Inválido");
    }
    if (validValue(user.getEmail())) {
      throw new InvalidValueException("E-mail inválido!");
    }
    if (!userNotExists(user)) {
      throw new InvalidValueException("E-mail em uso!");
    }

    User updatedUser = userExists.get();
    updatedUser.setName(user.getName());
    updatedUser.setNickname(user.getNickname());
    updatedUser.setEmail(user.getEmail());

    return UserDto.FromEntity(repository.save(updatedUser));
  }

  public UserImage createUserImage(MultipartFile file) throws InvalidValueException, IOException, RuntimeException {
    User user = authService.getAuth();
    UserImage userImage = user.getUserImage();
    List<String> mimetypes = List.of(
        "image/jpg",
        "image/jpeg",
        "image/png");

    File image = imageService.saveImage(file, mimetypes);
    userImage = new UserImage(image.getName(), image.getOriginalName(), image.getFileSize(),
        image.getFileUrl(), user);

    if (user.getUserImage() != null) {
      userImage.setId(user.getUserImage().getId());
    }

    return userImageRepository.save(userImage);
  }

  private boolean validValue(String value) {
    if (value == null || value.trim().length() < 6) {
      return false;
    }
    return true;
  }

  private boolean userNotExists(User user) {
    Optional<User> userExists = repository.findByEmailOrNickname(user.getEmail());
    return userExists.isEmpty() || userExists.get().getId() == user.getId();
  }

  private boolean userNotExists(RegisterDto register) {
    Optional<User> userExists = repository.findByEmailOrNickname(register.getEmail());
    return userExists.isEmpty();
  }
}
