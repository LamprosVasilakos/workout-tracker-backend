
package com.github.lamprosvasilakos.workouttracker.authentication;

import com.github.lamprosvasilakos.workouttracker.dto.request.AuthenticationRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.CreateUserRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.AuthenticationResponse;
import com.github.lamprosvasilakos.workouttracker.dto.response.CreateUserResponse;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectAlreadyExistsException;
import com.github.lamprosvasilakos.workouttracker.exception.ValidationException;
import com.github.lamprosvasilakos.workouttracker.mapper.UserMapper;
import com.github.lamprosvasilakos.workouttracker.model.User;
import com.github.lamprosvasilakos.workouttracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {

        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.username(),
                        authenticationRequest.password()
                )
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(authentication.getName());

        return new AuthenticationResponse(
                user.getUsername(),
                token
        );
    }

    @Transactional(rollbackOn = Exception.class)
    public CreateUserResponse registerUser(CreateUserRequest createUserRequest) throws AppObjectAlreadyExistsException, ValidationException {

        if(userRepository.existsByUsername(createUserRequest.username())){
            throw new AppObjectAlreadyExistsException("Username","User with username " + createUserRequest.username() + " already exists");
        }


        User newUser = userMapper.toUserEntity(createUserRequest);
        userRepository.save(newUser);
        return userMapper.toCreateUserResponse(newUser);
    }
}
