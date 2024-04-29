package com.userservice.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user-service/v1/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * This returns a list of all `UserDetails` based on the
     * `User` model defined. Moreover, a username defined here
     * is an email.
     *
     * Email addresses are unique hence, it is defined as an attribute
     * to unique identify the names of our users in the database.
     * @param email attribute from the `User`
     * @return UserDetails attributes
     */
    @GetMapping("/un/{username}")
    public ResponseEntity<Optional<User>> getAllUserDetailsByUsername(
            @PathVariable(value = "username") String email
    ) {
        return new ResponseEntity<>(
                userService.getAllUserDetailsByEmail(email),
                HttpStatus.OK
        );
    }
}
