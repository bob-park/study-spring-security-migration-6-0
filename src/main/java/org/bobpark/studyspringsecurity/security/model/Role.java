package org.bobpark.studyspringsecurity.security.model;

import java.util.Arrays;
import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER"),
    ADMIN("ROLE_ADMIN");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public static Role findByName(String roleName) {
        return Arrays.stream(Role.values())
            .filter(role -> role.getName().equals(roleName))
            .findAny()
            .orElse(null);
    }

}
