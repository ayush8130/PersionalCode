package com.kayra.market.kmorms.cms.security;

import static com.kayra.market.kmorms.cms.integration.security.LoginAnonymizerUtils.anonymizedSessionToken;
import static com.kayra.market.kmorms.cms.security.CookieUtils.isValidSellerSessionCookie;
import static com.kayra.market.kmorms.cms.security.SecurityToken.SELLER_SECURITY_TOKEN_NAME;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.util.WebUtils;

import com.kayra.market.kmorms.cms.integration.service.KmormsAuthenticationService;
import com.kayra.market.kmorms.model.admin.AdminLoginStatus;
import com.kayra.market.kmorms.model.security.LoggedInSeller;
import com.kayra.market.kmorms.model.security.LoggedInSellerBuilder;

public class CMSLoginServiceImpl implements CMSLoginService {

    private static final Logger LOG = LoggerFactory.getLogger(CMSLoginServiceImpl.class);
    private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    private final SecureRandom random;
    private KmormsAuthenticationService kmormsAuthenticationService;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * @param random
     *            the random number generator used for generating token data
     * @param kmormsAuthenticationService
     *            the session registry used to track the login tokens
     * @param authorities
     *            list of authorities automatically assigned upon login
     */
    public CMSLoginServiceImpl(SecureRandom random, KmormsAuthenticationService kmormsAuthenticationService, List<GrantedAuthority> authorities) {
        this.random = random;
        this.kmormsAuthenticationService = kmormsAuthenticationService;
        this.authorities = authorities;
    }

    /**
     * @param kmormsAuthenticationService
     *            the session registry used to track the login tokens
     * @param authorities
     *            list of authorities automatically assigned upon login
     */
    public CMSLoginServiceImpl(KmormsAuthenticationService kmormsAuthenticationService, List<GrantedAuthority> authorities) {
        this(new SecureRandom(), kmormsAuthenticationService, authorities);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = getSessionIdFrom(request);
        if (StringUtils.isBlank(sessionId)) {
            LOG.debug("Logout was requested from a not logged in user");
        } else {
          //  logout(sessionId, request, response);
        }

    }

    @Override
    public String getSessionIdIfValid(HttpServletRequest request) {
        String sessionId = getSessionIdFrom(request);
        return (null != getLoggedInUserIfValid(sessionId)) ? sessionId : null;
    }

    @Override
    public String getSessionIdFrom(HttpServletRequest request) {
        final Cookie sessionCookie = WebUtils.getCookie(request, SELLER_SECURITY_TOKEN_NAME.toString());
        if (!isValidSellerSessionCookie(sessionCookie)) {
            LOG.debug("Not found {} session cookie.", SELLER_SECURITY_TOKEN_NAME);
            return null;
        }

        return trimToNull(sessionCookie.getValue());
    }

    @Override
    public Authentication anonymousLoginIfNotLoggedIn(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isValidUser(LoggedInSeller seller) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void invalidateSessionIfExpired(HttpServletRequest request, HttpServletResponse response) {
        // TODO Auto-generated method stub

    }

    @Override
    public LoggedInSeller login(String sessionId, HttpServletRequest request, HttpServletResponse response) {
    	LoggedInSeller principal = getLoggedInUserIfValid(sessionId);
        if (null == principal) {
            return null;
        }
        SecurityContextHolder.getContext().setAuthentication(authenticationFor(principal, sessionId, request));
        LOG.debug("Logged in user {} with {}", principal.getUsername(), anonymizedSessionToken(sessionId));
        return principal;
    }
    
    private LoggedInSeller getLoggedInUserIfValid(String sessionId) {
        if (StringUtils.isBlank(sessionId)) {
            return null;
        }
        AdminLoginStatus sellerLoginStatus = kmormsAuthenticationService.authenticate(sessionId);
        if (sellerLoginStatus != null && sellerLoginStatus.getOutletBunCode() != null) {
            LoggedInSellerBuilder userBuilder = new LoggedInSellerBuilder();
            LoggedInSeller user = userBuilder.brandAdministrator(false).outletAdministrator(true).enabled(true).brandId(sellerLoginStatus.getBrand())
                    .outletBunCode(sellerLoginStatus.getOutletBunCode()).username(sellerLoginStatus.getUserId()).anonymous(false).build();
            if (!isValidUser(user)) {
                LOG.error("Invalid or no user exists for the given session {}", anonymizedSessionToken(sessionId));
                return null;
            }
            return user;
        } else {
            LOG.debug("{} does not reference an existing session or has expired.", anonymizedSessionToken(sessionId));
            return null;
        }
    }
    
    private AbstractAuthenticationToken authenticationFor(LoggedInSeller principal, String sessionId, HttpServletRequest request) {
        AbstractAuthenticationToken authentication;
        if (principal.isAnonymous()) {
            authentication = new AnonymousAuthenticationToken(sessionId, principal, AuthorityUtils.createAuthorityList(ROLE_ANONYMOUS));
            authentication.setDetails(new WebAuthenticationDetails(request));
        } else {
            authentication = new TokenBasedUserAuthentication(principal, sessionId, authorities);
        }
        return authentication;
    }

}
