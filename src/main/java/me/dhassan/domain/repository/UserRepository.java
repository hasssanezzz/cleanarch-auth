package me.dhassan.domain.repository;

import me.dhassan.domain.entity.User;

public interface UserRepository {
    <IDType> User findById(IDType id);
    User findByEmail(String email);
    void save(User user);
    void update(User user);
    void delete(User user);
}
