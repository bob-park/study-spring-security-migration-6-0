package org.bobpark.studyspringsecurity.security.source;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import org.bobpark.studyspringsecurity.security.model.FormWebAuthenticationDetails;

/**
 * Form 인증 - AuthenticationDetailsSource
 *
 * <p>
 * AuthenticationDetailsSource
 * <pre>
 *      - WebAuthenticationDetails 객체를 생성
 * </pre>
 */
@Component
public class FormAuthenticationDetailsSource implements
    AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new FormWebAuthenticationDetails(context);
    }
}
