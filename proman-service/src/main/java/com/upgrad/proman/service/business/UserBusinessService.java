package com.upgrad.proman.service.business;

import com.upgrad.proman.service.dao.UserDao;
import com.upgrad.proman.service.entity.RoleEntity;
import com.upgrad.proman.service.entity.UserAuthTokenEntity;
import com.upgrad.proman.service.entity.UserEntity;
import com.upgrad.proman.service.exception.ResourceNotFoundException;
import com.upgrad.proman.service.exception.UnAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserBusinessService {

  @Autowired
  UserDao userDao;

  @Autowired
  PasswordCryptographyProvider passwordCryptographyProvider;

  //@Transactional(propagation = Propagation.REQUIRED)
  public UserEntity getUser(String userUUID,String authorization)
      throws ResourceNotFoundException, UnAuthorizedException {
    UserAuthTokenEntity authToken = userDao.getAuthToken(authorization);
    RoleEntity role = authToken.getUser().getRole();
    if(role != null && role.getUuid() == 101) {
      UserEntity userEntity = userDao.getUser(userUUID);
      if (userEntity == null) {
        throw new ResourceNotFoundException("USR-001", "User not found");
      }
      return userEntity;
    }
    throw new UnAuthorizedException("ATH-002", "you are not authorized to fetch user details");
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public UserEntity createUser(UserEntity userEntity){
    if(userEntity.getPassword() == null){
      userEntity.setPassword("proman@123");
    }
    String[] userPassword = passwordCryptographyProvider.encrypt(userEntity.getPassword());
    userEntity.setSalt(userPassword[0]);
    userEntity.setPassword(userPassword[1]);
    return userDao.createUser(userEntity);
  }
}
