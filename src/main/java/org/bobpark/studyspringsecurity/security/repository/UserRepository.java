package org.bobpark.studyspringsecurity.security.repository;

import java.util.Optional;

import org.bobpark.studyspringsecurity.security.model.Account;

public interface UserRepository {

    Optional<Account> findByUsername(String username);
}
