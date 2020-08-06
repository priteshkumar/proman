package com.upgrad.proman.api.controller;

import com.upgrad.proman.api.model.CreateUserRequest;
import com.upgrad.proman.api.model.CreateUserResponse;
import com.upgrad.proman.api.model.UserDetailsResponse;
import com.upgrad.proman.api.model.UserStatusType;
import com.upgrad.proman.service.business.UserBusinessService;
import com.upgrad.proman.service.entity.UserEntity;
import com.upgrad.proman.service.exception.ResourceNotFoundException;
import com.upgrad.proman.service.exception.UnAuthorizedException;
import com.upgrad.proman.service.type.UserStatus;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAdminController {

  @Autowired
  private UserBusinessService userBusinessService;

  @RequestMapping(method = RequestMethod.GET, path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDetailsResponse> getUser(@PathVariable("id") final String uuid,
      @RequestHeader("authorization") String authorization)
      throws ResourceNotFoundException, UnAuthorizedException {
    System.out.println(authorization);
    String accessToken = authorization.split("Bearer ")[1];
    System.out.println(accessToken);
    UserEntity userEntity = userBusinessService.getUser(uuid, accessToken);
    UserDetailsResponse userDetailsResponse =
        new UserDetailsResponse().id(userEntity.getUuid()).emailAddress(userEntity.getEmail())
            .firstName(userEntity.getFirstName()).lastName(userEntity.getLastName())
            .mobileNumber(userEntity.getMobilePhone())
            .status(UserStatusType.valueOf(UserStatus.getEnum(userEntity.getStatus()).name()));
    return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST, path = "/users", consumes =
      MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CreateUserResponse> createUser(final CreateUserRequest createUserRequest) {
    UserEntity userEntity = new UserEntity();
    userEntity.setUuid(UUID.randomUUID().toString());
    userEntity.setEmail(createUserRequest.getEmailAddress());
    userEntity.setFirstName(createUserRequest.getFirstName());
    userEntity.setLastName(createUserRequest.getLastName());
    userEntity.setMobilePhone(createUserRequest.getMobileNumber());
    userEntity.setStatus(UserStatus.ACTIVE.getCode());
    userEntity.setCreatedAt(ZonedDateTime.now());
    userEntity.setCreatedBy("api-backend");

    UserEntity createdUser = userBusinessService.createUser(userEntity);
    CreateUserResponse createUserResponse =
        new CreateUserResponse().id(createdUser.getUuid())
            .status(UserStatusType.valueOf(UserStatus.getEnum(createdUser.getStatus()).name()));
    return new ResponseEntity<>(createUserResponse, HttpStatus.CREATED);
  }
}