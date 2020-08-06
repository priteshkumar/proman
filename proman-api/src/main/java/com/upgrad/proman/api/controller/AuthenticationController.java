package com.upgrad.proman.api.controller;

import com.upgrad.proman.api.model.AuthorizedUserResponse;
import com.upgrad.proman.service.business.AuthenticationService;
import com.upgrad.proman.service.entity.UserAuthTokenEntity;
import com.upgrad.proman.service.entity.UserEntity;
import com.upgrad.proman.service.exception.AuthenticationFailedException;
import com.upgrad.proman.service.type.UserStatus;
import java.util.Base64;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//encodedData = "Basic " + window.btoa('luke@code.com:codeluke')

@RestController
public class AuthenticationController {

  @Autowired
  AuthenticationService authenticationService;


  @RequestMapping(method = RequestMethod.POST, path = "/auth/login", produces =
      MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthorizedUserResponse> login(
      @RequestHeader("authorization") final String authorization)
      throws AuthenticationFailedException {
    System.out.println(authorization);
    String[] authData = authorization.split("Basic ");
    System.out.println(authData[1]);
    String[] loginDetails = new String(Base64.getDecoder().decode(authData[1])).split(":");
    UserAuthTokenEntity userAuthTokenEntity = authenticationService.authenticate(loginDetails[0],
        loginDetails[1]);

    UserEntity userEntity = userAuthTokenEntity.getUser();
    AuthorizedUserResponse authorizedUserResponse =
        new AuthorizedUserResponse().id(UUID.fromString(userEntity.getUuid()))
            .emailAddress(userEntity.getEmail())
            .firstName(userEntity.getFirstName())
            .lastName(userEntity.getLastName()).lastLoginTime(userEntity.getLastLoginAt())
            .mobilePhone(userEntity.getMobilePhone())
            .status(UserStatus.getEnum(userEntity.getStatus()).name());

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("access-token",userAuthTokenEntity.getAccessToken());
    return new ResponseEntity<>(authorizedUserResponse, httpHeaders,HttpStatus.OK);
  }
}
