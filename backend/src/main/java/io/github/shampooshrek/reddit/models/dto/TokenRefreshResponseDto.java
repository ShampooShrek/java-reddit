package io.github.shampooshrek.reddit.models.dto;

import io.github.shampooshrek.reddit.models.dto.TokenRefreshResponseDto;

public class TokenRefreshResponseDto {

  private String token;
  private String refreshToken;

  public TokenRefreshResponseDto(String accessToken, String refreshToken) {
    this.token = accessToken;
    this.refreshToken = refreshToken;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String accessToken) {
    this.token = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
