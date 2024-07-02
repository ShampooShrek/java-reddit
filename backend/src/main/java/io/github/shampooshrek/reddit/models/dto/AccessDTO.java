package io.github.shampooshrek.reddit.models.dto;

public class AccessDTO {
  private String token;
  private String refreshToken;

  // TODO implementar o usuario e liberaçoes

  public AccessDTO(String token) {
    this.token = token;
  }

  public AccessDTO(String token, String refreshToken) {
    this.token = token;
    this.refreshToken = refreshToken;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

}
