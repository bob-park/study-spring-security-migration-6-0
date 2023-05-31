package org.bobpark.studyspringsecurity.security.configure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.bobpark.studyspringsecurity.security.provider.CustomAuthenticationProvider;
import org.bobpark.studyspringsecurity.security.repository.UserRepository;
import org.bobpark.studyspringsecurity.security.service.CustomUserDetailsService;
import org.bobpark.studyspringsecurity.security.source.FormAuthenticationDetailsSource;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class DefaultSecurityConfiguration {

    private final FormAuthenticationDetailsSource authenticationDetailsSource;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    @Order(1)
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
            request ->

                request
                    .requestMatchers("/login*", "/error").permitAll()
                    .anyRequest().authenticated());

        http.formLogin(
            formLogin ->
                formLogin
                    .loginPage("/login")
                    .loginProcessingUrl("/login_proc")
                    .authenticationDetailsSource(authenticationDetailsSource)
                    .defaultSuccessUrl("/")
                    .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService(null), passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider ajaxAuthenticationProvider) {
        return new ProviderManager(authenticationProvider(), ajaxAuthenticationProvider);
    }
}
