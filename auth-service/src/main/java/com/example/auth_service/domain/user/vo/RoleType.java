package com.example.auth_service.domain.user.vo;


import lombok.Getter;

@Getter
public enum RoleType {
    USUARIO(1),
    PRODUTOR(2),
    RECRUTADOR(3),
    ADMIN(4);

    @Getter
    private final int level;

    RoleType(int level) {
        this.level = level;
    }

    public boolean covers(RoleType other) {
        return this.level >= other.level;
    }
}
