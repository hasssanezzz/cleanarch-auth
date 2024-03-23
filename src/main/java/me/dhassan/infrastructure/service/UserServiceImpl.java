package me.dhassan.infrastructure.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import me.dhassan.domain.entity.User;
import me.dhassan.domain.service.UserService;
import me.dhassan.infrastructure.entity.UserEntity;
import me.dhassan.infrastructure.mapper.UserMapper;

import java.util.List;

@ApplicationScoped
public class UserServiceImpl implements UserService {
    @Inject
    EntityManager entityManager;

    @Transactional
    public User createUser(User user) {
        UserEntity userEntity = UserMapper.mapToUserEntity(user);
        entityManager.persist(userEntity);
        return user;
    }

    @Transactional
    public User findUserByEmail(String email) {
        String jpql = "SELECT u FROM user u WHERE u.email = :email";
        TypedQuery<UserEntity> query = entityManager.createQuery(jpql, UserEntity.class);
        query.setParameter("email", email);
        List<UserEntity> resultList = query.getResultList();
        return resultList.isEmpty() ? null : UserMapper.mapToUserModel(resultList.getFirst());
    }

    @Transactional
    public <IDType> User findUserById(IDType id) {
        return UserMapper.mapToUserModel(entityManager.find(UserEntity.class, id));
    }
}
