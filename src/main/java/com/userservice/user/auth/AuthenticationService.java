package com.userservice.user.auth;

import com.userservice.user.User;
import com.userservice.user.UserRepository;
import com.userservice.user.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder pwdEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;



    /**
     * Register User (Sign-up)
     * @param request `RegisterRequest` class which entails the following attributes:
     * firstname, lastname, email, and password
     *
     * @return `AuthenticationResponse`, a JWT token,
     * and a registered User in a MySQL database.
     */
    public AuthenticationResponse register(RegisterRequest request) {
        /* Define our User who is signing-up */
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(pwdEncoder.encode(request.getPassword()))
                .build();
        /* Save into database using JPA hibernate */
        User saveUserToDb = userRepository.save(user);
        /* Generate JWT token once the user is in MySQL database */
        String jwtToken = jwtService.generateToken(saveUserToDb);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }



    /**
     * Authenticate User (Sign-in)
     * @param request `AuthenticationRequest` class which consists of username and password
     * @return `AuthenticationResponse`, a JWT token. It can be set as a Cookie for
     * User Management Sessions
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();
        UsernamePasswordAuthenticationToken signIn = new UsernamePasswordAuthenticationToken(email, password);
        /** Sign-in and authenticate user */
        authManager.authenticate(signIn);

        User userSigningIn = userRepository.findByEmail(email)
                .orElseThrow();

        /** * After finding the user, create a JWT Token */
        String authenticatedUserJWT = jwtService.generateToken(userSigningIn);

        return AuthenticationResponse.builder()
                .token(authenticatedUserJWT)
                .build();
    }

}
