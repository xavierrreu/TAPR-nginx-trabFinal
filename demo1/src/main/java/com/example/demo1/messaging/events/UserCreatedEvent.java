package com.example.demo1.messaging.events;

import java.util.UUID;

public record UserCreatedEvent(
    UUID userId,
    String email,
    String name,
    String role
) {}
