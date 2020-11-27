package com.kayra.market.kmorms.cms.integration.security;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.security.core.Authentication;

/**
 * Custom {@link HttpServletRequestWrapper} class that provides the current {@link Authentication} object as the principal immediately after an anonymous user
 * session is created.
 * This is needed as the request object does not contain the required cookie as we have just
 * created the session
 */
public class AnonymousUserCreationRequestWrapper extends HttpServletRequestWrapper {

    private final Authentication authentication;

    public AnonymousUserCreationRequestWrapper(HttpServletRequest request, Authentication authentication) {
        super(request);
        this.authentication = authentication;
    }

    /**
     * Returns the {@link Authentication} object in the security context
     */
    @Override
    public Principal getUserPrincipal() {
        return (Principal) authentication;
    }

}
