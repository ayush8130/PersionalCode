package com.kayra.market.kmorms.cms.security;

import static com.kayra.market.kmorms.cms.integration.security.LoginAnonymizerUtils.anonymizedSessionToken;
import static com.kayra.market.kmorms.cms.security.SecurityToken.SELLER_SECURITY_TOKEN_NAME;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

import com.kayra.market.kmorms.cms.integration.security.SecureResponseWrapper;
import com.kayra.market.kmorms.model.security.LoggedInSeller;

public class CMSSecuritySessionTokenFIlter extends GenericFilterBean {

    private final CMSLoginService booksloginService;

    @Autowired
    public CMSSecuritySessionTokenFIlter(CMSLoginService booksloginService) {
        this.booksloginService = booksloginService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response = new SecureResponseWrapper(response);
        final String sessionId = booksloginService.getSessionIdFrom(request);
        if (StringUtils.isNotBlank(sessionId)) {
            LOG.debug("Found {}", anonymizedSessionToken(sessionId));
            LoggedInSeller user = booksloginService.login(sessionId, request, response);
            if (!booksloginService.isValidUser(user)) {
                LOG.debug("Invalid or no user exists for the given session {}", anonymizedSessionToken(sessionId));
                // fail silently to login. This invalid session id could be an expired session id sent during an anonymous call. There's not created security
                // context for the current call and the call is being served based on the security configuration for the current path for anonymous users
            }
        } else {
            LOG.debug("Invalid or no {} cookie has been set", SELLER_SECURITY_TOKEN_NAME);
        }
        chain.doFilter(request, response);
    }

    private static final Logger LOG = LoggerFactory.getLogger(CMSSecuritySessionTokenFIlter.class);
}
