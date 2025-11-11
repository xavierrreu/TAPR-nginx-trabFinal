package com.example.auth_service.application.user;

import com.example.auth_service.application.ports.PasswordHasher;
import com.example.auth_service.domain.user.User;
import com.example.auth_service.domain.user.UserRepository;
import com.example.auth_service.domain.user.vo.Email;
import com.example.auth_service.domain.user.vo.RoleType;
import com.example.auth_service.interfaces.rest.dto.user.UserResponse;
import com.example.auth_service.messaging.events.UserCreatedEvent;
import com.example.auth_service.messaging.events.UserCreatedPublisher;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserHandler {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final UserCreatedPublisher userCreatedPublisher;

    @Transactional
    public UserResponse handle(String nome, String emailRaw, String senha) {
        Email email = Email.of(emailRaw);

        String hash = passwordHasher.hash(senha);
        User user = new User(nome, hash, email, RoleType.CUSTOMER);

        User saved = userRepository.save(user);

        userCreatedPublisher.publish(
            new UserCreatedEvent(
                saved.getId(),
                saved.getEmail().getValue(),
                saved.getName(),
                saved.getRole().getValue().name()
            )
        );
        
        return new UserResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail().getValue(),
                saved.getRole().getValue().name()
        );
    }
}
