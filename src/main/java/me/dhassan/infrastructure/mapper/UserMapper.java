package me.dhassan.infrastructure.mapper;

import me.dhassan.domain.entity.User;
import me.dhassan.infrastructure.entity.UserEntity;

public class UserMapper {
    public static User mapToUserModel(UserEntity userEntity) {
        User user = new User(userEntity.name, userEntity.email, userEntity.password);
        user.id = userEntity.id;
        user.notes = NoteMapper.mapNoteEntitiesToNoteModels(userEntity.noteEntities);

        return user;
    }

    public static UserEntity mapToUserEntity(User user) {
        UserEntity userEntity = new UserEntity(user.name, user.email, user.password);
        userEntity.id = user.id;

        return userEntity;
    }
}
