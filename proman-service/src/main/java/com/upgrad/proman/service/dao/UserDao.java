package com.upgrad.proman.service.dao;

import com.upgrad.proman.service.entity.UserAuthTokenEntity;
import com.upgrad.proman.service.entity.UserEntity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

  @PersistenceContext
  private EntityManager entityManager;

  public UserEntity createUser(UserEntity userEntity) {
    entityManager.persist(userEntity);
    return userEntity;
  }

  public UserEntity getUser(String userUUID) {
    TypedQuery<UserEntity> query = entityManager.createNamedQuery("userByUuid", UserEntity.class);
    query.setParameter("uuid", userUUID);
    try {
      UserEntity userEntity = query.getSingleResult();
      return userEntity;
    } catch (NoResultException e) {
      return null;
    }
  }

  public UserEntity getUserByEmail(final String email) {
    try {
      TypedQuery<UserEntity> query = entityManager
          .createNamedQuery("userByEmail", UserEntity.class);
      query.setParameter("email", email);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public void updateUser(UserEntity updatedUserEntity) {
    entityManager.merge(updatedUserEntity);
  }

  public UserAuthTokenEntity createAuthToken(UserAuthTokenEntity userAuthTokenEntity) {
    entityManager.persist(userAuthTokenEntity);
    return userAuthTokenEntity;
  }

  public UserAuthTokenEntity getAuthToken(String accessToken) {
    try {
      return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class)
          .setParameter("accessToken", accessToken).getSingleResult();
    }catch(NoResultException e){
      return null;
    }
  }
}
