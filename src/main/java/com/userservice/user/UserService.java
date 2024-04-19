package com.userservice.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> getAllUserDetailsByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user;
    }

}
