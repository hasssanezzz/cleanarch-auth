package me.dhassan.domain.service;


import me.dhassan.domain.entity.User;

public interface UserUseCases {
    User createUser(User User);
    User findUserByEmail(String email);
    <IDType> User findUserById(IDType id);
}
