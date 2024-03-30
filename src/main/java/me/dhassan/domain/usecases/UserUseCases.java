package me.dhassan.domain.usecases;


import me.dhassan.domain.entity.User;

public interface UserUseCases {
    User createUser(User User);
    User findUserByEmail(String email);
    <IDType> User findUserById(IDType id);
}
