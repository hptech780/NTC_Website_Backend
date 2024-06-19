package com.blog.post.controller;








import com.blog.post.entity.User;
import com.blog.post.repositories.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/google")
    public User authenticateWithGoogle(@RequestBody TokenRequest tokenRequest) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(tokenRequest.getToken());
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String username = (String) payload.get("name");
//            String picture = (String) payload.get("picture");

            User user = userRepository.findByEmail(email);
            if (user == null) {
                user = new User();
                user.setUsername(username);
                user.setEmail(email);
               
                user = userRepository.save(user);
            }

            return user;
        } else {
            throw new RuntimeException("Invalid ID token.");
        }
    }
}


@Getter
@Setter
class TokenRequest {
    private String token;

    // Getters and setters
}
