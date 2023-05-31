package org.bobpark.studyspringsecurity.security.repository.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

import org.bobpark.studyspringsecurity.security.model.Account;
import org.bobpark.studyspringsecurity.security.model.Role;
import org.bobpark.studyspringsecurity.security.repository.UserRepository;

@Slf4j
@Repository
public class InMemoryUserRepository implements UserRepository {

    private final List<Account> memoryAccounts =
        Collections.synchronizedList(
            List.of(
                Account.builder()
                    .id(1)
                    .age(1)
                    .username("admin")
                    .password("{noop}12345")
                    .userRoles(List.of(Role.USER, Role.MANAGER, Role.ADMIN))
                    .build(),
                Account.builder()
                    .id(2)
                    .age(1)
                    .username("user")
                    .password("{noop}12345")
                    .userRoles(List.of(Role.USER))
                    .build()));

    @Override
    public Optional<Account> findByUsername(String username) {

        return memoryAccounts.stream()
            .filter(account -> account.username().equals(username))
            .findAny();
    }
}
