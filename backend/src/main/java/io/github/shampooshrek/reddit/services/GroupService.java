package io.github.shampooshrek.reddit.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.github.shampooshrek.reddit.exceptions.InvalidValueException;
import io.github.shampooshrek.reddit.exceptions.UnauthorizedEventException;
import io.github.shampooshrek.reddit.models.File;
import io.github.shampooshrek.reddit.models.Group;
import io.github.shampooshrek.reddit.models.GroupBanner;
import io.github.shampooshrek.reddit.models.GroupImage;
import io.github.shampooshrek.reddit.models.Post;
import io.github.shampooshrek.reddit.models.User;
import io.github.shampooshrek.reddit.models.dto.GroupDto;
import io.github.shampooshrek.reddit.repositories.UserRepository;
import io.github.shampooshrek.reddit.repositories.GroupBannerRepository;
import io.github.shampooshrek.reddit.repositories.GroupImageRepository;
import io.github.shampooshrek.reddit.repositories.GroupRepository;
import io.github.shampooshrek.reddit.repositories.PostRepository;

@Service
public class GroupService {
  @Autowired
  private GroupRepository groupRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private GroupImageRepository groupImageRepository;

  @Autowired
  private GroupBannerRepository groupBannerRepository;

  @Autowired
  private ImageService imageService;

  @Autowired
  private AuthService authService;

  public GroupDto getById(long id) throws InvalidValueException {
    Group group = groupRepository.findById(id)
        .orElseThrow(() -> new InvalidValueException("Grupo não encontrado!"));
    for (User u : group.getAdmins()) {
      System.out.println(u.getName());
    }
    GroupDto groupDto = GroupDto.FromEntity(group)
        .setUsers(group.getUsers())
        .setAdmins(group.getAdmins())
        .setPosts(group.getActivedPosts());
    return groupDto;
  }

  public List<GroupDto> getGroups() throws InvalidValueException {
    List<GroupDto> groups = new ArrayList<>();
    groupRepository.findAll()
        .forEach(g -> groups.add(GroupDto.FromEntity(g)
            .setAdmins(g.getAdmins())
            .setUsers(g.getUsers())));
    return groups;
  }

  public GroupDto createGroup(Group group) throws InvalidValueException {
    User user = authService.getAuth();

    if (group.getGroupName().trim().equals("")) {
      throw new InvalidValueException("Nome do Grupo Inválido!");
    }

    group.setUserCreator(user);

    group.getAdmins().add(user);
    group.getUsers().add(user);

    group = groupRepository.save(group);

    user.getGroups().add(group);
    user.getAdminGroups().add(group);

    userRepository.save(user);

    return GroupDto.FromEntity(group)
        .setUsers(group.getUsers())
        .setAdmins(group.getAdmins());
  }

  public void userEnterGroup(long groupId) throws InvalidValueException {
    User user = authService.getAuth();
    Group group = validateGroupExists(groupId);

    boolean userInGroup = group.getUsers().contains(user);

    if (userInGroup)
      throw new InvalidValueException("Usuário já esta no grupo!");

    group.getUsers().add(user);
    user.getGroups().add(group);

    groupRepository.save(group);
    userRepository.save(user);
  }

  public void userQuitGroup(long groupId) throws InvalidValueException {
    User user = authService.getAuth();
    Group group = validateGroupExists(groupId);

    boolean userInGroup = group.getUsers().contains(user);

    if (userInGroup) {
      group.getUsers().remove(user);
      group.getAdmins().remove(user);

      user.getGroups().remove(group);
      user.getAdminGroups().remove(group);
    } else {
      throw new InvalidValueException("Usuário não está neste grupo!");
    }

    groupRepository.save(group);
    userRepository.save(user);
  }

  public void addAdminToGroup(long groupId, long userId) throws InvalidValueException, UnauthorizedEventException {
    User auth = authService.getAuth();
    Group group = validateGroupExists(groupId);
    User user = validateUserExists(userId);

    boolean isAdmin = group.getAdmins().contains(auth);
    boolean userAlredyIsAdmin = group.getAdmins().contains(user);
    boolean userIsNotInGroup = !group.getUsers().contains(user);

    if (!isAdmin)
      throw new UnauthorizedEventException("Usuário não autorizado");

    if (userAlredyIsAdmin)
      throw new InvalidValueException("Usuário já é administrador!");

    group.getAdmins().add(user);
    user.getAdminGroups().add(group);

    if (userIsNotInGroup) {
      group.getUsers().add(user);
      user.getGroups().add(group);
    }

    groupRepository.save(group);
    userRepository.save(user);
  }

  public void adminRemoveAdmin(long groupId, long userId) throws InvalidValueException, UnauthorizedEventException {
    User auth = authService.getAuth();
    Group group = validateGroupExists(groupId);
    User user = validateUserExists(userId);

    boolean isAdmin = group.getAdmins().contains(auth);
    boolean userIsCreator = group.getUserCreator().getId() == user.getId();

    if (!isAdmin)
      throw new UnauthorizedEventException("Usuário não autorizado");

    if (userIsCreator)
      throw new UnauthorizedEventException("Usuário não autorizado");

    group.getAdmins().remove(user);
    user.getAdminGroups().remove(group);

    groupRepository.save(group);
    userRepository.save(user);
  }

