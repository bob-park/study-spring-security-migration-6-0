package org.bobpark.studyspringsecurity.security.model;

import java.util.List;

import lombok.Builder;

@Builder
public record Account(long id, String username, String password, String email, int age, List<Role> userRoles) {
}
