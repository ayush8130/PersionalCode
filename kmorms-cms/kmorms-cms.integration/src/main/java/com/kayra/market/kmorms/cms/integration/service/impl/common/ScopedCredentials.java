package com.kayra.market.kmorms.cms.integration.service.impl.common;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;

/**
 * Wraps {@link AuthScope} and {@link Credentials} so it can be easily configured in spring
 */
public class ScopedCredentials {

    private final AuthScope authScope;

    private final Credentials credentials;

    public ScopedCredentials(AuthScope authScope, Credentials credentials) {
        this.authScope = authScope;
        this.credentials = credentials;
    }

    public AuthScope getAuthScope() {
        return authScope;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public String toString() {
        return String.format("ScopeCredentials [ authScope = %s, credentials = %s ]", authScope, credentials);
    }

}
