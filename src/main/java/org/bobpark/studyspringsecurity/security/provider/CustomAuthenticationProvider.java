package org.bobpark.studyspringsecurity.security.provider;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.bobpark.studyspringsecurity.security.context.AccountContext;
import org.bobpark.studyspringsecurity.security.model.FormWebAuthenticationDetails;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        // * 검증 로직

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(
            username);

        // password 검증
        if (!passwordEncoder.matches(password, accountContext.getAccount().password())) {
            throw new BadCredentialsException("Bad credentials.");
        }

        // 정책에 따라 추가 검증할 수도 있음
        FormWebAuthenticationDetails formWebAuthenticationDetails = (FormWebAuthenticationDetails) authentication.getDetails();

        String secretKey = formWebAuthenticationDetails.getSecretKey();

        if (!"secret".equals(secretKey)) {
            throw new InsufficientAuthenticationException("Insufficient authentication.");
        }

        // 검증이 끝나면 AuthenticationToken 을 생성하여 return 한다.
        return new UsernamePasswordAuthenticationToken(accountContext.getAccount(),
            null,
            accountContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // * AuthenticationProvider 에서 사용하고자 하는 Authentication Token 이 맞는지 확인
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
