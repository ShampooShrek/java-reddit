package io.github.shampooshrek.reddit.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_image")
public class UserImage extends File {

  @OneToOne
  @JoinColumn(name = "user_id", unique = true)
  @JsonIgnore
  private User user;

  public UserImage() {
  }

  public UserImage(String name, String originalName, long fileSize, String fileUrl) {
    super(name, originalName, fileSize, fileUrl);
  }

  public UserImage(String name, String originalName, long fileSize, String fileUrl, User user) {
    super(name, originalName, fileSize, fileUrl);
    this.user = user;
  }

}
