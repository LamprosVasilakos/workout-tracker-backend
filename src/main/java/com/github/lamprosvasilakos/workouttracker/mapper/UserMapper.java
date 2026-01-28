package com.github.lamprosvasilakos.workouttracker.mapper;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateUserRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.CreateUserResponse;
import com.github.lamprosvasilakos.workouttracker.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUserEntity(CreateUserRequest request){
        return User.builder()
                .username(request.username())
                .password(request.password())
                .build();
    }

    public CreateUserResponse toCreateUserResponse(User user){
        return new CreateUserResponse(
                user.getId(),
                user.getUsername(),
                user.getCreatedAt()
        );
    }

}
