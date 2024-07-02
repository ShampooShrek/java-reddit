package io.github.shampooshrek.reddit.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.shampooshrek.reddit.exceptions.InvalidValueException;
import io.github.shampooshrek.reddit.exceptions.UnauthorizedEventException;
import io.github.shampooshrek.reddit.models.Group;
import io.github.shampooshrek.reddit.models.Post;
import io.github.shampooshrek.reddit.models.dto.PostDto;
import io.github.shampooshrek.reddit.models.User;
import io.github.shampooshrek.reddit.repositories.GroupRepository;
import io.github.shampooshrek.reddit.repositories.PostRepository;

@Service
public class PostService {
  @Autowired
  private PostRepository postRepository;

  @Autowired
  private GroupRepository groupRepository;

  @Autowired
  private CommentService commentService;

  @Autowired
  private AuthService authService;

  public List<PostDto> getPosts() {
    Iterable<Post> posts = postRepository.findAll();
    List<PostDto> postsDtos = new ArrayList<>();
    posts.forEach(p -> {
      PostDto pDto = PostDto
          .FromEntity(p)
          .setUser(p.getUser());

      postsDtos.add(pDto);
    });
    return postsDtos;
  }

  public PostDto getPostById(long id) throws InvalidValueException {
    Post post = postRepository.findById(id).orElseThrow(() -> new InvalidValueException("Post não encontrado!"));
    return PostDto
        .FromEntity(post)
        .setUser(post.getUser())
        .setComments(commentService.getCommentsTree(id))
        .setGroup(post.getGroup());
  }

  public List<PostDto> getGroupPosts(long groupId) throws InvalidValueException {
    groupRepository.findById(groupId).orElseThrow(() -> new InvalidValueException("Grupo não encontrado!"));
    List<PostDto> posts = new ArrayList<>();
    postRepository.findGroupPosts(groupId).forEach(p -> posts.add(PostDto.FromEntity(p).setUser(p.getUser())));

    return posts;
  }

  public PostDto createPost(Post post, long groupId) throws InvalidValueException {
    User user = authService.getAuth();

    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new InvalidValueException("Grupo não encontrado!"));

    if (post.getTitle() == null) {
      throw new InvalidValueException("Título inválido!");
    }
    if (post.getContent() == null) {
      throw new InvalidValueException("Conteúdo inválido!");
    }

    post.setUser(user);
    post.setGroup(group);

    return PostDto.FromEntity(postRepository.save(post));
  }

  public PostDto updatePost(Post post, long id) throws InvalidValueException, UnauthorizedEventException {
    User user = authService.getAuth();
    Post postExists = postRepository.findById(id)
        .orElseThrow(() -> new InvalidValueException("Post não encontrado!"));

    if (user.getId() != postExists.getUser().getId())
      throw new UnauthorizedEventException("Usuário sem permissão para alterar este post");

    if (post.getTitle() == null) {
      throw new InvalidValueException("Título inválido!");
    }
    if (post.getContent() == null) {
      throw new InvalidValueException("Conteúdo inválido!");
    }

    postExists.setTitle(post.getTitle());
    postExists.setDescription(post.getDescription());
    postExists.setContent(post.getContent());

    return PostDto.FromEntity(postRepository.save(postExists));
  }

  public void deletePost(long postId) throws InvalidValueException, UnauthorizedEventException {
    User user = authService.getAuth();
    Post postExists = postRepository.findById(postId)
        .orElseThrow(() -> new InvalidValueException("Post não encontrado!"));

    if (user.getId() != postExists.getUser().getId())
      throw new UnauthorizedEventException("Usuário sem permissão para alterar este post");

    postExists.setDeleted(true);
    postRepository.save(postExists);
  }

}
