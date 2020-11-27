package com.kayra.market.kmorms.cms.integration.security.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.kayra.market.kmorms.model.security.LoggedInSeller;

@Component
public final class SecurityContextAccessorImpl implements SecurityContextAccessor {

    @Autowired
    private AuthenticationTrustResolver authenticationTrustResolver;

    @Override
    public boolean isCurrentAuthenticationAnonymous() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return isAnonymous(authentication);
    }

    private boolean isAnonymous(Authentication authentication) {
        return authenticationTrustResolver.isAnonymous(authentication);
    }

    private boolean isAuthenticated(Authentication authentication) {
        return (null != authentication) && authentication.isAuthenticated();
    }

    @Override
    public Authentication currentLoggedInAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticated(authentication) && (!isAnonymous(authentication))) {
            return authentication;
        }
        return null;
    }

    @Override
    public LoggedInSeller currentLoggedInSeller() {
        Authentication authentication = currentLoggedInAuthentication();
        if (null == authentication) {
            return null;
        }
        LoggedInSeller user = (LoggedInSeller) authentication.getPrincipal();

        if ((null == user) || (user.isAnonymous())) {
            return null;
        }
        return user;
    }
}
