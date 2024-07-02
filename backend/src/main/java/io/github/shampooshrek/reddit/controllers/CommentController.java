package io.github.shampooshrek.reddit.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.shampooshrek.reddit.enums.ResponseType;
import io.github.shampooshrek.reddit.exceptions.InvalidValueException;
import io.github.shampooshrek.reddit.exceptions.UnauthorizedEventException;
import io.github.shampooshrek.reddit.models.ApiResponse;
import io.github.shampooshrek.reddit.models.Comment;
import io.github.shampooshrek.reddit.models.dto.CommentDto;
import io.github.shampooshrek.reddit.services.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

  @Autowired
  private CommentService service;

  @GetMapping
  public ResponseEntity<ApiResponse> getComments() {
    try {
      List<CommentDto> comments = service.getComments();
      ApiResponse response = new ApiResponse(comments, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @PostMapping
  public ResponseEntity<ApiResponse> createComment(@RequestBody Comment comment) {
    try {
      CommentDto createdComment = service.createComment(comment);
      ApiResponse response = new ApiResponse(createdComment, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateComment(@RequestBody Comment comment) {
    try {
      CommentDto updatedComment = service.updateComment(comment);
      ApiResponse response = new ApiResponse(updatedComment, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (UnauthorizedEventException e) {
      return ApiResponse.noAuthorizatonResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @DeleteMapping
  public ResponseEntity<ApiResponse> deleteComment(@RequestBody Comment comment) {
    try {
      service.deleteComment(comment);
      ApiResponse response = new ApiResponse("OK!", ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (UnauthorizedEventException e) {
      return ApiResponse.noAuthorizatonResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }
}
