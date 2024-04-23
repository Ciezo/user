package com.userservice.user.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-service/v1/demo")
public class DemoController {

    /**
     * This endpoint requires Authentication
     * @return `true` if a user is logged-in with an assigned JWT token
     */
    @GetMapping("/auth/test")
    public ResponseEntity<String> test_auth() {
        return ResponseEntity.ok("true");
    }

    /**
     * This endpoint can be used with no Authentication
     * @return `true` always even if it has no authentication
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("true");
    }

}
