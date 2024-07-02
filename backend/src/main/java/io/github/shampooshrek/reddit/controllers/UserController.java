package io.github.shampooshrek.reddit.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.github.shampooshrek.reddit.enums.ResponseType;
import io.github.shampooshrek.reddit.exceptions.InvalidValueException;
import io.github.shampooshrek.reddit.models.ApiResponse;
import io.github.shampooshrek.reddit.models.User;
import io.github.shampooshrek.reddit.models.UserImage;
import io.github.shampooshrek.reddit.models.dto.RegisterDto;
import io.github.shampooshrek.reddit.models.dto.UserDto;
import io.github.shampooshrek.reddit.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService service;

  @GetMapping
  public ResponseEntity<ApiResponse> getUser() {
    List<UserDto> users = service.getUsers();
    ApiResponse response = new ApiResponse(users, ResponseType.SUCCESS);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getUserById(@PathVariable long id) {
    try {
      UserDto user = service.getUserById(id);
      ApiResponse response = new ApiResponse(user, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return ApiResponse.internalErrorResponse();
    }
  }

  @PostMapping
  public ResponseEntity<ApiResponse> createUser(@RequestBody RegisterDto user) {
    try {
      service.createUser(user);
      ApiResponse response = new ApiResponse(user, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse> updateUser(@RequestBody User user,
      @PathVariable long id) {
    try {
      UserDto userDto = service.updateUser(user, id);
      ApiResponse response = new ApiResponse(userDto, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @PostMapping("/image")
  public ResponseEntity<ApiResponse> uploadImage(@RequestParam("file") MultipartFile file) {
    try {
      UserImage filename = service.createUserImage(file);
      ApiResponse response = new ApiResponse(filename, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (InvalidValueException | RuntimeException | IOException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

}
