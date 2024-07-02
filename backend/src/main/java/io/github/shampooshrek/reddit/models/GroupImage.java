package io.github.shampooshrek.reddit.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "group_image")
public class GroupImage extends GroupImageTemplate {

  public GroupImage() {
  }

  public GroupImage(String name, String originalName, long fileSize, String fileUrl) {
    super(name, originalName, fileSize, fileUrl);
  }

  public GroupImage(String name, String originalName, long fileSize, String fileUrl, Group group) {
    super(name, originalName, fileSize, fileUrl, group);
  }

}
