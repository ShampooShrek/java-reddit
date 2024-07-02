package io.github.shampooshrek.reddit.models.dto;

/**
 * UserByTokenDto
 */
public class UserByTokenDto {
  private String token;

  public UserByTokenDto() {
  }

  public UserByTokenDto(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}
