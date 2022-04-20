package com.quest.etna.config;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.repositories.UserRepository;

@Service
public class JwtCheckingService {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    private String username;
    private String password;
    private JwtUserDetails userDetails;

    @Autowired
    public JwtCheckingService(UserRepository userRepo, JwtTokenUtil jwtTokenUtil,
                              AuthenticationManager authenticationManager) {
        this.userRepository = userRepo;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    public void setupUser(String username, String password) {
        this.username = username;
        this.password = password;
        Optional<User> user = userRepository.findByUsername(username);
        this.userDetails = new JwtUserDetails(user.get());
    }

    public String getValidToken() throws Exception {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(this.username, this.password)
        );
        return jwtTokenUtil.generateToken(userDetails);
    }
}
