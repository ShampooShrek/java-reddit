package io.github.shampooshrek.reddit.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;

@MappedSuperclass
public class GroupImageTemplate extends File {

  @OneToOne
  @JoinColumn(name = "group_id")
  @JsonIgnore
  private Group group;

  public GroupImageTemplate() {
  }

  public GroupImageTemplate(String name, String originalName, long fileSize, String fileUrl) {
    super(name, originalName, fileSize, fileUrl);
  }

  public GroupImageTemplate(String name, String originalName, long fileSize, String fileUrl, Group group) {
    super(name, originalName, fileSize, fileUrl);
    this.group = group;
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

}
