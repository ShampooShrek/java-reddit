package io.github.shampooshrek.reddit.models.dto;

import io.github.shampooshrek.reddit.models.UserImage;
import io.github.shampooshrek.reddit.models.User;
import io.github.shampooshrek.reddit.models.Post;
import io.github.shampooshrek.reddit.models.Comment;
import io.github.shampooshrek.reddit.models.Group;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserDto {
  private Long id;
  private String name;
  private String nickname;
  private String email;

  private UserImage image;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<PostDto> posts;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<CommentDto> comments;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<GroupDto> groups;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<GroupDto> adminGroups;

  public static UserDto FromEntity(User user) {
    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setName(user.getName());
    userDto.setEmail(user.getEmail());
    userDto.setNickname(user.getNickname());
    userDto.setImage(user.getUserImage());
    return userDto;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<PostDto> getPosts() {
    return posts;
  }

  public UserDto setPosts(List<Post> posts) {
    List<PostDto> postsDto = new ArrayList<>();
    posts.forEach(p -> postsDto.add(PostDto.FromEntity(p)));
    this.posts = postsDto;
    return this;
  }

  public UserDto setPostsDto(List<PostDto> posts) {
    this.posts = posts;
    return this;
  }

  public List<CommentDto> getComments() {
    return comments;
  }

  public UserDto setCommentsDto(List<CommentDto> comments) {
    this.comments = comments;
    return this;
  }

  public UserDto setComments(List<Comment> comments) {
    List<CommentDto> commentsDto = new ArrayList<>();
    comments.forEach(c -> commentsDto.add(CommentDto.FromEntity(c)));
    this.comments = commentsDto;
    return this;
  }

  public UserImage getImage() {
    return image;
  }

  public UserDto setImage(UserImage image) {
    this.image = image;
    return this;
  }

  public List<GroupDto> getGroups() {
    return groups;
  }

  public UserDto setGroups(List<Group> groups) {
    List<GroupDto> groupsDto = groups.stream()
        .map(g -> GroupDto.FromEntity(g)).toList();
    this.groups = groupsDto;
    return this;
  }

  public UserDto setGroupsDto(List<GroupDto> groups) {
    this.groups = groups;
    return this;
  }

  public List<GroupDto> getAdminGroups() {
    return adminGroups;
  }

  public UserDto setAdminGroups(List<Group> adminGroups) {
    List<GroupDto> groupsDto = adminGroups.stream()
        .map(g -> GroupDto.FromEntity(g)).toList();
    this.adminGroups = groupsDto;
    return this;
  }

  public UserDto setAdminGroupsDto(List<GroupDto> adminGroups) {
    this.adminGroups = adminGroups;
    return this;
  }

}
