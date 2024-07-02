package io.github.shampooshrek.reddit.exceptions;

public class UnauthorizedEventException extends Exception {

  public UnauthorizedEventException(String message) {
    super(message);
  }

  public UnauthorizedEventException(String message, Throwable e) {
    super(message, e);
  }
}
