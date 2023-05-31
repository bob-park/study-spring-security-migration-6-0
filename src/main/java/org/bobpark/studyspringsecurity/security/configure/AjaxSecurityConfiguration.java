package org.bobpark.studyspringsecurity.security.configure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.bobpark.studyspringsecurity.security.entrypoint.AjaxLoginAuthenticationEntryPoint;
import org.bobpark.studyspringsecurity.security.filter.AjaxLoginProcessingFilter;
import org.bobpark.studyspringsecurity.security.handler.AjaxAccessDeniedHandler;
import org.bobpark.studyspringsecurity.security.handler.AjaxAuthenticationFailureHandler;
import org.bobpark.studyspringsecurity.security.handler.AjaxAuthenticationSuccessHandler;
import org.bobpark.studyspringsecurity.security.provider.AjaxAuthenticationProvider;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class AjaxSecurityConfiguration {

    private final ObjectMapper mapper;

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Order(0)
    public SecurityFilterChain ajaxFilterChin(HttpSecurity http) throws Exception {

        http.securityMatcher("/api/**");

        http.authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers("/api/login").permitAll()
                    .requestMatchers("/api/messages").hasRole("MANAGER")
                    .requestMatchers("/api/**").authenticated());

        //
        http
            .securityContext(securityContext -> securityContext.requireExplicitSave(false))
            .addFilterBefore(ajaxLoginProcessingFilter(null), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(
            exception ->
                exception
                    .authenticationEntryPoint(new AjaxLoginAuthenticationEntryPoint())
                    .accessDeniedHandler(ajaxAccessDeniedHandler()));

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }


    @Bean
    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter(AuthenticationManager authenticationManager) {

        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter(mapper);

        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManager);
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler());
        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler());

        return ajaxLoginProcessingFilter;
    }

    @Bean
    public AuthenticationProvider ajaxAuthenticationProvider() {
        return new AjaxAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public AuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new AjaxAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
        return new AjaxAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler ajaxAccessDeniedHandler() {
        return new AjaxAccessDeniedHandler();
    }

}
