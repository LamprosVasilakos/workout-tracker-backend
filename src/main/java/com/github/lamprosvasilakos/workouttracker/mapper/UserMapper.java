package com.github.lamprosvasilakos.workouttracker.mapper;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateUserRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.CreateUserResponse;
import com.github.lamprosvasilakos.workouttracker.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toUserEntity(CreateUserRequest request){
        return User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
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
