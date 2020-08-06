package com.upgrad.proman.service.business;

import com.upgrad.proman.service.common.JwtTokenProvider;
import com.upgrad.proman.service.dao.UserDao;
import com.upgrad.proman.service.entity.UserAuthTokenEntity;
import com.upgrad.proman.service.entity.UserEntity;
import com.upgrad.proman.service.exception.AuthenticationFailedException;
import java.time.ZonedDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

  @Autowired
  private UserDao userDao;

  @Autowired
  private PasswordCryptographyProvider passwordCryptographyProvider;

  @Transactional(propagation = Propagation.REQUIRED)
  public UserAuthTokenEntity authenticate(final String email, final String password)
      throws AuthenticationFailedException {
    UserEntity userEntity = userDao.getUserByEmail(email);
    if (userEntity == null) {
      throw new AuthenticationFailedException("ATH-001", "user email is invalid");
    }
    String encryptedPassword = passwordCryptographyProvider.encrypt(password, userEntity.getSalt());
    if (encryptedPassword.equals(userEntity.getPassword())) {
      UserAuthTokenEntity userAuthTokenEntity = new UserAuthTokenEntity();
      userAuthTokenEntity.setUser(userEntity);
      ZonedDateTime now = ZonedDateTime.now();
      ZonedDateTime expires = now.plusHours(8);
      userAuthTokenEntity.setAccessToken(
          new JwtTokenProvider(encryptedPassword)
              .generateToken(userEntity.getUuid(), now, expires));
      userAuthTokenEntity.setCreatedAt(now);
      userAuthTokenEntity.setLoginAt(now);
      userAuthTokenEntity.setExpiresAt(expires);
      userAuthTokenEntity.setCreatedBy("api-backend");
      userDao.createAuthToken(userAuthTokenEntity);
      userEntity.setLastLoginAt(now);
      userDao.updateUser(userEntity);
      return userAuthTokenEntity;
    } else {
      throw new AuthenticationFailedException("ATH-002", "user password is invalid");
    }
  }
}
