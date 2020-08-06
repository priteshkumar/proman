package com.upgrad.proman.service.business;

import com.upgrad.proman.service.dao.UserDao;
import com.upgrad.proman.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignUpBusinessService {

  @Autowired
  private UserDao userDao;

  @Autowired
  UserBusinessService userBusinessService;

  @Autowired
  private PasswordCryptographyProvider passwordCryptographyProvider;

  @Transactional(propagation = Propagation.REQUIRED)
  public UserEntity signup(UserEntity userEntity) {
    /*String[] passWordData = passwordCryptographyProvider.encrypt(userEntity.getPassword());
    userEntity.setSalt(passWordData[0]);
    userEntity.setPassword(passWordData[1]);
    return userDao.createUser(userEntity);*/
    return userBusinessService.createUser(userEntity);
  }
}
