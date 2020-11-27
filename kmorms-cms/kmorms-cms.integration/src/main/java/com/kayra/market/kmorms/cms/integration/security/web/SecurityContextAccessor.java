package com.kayra.market.kmorms.cms.integration.security.web;

import org.springframework.security.core.Authentication;

import com.kayra.market.kmorms.model.security.LoggedInSeller;

/**
 * Provides security context access functionality
 */
public interface SecurityContextAccessor {
    boolean isCurrentAuthenticationAnonymous();

    Authentication currentLoggedInAuthentication();

    LoggedInSeller currentLoggedInSeller();
}