  public void adminRemoveUser(long groupId, long userId) throws InvalidValueException, UnauthorizedEventException {
    User auth = authService.getAuth();
    Group group = validateGroupExists(groupId);
    User user = validateUserExists(userId);

    boolean isAdmin = group.getAdmins().contains(auth);
    boolean isUserCreator = group.getUserCreator().getId() == user.getId();

    if (!isAdmin || isUserCreator)
      throw new UnauthorizedEventException("Usuário não autorizado");

    group.getUsers().remove(user);
    group.getAdmins().remove(user);

    user.getGroups().remove(group);
    user.getAdminGroups().remove(group);

    groupRepository.save(group);
    userRepository.save(user);
  }

  public void removeUserFromGroup(long groupId, long userId) throws InvalidValueException, UnauthorizedEventException {
    User auth = authService.getAuth();
    Group group = validateGroupExists(groupId);
    User user = validateUserExists(userId);

    boolean isAdmin = group.getAdmins().contains(auth);
    boolean userIsCreator = group.getUserCreator().getId() == userId;

    if (!isAdmin || userIsCreator)
      throw new UnauthorizedEventException("Usuário não autorizado");

    group.getUsers().remove(user);
    group.getAdmins().remove(user);

    user.getGroups().remove(group);
    user.getAdminGroups().remove(group);

    groupRepository.save(group);
    userRepository.save(user);
  }

  public void adminRemovePost(long groupId, long postId) throws InvalidValueException, UnauthorizedEventException {
    User auth = authService.getAuth();
    Group group = validateGroupExists(groupId);
    Post post = validatePostExists(postId);

    boolean isAdmin = group.getAdmins().contains(auth);
    boolean isPostOfGroup = group.getPosts().contains(post);

    if (!isAdmin || !isPostOfGroup)
      throw new UnauthorizedEventException("Usuário não autorizado");

    if (post.isDeleted()) {
      throw new InvalidValueException("Post já deletado!");
    }

    post.setDeleted(true);

    postRepository.save(post);
  }

  public GroupDto postGroupImage(MultipartFile file, long groupId, String type)
      throws InvalidValueException, UnauthorizedEventException, RuntimeException, IOException {
    User user = authService.getAuth();
    Group group = validateGroupExists(groupId);
    List<String> mimetypes = List.of(
        "image/jpg",
        "image/jpeg",
        "image/png");

    boolean isAdmin = group.getAdmins().contains(user);
    if (!isAdmin)
      throw new UnauthorizedEventException("Usuário sem permissão");

    File createdFile = imageService.saveImage(file, mimetypes);

    if (type.equals("group_banner")) {
      GroupBanner image = new GroupBanner(createdFile.getName(), createdFile.getOriginalName(),
          createdFile.getFileSize(),
          createdFile.getFileUrl(), group);
      GroupBanner groupBanner = group.getGroupBanner();
      if (groupBanner != null) {
        image.setId(groupBanner.getId());
        image.setGroup(group);
      }
      GroupBanner groupImageCreated = groupBannerRepository.save((GroupBanner) image);
      group.setBannerImage(groupImageCreated);
    } else if (type.equals("group_image")) {
      GroupImage image = new GroupImage(createdFile.getName(), createdFile.getOriginalName(),
          createdFile.getFileSize(),
          createdFile.getFileUrl(), group);
      GroupImage groupImage = group.getGroupImage();
      if (groupImage != null) {
        image.setId(groupImage.getId());
        image.setGroup(group);
      }
      GroupImage groupImageCreated = groupImageRepository.save(image);
      group.setGroupImage(groupImageCreated);
    } else {
      throw new InvalidValueException("Tipo não encontrado");
    }
    return GroupDto.FromEntity(groupRepository.save(group));
  }

  public GroupDto updateGroup(Group groupRequest, long groupId)
      throws InvalidValueException, UnauthorizedEventException {
    User user = authService.getAuth();
    Group group = validateGroupExists(groupId);

    if (group.getUserCreator().getId() != user.getId())
      throw new UnauthorizedEventException("Apenas o criador do grupo pode fazer essas alterações!");

    if (groupRequest.getGroupName() != null) {
      if (groupRequest.getGroupName().trim().equals("")) {
        throw new InvalidValueException("Nome do Grupo Inválido!");
      }
      group.setGroupName(groupRequest.getGroupName());
    }

    if (groupRequest.getDescription() != null) {
      group.setDescription(groupRequest.getDescription());
    }

    return GroupDto.FromEntity(groupRepository.save(group));
  }

  public void deleteGroup(long groupId) throws InvalidValueException, UnauthorizedEventException {
    User user = authService.getAuth();
    Group group = validateGroupExists(groupId);

    if (group.getUserCreator().getId() != user.getId())
      throw new UnauthorizedEventException("Apenas o criador do grupo pode fazer essas alterações!");

    group.setDeleted(true);
    groupRepository.save(group);
  }

  private Group validateGroupExists(Long groupId) throws InvalidValueException {
    return groupRepository.findById(groupId).orElseThrow(
        () -> new InvalidValueException("Grupo não encontrado!"));
  }

  private User validateUserExists(Long userId) throws InvalidValueException {
    return userRepository.findById(userId).orElseThrow(
        () -> new InvalidValueException("Usuário não encontrado!"));
  }

  private Post validatePostExists(Long postId) throws InvalidValueException {
    return postRepository.findById(postId)
        .orElseThrow(() -> new InvalidValueException("Post não encontrado"));
  }
}
