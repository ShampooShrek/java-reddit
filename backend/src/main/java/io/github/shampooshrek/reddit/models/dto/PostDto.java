package io.github.shampooshrek.reddit.models.dto;

import io.github.shampooshrek.reddit.models.User;
import io.github.shampooshrek.reddit.models.Group;
import io.github.shampooshrek.reddit.models.Post;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class PostDto {
  private long id;
  private String title;
  private String content;
  private String description;
  private Date createdAt;
  private boolean deleted;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<CommentDto> comments;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private GroupDto group;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private UserDto user;

  public static PostDto FromEntity(Post post) {
    PostDto postDto = new PostDto();
    postDto.setId(post.getId());
    postDto.setTitle(post.getTitle());
    postDto.setContent(post.getContent());
    postDto.setDeleted(post.isDeleted());
    postDto.setDescription(post.getDescription());
    postDto.setCreatedAt(post.getCreatedAt());
    return postDto;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public UserDto getUser() {
    return user;
  }

  public PostDto setUser(User user) {
    this.user = UserDto.FromEntity(user);
    return this;
  }

  public List<CommentDto> getComments() {
    return comments;
  }

  public PostDto setComments(List<CommentDto> comments) {
    this.comments = comments;
    return this;
  }

  public GroupDto getGroup() {
    return group;
  }

  public PostDto setGroup(Group group) {
    this.group = GroupDto.FromEntity(group);
    return this;
  }

  public PostDto setGroupDto(GroupDto group) {
    this.group = group;
    return this;
  }
}
