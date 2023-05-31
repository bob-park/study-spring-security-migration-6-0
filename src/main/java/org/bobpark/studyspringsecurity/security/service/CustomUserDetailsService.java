package org.bobpark.studyspringsecurity.security.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.bobpark.studyspringsecurity.security.context.AccountContext;
import org.bobpark.studyspringsecurity.security.model.Account;
import org.bobpark.studyspringsecurity.security.model.Role;
import org.bobpark.studyspringsecurity.security.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account =
            userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("not found username."));

        List<? extends GrantedAuthority> collect =
            account.userRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet())
                .stream().map(SimpleGrantedAuthority::new)
                .toList();

        return new AccountContext(account, collect);
    }
}
