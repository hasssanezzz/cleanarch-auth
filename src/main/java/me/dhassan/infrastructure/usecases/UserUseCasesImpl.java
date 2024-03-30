package me.dhassan.infrastructure.usecases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.dhassan.domain.entity.User;
import me.dhassan.domain.service.UserUseCases;
import me.dhassan.infrastructure.repository.UserRepositoryImpl;

@ApplicationScoped
public class UserUseCasesImpl implements UserUseCases {

    @Inject
    UserRepositoryImpl userRepository;

    public User createUser(User user) {
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User findUserByEmail(String email) {
        return  userRepository.findByEmail(email);
    }

    @Transactional
    public <IDType> User findUserById(IDType id) {
        return userRepository.findById(id);
    }
}
