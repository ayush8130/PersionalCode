package com.kayra.market.kmorms.cms.integration.security;

import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import com.kayra.market.kmorms.model.security.LoggedInSeller;

/**
 * Hold {@link LoggedInAdministrator} that has been authenticated either through the
 * login call or re-authenticated via a cookie based session ID (not to be
 * confused the HttpSessions).
 *
 * @author rajeev
 */
public class TokenBasedEmployerAuthentication extends AbstractAuthenticationToken {
    private String sessionId;

    public TokenBasedEmployerAuthentication(LoggedInSeller userAccount, String sessionId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.setDetails(userAccount);
        Assert.notNull(trimToNull(sessionId), "SessionID must be present");
        this.sessionId = sessionId;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return sessionId;
    }

    @Override
    public Object getPrincipal() {
        return getLoggedInSeller();
    }

    private LoggedInSeller getLoggedInSeller() {
        return (LoggedInSeller) getDetails();
    }

    private static final long serialVersionUID = 1L;
}
