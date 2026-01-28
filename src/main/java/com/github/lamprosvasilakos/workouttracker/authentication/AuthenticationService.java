package com.github.lamprosvasilakos.workouttracker.authentication;

import com.github.lamprosvasilakos.workouttracker.dto.request.AuthenticationRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.CreateUserRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.AuthenticationResponse;
import com.github.lamprosvasilakos.workouttracker.dto.response.CreateUserResponse;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectAlreadyExistsException;
import com.github.lamprosvasilakos.workouttracker.exception.AuthenticationFailedException;
import com.github.lamprosvasilakos.workouttracker.exception.ValidationException;
import com.github.lamprosvasilakos.workouttracker.mapper.UserMapper;
import com.github.lamprosvasilakos.workouttracker.entity.User;
import com.github.lamprosvasilakos.workouttracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws AuthenticationFailedException {

        try {
            Authentication authentication = authenticationManager.authenticate(
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
        } catch (BadCredentialsException e) {
            throw new AuthenticationFailedException("InvalidCredentials", "Wrong username or password");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public CreateUserResponse registerUser(CreateUserRequest createUserRequest) throws AppObjectAlreadyExistsException, ValidationException {

        if(userRepository.existsByUsername(createUserRequest.username())){
            throw new AppObjectAlreadyExistsException("Username","User with username " + createUserRequest.username() + " already exists");
        }


        User newUser = userMapper.toUserEntity(createUserRequest);
        newUser.setPassword(passwordEncoder.encode(createUserRequest.password()));
        userRepository.save(newUser);
        return userMapper.toCreateUserResponse(newUser);
    }
}
