package io.github.shampooshrek.reddit.models.dto;

/**
 * LoginResponse
 */
public class LoginResponse {
  public String cu;

  public LoginResponse(String cu) {
    this.cu = cu;
  }

  public String getCu() {
    return cu;
  }

  public void setCu(String cu) {
    this.cu = cu;
  }
}
