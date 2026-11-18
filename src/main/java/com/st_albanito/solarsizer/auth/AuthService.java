package com.st_albanito.solarsizer.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest) {
        String normalizedEmail = registerRequest.email().trim().toLowerCase();
        if(userRepository.existsByEmail(normalizedEmail)){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");
        }
        User user = User.builder()
                .email(normalizedEmail)
                .passwordHash(passwordEncoder.encode(registerRequest.password()))
                .displayName(registerRequest.displayName().trim())
                .role("USER")
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getDisplayName(),
                savedUser.getRole()
        );
    }

    public AuthResponse login(LoginRequest loginRequest) {
        String normalizedEmail = loginRequest.email().trim().toLowerCase();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail,loginRequest.password())
        );

        User user = userRepository.findByEmail(normalizedEmail).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid Credentials"));
        String token = jwtService.generateToken(user);
        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getRole()
        );
    }
}
