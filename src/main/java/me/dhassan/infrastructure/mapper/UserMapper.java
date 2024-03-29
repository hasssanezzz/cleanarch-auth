package me.dhassan.infrastructure.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.dhassan.domain.entity.Note;
import me.dhassan.domain.entity.User;
import me.dhassan.infrastructure.entity.UserEntity;

@ApplicationScoped
public class UserMapper {
    @Inject
    NoteMapper noteMapper;

    public User mapToUserModel(UserEntity userEntity) {
        User user = new User(userEntity.name, userEntity.email, userEntity.password);
        user.id = userEntity.id;
        user.notes = noteMapper.mapNoteEntitiesToNoteModels(userEntity.noteEntities);

        return user;
    }

    public UserEntity mapToUserEntity(User user) {
        UserEntity userEntity = new UserEntity(user.name, user.email, user.password);

        userEntity.id = user.id;

        return userEntity;
    }
}
