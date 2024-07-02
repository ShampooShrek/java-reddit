package io.github.shampooshrek.reddit.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.shampooshrek.reddit.exceptions.InvalidValueException;
import io.github.shampooshrek.reddit.exceptions.UnauthorizedEventException;
import io.github.shampooshrek.reddit.models.Comment;
import io.github.shampooshrek.reddit.models.dto.CommentDto;
import io.github.shampooshrek.reddit.models.Post;
import io.github.shampooshrek.reddit.models.User;
import io.github.shampooshrek.reddit.repositories.CommentRepository;
import io.github.shampooshrek.reddit.repositories.PostRepository;

@Service
public class CommentService {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private AuthService authService;

  public List<CommentDto> getComments() {
    List<CommentDto> comments = new ArrayList<>();
    commentRepository.findAll().forEach(c -> {
      CommentDto commentDto = CommentDto
          .FromEntity(c)
          .setUser(c.getUser())
          .setPost(c.getPost());

      if (c.getParentComment() != null) {
        commentDto.setParentComment(CommentDto.FromEntity(c.getParentComment()));
      }

      comments.add(commentDto);
    });
    return comments;
  }

  public List<CommentDto> getCommentsTree(long postId) {
    return commentRepository.findCommentsWithoutParent(postId)
        .stream()
        .filter(c -> !c.isDeleted())
        .map(c -> {
          CommentDto comment = CommentDto.FromEntity(c)
              .setUser(c.getUser());
          return withChildrens(comment);
        }).toList();
  }

  public CommentDto createComment(Comment comment) throws InvalidValueException {
    User user = authService.getAuth();

    Post post = postRepository.findById(comment.getPostId())
        .orElseThrow(() -> new InvalidValueException("Post não encontrado!"));

    Comment parentComment = null;

    if (comment.getParentCommentId() > 0) {
      parentComment = commentRepository
          .findById(comment.getParentCommentId())
          .orElseThrow(() -> new InvalidValueException("Comentário pai não encontrado!"));
    }

    if (comment.getContent() == null || comment.getContent().equals("")) {
      throw new InvalidValueException("Comentário inválido!");
    }

    comment.setPost(post);
    comment.setUser(user);
    comment.setParentComment(parentComment);

    return CommentDto.FromEntity(commentRepository.save(comment));
  }

  public CommentDto updateComment(Comment commentRequest) throws InvalidValueException, UnauthorizedEventException {
    User user = authService.getAuth();
    Comment comment = commentRepository.findById(commentRequest.getId())
        .orElseThrow(() -> new InvalidValueException("Comentário não encontrado!"));

    if (user.getId() != comment.getUser().getId())
      throw new UnauthorizedEventException("Usuário não autorizado!");

    comment.setContent(commentRequest.getContent());

    return CommentDto.FromEntity(commentRepository.save(comment));
  }

  public void deleteComment(Comment commentRequest) throws InvalidValueException, UnauthorizedEventException {
    User user = authService.getAuth();

    Comment comment = commentRepository.findById(commentRequest.getId())
        .orElseThrow(() -> new InvalidValueException("Comentário não encontrado!"));

    if (user.getId() != comment.getUser().getId())
      throw new UnauthorizedEventException("Usuário não autorizado!");

    comment.setDeleted(true);

    commentRepository.save(comment);
  }

  private CommentDto withChildrens(CommentDto parent) {
    List<CommentDto> childrens = commentRepository
        .findCommentsPerParent(parent.getId())
        .stream()
        .filter(comment -> !comment.isDeleted())
        .map(c -> {
          CommentDto comment = withChildrens(CommentDto.FromEntity(c));
          comment.setUser(c.getUser());
          return comment;
        })
        .toList();
    return parent.setCommentsDto(childrens);
  }

}
