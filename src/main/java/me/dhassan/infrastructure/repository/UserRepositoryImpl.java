package me.dhassan.infrastructure.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import me.dhassan.domain.entity.User;
import me.dhassan.domain.repository.UserRepository;
import me.dhassan.infrastructure.entity.UserEntity;
import me.dhassan.infrastructure.mapper.UserMapper;

import java.util.List;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {

    @Inject
    EntityManager entityManager;

    @Inject
    UserMapper userMapper;

    public <IDType> User findById(IDType id) {
        return userMapper.mapToUserModel(entityManager.find(UserEntity.class, id));
    }

    public User findByEmail(String email) {
        String jpql = "SELECT u FROM user u WHERE u.email = :email";
        TypedQuery<UserEntity> query = entityManager.createQuery(jpql, UserEntity.class);
        query.setParameter("email", email);
        List<UserEntity> resultList = query.getResultList();
        return resultList.isEmpty() ? null : userMapper.mapToUserModel(resultList.getFirst());
    }

    @Transactional
    public void save(User user) {
        UserEntity userEntity = userMapper.mapToUserEntity(user);
        entityManager.persist(userEntity);
    }

    @Transactional
    public void update(User user) {
        UserEntity userEntity = userMapper.mapToUserEntity(findById(user.id));

        userEntity.name = user.name == null ? userEntity.name : user.name;
        userEntity.email = user.email == null ? userEntity.email : user.email;

        entityManager.merge(userEntity);
    }

    @Transactional
    public void delete(User user) {
        entityManager.remove(userMapper.mapToUserEntity(user));
    }
}
