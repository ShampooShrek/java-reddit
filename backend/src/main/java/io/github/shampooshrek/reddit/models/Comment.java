package io.github.shampooshrek.reddit.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "comments")
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String content;

  @Column(name = "created_at")
  private Date createdAt = new Date();

  private boolean deleted = false;

  @ManyToOne
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  @OneToMany(mappedBy = "parentComment")
  private List<Comment> comments = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "parent_comment")
  private Comment parentComment;

  @ManyToOne
  @JoinColumn(name = "post_id", nullable = false)
  @JsonBackReference
  private Post post;

  @Transient
  private long userId;

  @Transient
  private long postId;

  @Transient
  private long parentCommentId;

  public Comment() {
  }

  public Comment(String content, Date createdAt, List<Comment> comments, Comment parentComment) {
    this.content = content;
    this.createdAt = createdAt;
    this.comments = comments;
    this.parentComment = parentComment;
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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  public Comment getParentComment() {
    return parentComment;
  }

  public void setParentComment(Comment parentComment) {
    this.parentComment = parentComment;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public long getPostId() {
    return postId;
  }

  public void setPostId(long postId) {
    this.postId = postId;
  }

  public long getParentCommentId() {
    return parentCommentId;
  }

  public void setParentCommentId(long parentCommentId) {
    this.parentCommentId = parentCommentId;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

}
