package io.github.shampooshrek.reddit.models.dto;

import io.github.shampooshrek.reddit.models.User;

public class UserImageDto extends FileDto {
  private User user;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
