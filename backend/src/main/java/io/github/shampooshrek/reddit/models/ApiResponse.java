package io.github.shampooshrek.reddit.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.github.shampooshrek.reddit.enums.ResponseType;

public class ApiResponse {
  private Object response;
  private String type;

  public ApiResponse(Object response, ResponseType type) {
    this.response = response;
    this.type = type.label;
  }

  public static ResponseEntity<ApiResponse> internalErrorResponse() {
    ApiResponse response = new ApiResponse("Ops, algo deu errado, tente novamente mais tarde!", ResponseType.ERROR);
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public static ResponseEntity<ApiResponse> noAuthorizatonResponse(String message) {
    ApiResponse response = new ApiResponse(message, ResponseType.ERROR);
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  public static ResponseEntity<ApiResponse> invalidValueResponse(String message) {
    ApiResponse response = new ApiResponse(message, ResponseType.ERROR);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  public Object getResponse() {
    return response;
  }

  public void setResponse(Object response) {
    this.response = response;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
