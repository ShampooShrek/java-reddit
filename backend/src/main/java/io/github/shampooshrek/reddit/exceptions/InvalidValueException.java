package io.github.shampooshrek.reddit.exceptions;

public class InvalidValueException extends Exception {

  public InvalidValueException(String message) {
    super(message);
  }

  public InvalidValueException(String message, Throwable e) {
    super(message, e);
  }
}
