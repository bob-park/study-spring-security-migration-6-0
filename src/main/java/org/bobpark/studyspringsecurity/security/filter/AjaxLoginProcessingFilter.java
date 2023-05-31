package org.bobpark.studyspringsecurity.security.filter;

import static org.springframework.util.StringUtils.hasText;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.bobpark.studyspringsecurity.security.model.AccountDto;
import org.bobpark.studyspringsecurity.security.token.AjaxAuthenticationToken;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper mapper;

    public AjaxLoginProcessingFilter(ObjectMapper mapper) {
        // 해당 pattern 과 일치하면 작동
        super(new AntPathRequestMatcher("/api/login"));

        this.mapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {

        if (!isAjax(request)) {
            throw new IllegalStateException("Authentication is not support.");
        }

        AccountDto accountDto = mapper.readValue(request.getReader(), AccountDto.class);

        if (!hasText(accountDto.username()) || !hasText(accountDto.password())) {
            throw new IllegalArgumentException("Username or Password is empty.");
        }

        AjaxAuthenticationToken ajaxAuthenticationToken =
            new AjaxAuthenticationToken(accountDto.username(), accountDto.password());

        Authentication authenticate = getAuthenticationManager().authenticate(ajaxAuthenticationToken);

        return authenticate;
    }

    private boolean isAjax(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");

        return "XMLHttpRequest".equals(header);
    }
}
