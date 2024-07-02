package io.github.shampooshrek.reddit.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.shampooshrek.reddit.enums.ResponseType;
import io.github.shampooshrek.reddit.exceptions.InvalidValueException;
import io.github.shampooshrek.reddit.exceptions.UnauthorizedEventException;
import io.github.shampooshrek.reddit.models.ApiResponse;
import io.github.shampooshrek.reddit.models.Post;
import io.github.shampooshrek.reddit.models.dto.PostDto;
import io.github.shampooshrek.reddit.services.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
  @Autowired
  private PostService service;

  @GetMapping
  public ResponseEntity<ApiResponse> getPosts() {
    try {
      List<PostDto> posts = service.getPosts();
      ApiResponse response = new ApiResponse(posts, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getPostById(@PathVariable Long id) {
    try {
      PostDto post = service.getPostById(id);
      ApiResponse response = new ApiResponse(post, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse> updatePost(@RequestBody Post post, @PathVariable long id) {
    try {
      PostDto updatedPost = service.updatePost(post, id);
      ApiResponse response = new ApiResponse(updatedPost, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deletePost(@PathVariable long id) {
    try {
      service.deletePost(id);
      ApiResponse response = new ApiResponse("OK!", ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (UnauthorizedEventException e) {
      return ApiResponse.noAuthorizatonResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @PostMapping("/groups/{groupId}")
  public ResponseEntity<ApiResponse> createPost(@RequestBody Post post, @PathVariable long groupId) {
    try {
      PostDto createdPost = service.createPost(post, groupId);
      ApiResponse response = new ApiResponse(createdPost, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      System.out.println(e);
      return ApiResponse.internalErrorResponse();
    }
  }

  @GetMapping("/groups/{groupId}")
  public ResponseEntity<ApiResponse> getGroupPosts(@PathVariable long groupId) {
    try {
      List<PostDto> posts = service.getGroupPosts(groupId);
      ApiResponse response = new ApiResponse(posts, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }
}
