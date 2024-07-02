package io.github.shampooshrek.reddit.models.dto;

import io.github.shampooshrek.reddit.models.User;
import io.github.shampooshrek.reddit.models.Post;
import io.github.shampooshrek.reddit.models.Comment;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CommentDto {
  private long id;
  private String content;
  private Date createdAt;

  private CommentDto parentComment;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private PostDto post;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<CommentDto> comments;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private UserDto user;

  public CommentDto() {
  };

  public static CommentDto FromEntity(Comment comment) {
    CommentDto commentDto = new CommentDto();
    commentDto.setId(comment.getId());
    commentDto.setContent(comment.getContent());
    commentDto.setCreatedAt(comment.getCreatedAt());
    return commentDto;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public PostDto getPost() {
    return post;
  }

  public CommentDto setPost(Post post) {
    this.post = PostDto.FromEntity(post);
    return this;
  }

  public CommentDto setPostUser(User user) {
    this.post.setUser(user);
    return this;
  }

  public CommentDto getParentComment() {
    return parentComment;
  }

  public CommentDto setParentComment(CommentDto parentComment) {
    if (parentComment != null) {
      this.parentComment = parentComment;
    }
    return this;
  }

  public List<CommentDto> getComments() {
    return comments;
  }

  public CommentDto setComments(List<Comment> comments) {
    List<CommentDto> commentsDto = comments.stream().map(e -> CommentDto.FromEntity(e)).toList();
    this.comments = commentsDto;
    return this;
  }

  public CommentDto setCommentsDto(List<CommentDto> comments) {
    this.comments = comments;
    return this;
  }

  public UserDto getUser() {
    return user;
  }

  public CommentDto setUser(User user) {
    this.user = UserDto.FromEntity(user);
    return this;
  }

}
