package io.github.shampooshrek.reddit.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.github.shampooshrek.reddit.enums.ResponseType;
import io.github.shampooshrek.reddit.exceptions.InvalidValueException;
import io.github.shampooshrek.reddit.exceptions.UnauthorizedEventException;
import io.github.shampooshrek.reddit.models.ApiResponse;
import io.github.shampooshrek.reddit.models.Group;
import io.github.shampooshrek.reddit.models.dto.GroupDto;
import io.github.shampooshrek.reddit.services.GroupService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

  @Autowired
  private GroupService groupService;

  @GetMapping
  public ResponseEntity<ApiResponse> getGroups() {
    try {
      List<GroupDto> groups = groupService.getGroups();
      ApiResponse response = new ApiResponse(groups, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return ApiResponse.internalErrorResponse();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getGroup(@PathVariable long id) {
    try {
      GroupDto groups = groupService.getById(id);
      ApiResponse response = new ApiResponse(groups, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return ApiResponse.internalErrorResponse();
    }
  }

  @PostMapping
  public ResponseEntity<ApiResponse> createGroup(@RequestBody Group group) {
    try {
      GroupDto createdGroup = groupService.createGroup(group);
      ApiResponse response = new ApiResponse(createdGroup, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse> updateGroup(@RequestBody Group group, @PathVariable long id) {
    try {
      GroupDto createdGroup = groupService.updateGroup(group, id);
      ApiResponse response = new ApiResponse(createdGroup, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (UnauthorizedEventException e) {
      return ApiResponse.noAuthorizatonResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteGroup(@PathVariable long id) {
    try {
      groupService.deleteGroup(id);
      ApiResponse response = new ApiResponse("OK!", ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (UnauthorizedEventException e) {
      return ApiResponse.noAuthorizatonResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @RequestMapping(value = "/{groupId}/users/action", method = { RequestMethod.POST, RequestMethod.DELETE })
  public ResponseEntity<ApiResponse> userGroupAction(@PathVariable long groupId, HttpServletRequest request) {
    try {
      System.out.println(request.getMethod());
      if (request.getMethod().equals("POST")) {
        groupService.userEnterGroup(groupId);
      } else {
        groupService.userQuitGroup(groupId);
      }

      ApiResponse response = new ApiResponse("OK!", ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @RequestMapping(value = "/{groupId}/admins/action/users/{userId}", method = {
      RequestMethod.POST,
      RequestMethod.DELETE })
  public ResponseEntity<ApiResponse> adminGroupUsersAction(@PathVariable long groupId, @PathVariable long userId,
      HttpServletRequest request) {
    try {
      if (request.getMethod().equals("POST")) {
        groupService.addAdminToGroup(groupId, userId);
      } else {
        groupService.adminRemoveUser(groupId, userId);
      }
      ApiResponse response = new ApiResponse("OK!", ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (UnauthorizedEventException e) {
      return ApiResponse.noAuthorizatonResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @DeleteMapping(value = "/{groupId}/admins/action/admins/{userId}")
  public ResponseEntity<ApiResponse> adminGroupAdminsAction(@PathVariable long groupId, @PathVariable long userId) {
    try {
      groupService.adminRemoveAdmin(groupId, userId);
      ApiResponse response = new ApiResponse("OK!", ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (UnauthorizedEventException e) {
      return ApiResponse.noAuthorizatonResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @DeleteMapping("/{groupId}/admins/action/posts/{postId}")
  private ResponseEntity<ApiResponse> adminGroupPostAction(@PathVariable long groupId, @PathVariable long postId) {
    try {
      groupService.adminRemovePost(groupId, postId);

      ApiResponse response = new ApiResponse("OK!", ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (InvalidValueException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (UnauthorizedEventException e) {
      return ApiResponse.noAuthorizatonResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }

  @PostMapping("/{groupId}/image/{type}")
  public ResponseEntity<ApiResponse> uploadImage(@RequestParam("file") MultipartFile file, @PathVariable long groupId,
      @PathVariable String type) {
    try {
      GroupDto group = groupService.postGroupImage(file, groupId, type);
      ApiResponse response = new ApiResponse(group, ResponseType.SUCCESS);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (InvalidValueException | RuntimeException | IOException e) {
      return ApiResponse.invalidValueResponse(e.getMessage());
    } catch (Exception e) {
      return ApiResponse.internalErrorResponse();
    }
  }
}
