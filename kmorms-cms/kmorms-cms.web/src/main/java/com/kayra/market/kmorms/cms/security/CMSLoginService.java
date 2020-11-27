package com.kayra.market.kmorms.cms.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import com.kayra.market.kmorms.model.security.LoggedInSeller;

/**
 * @author RajaJava
 *
 */

public interface CMSLoginService {

    /**
     * Logout/kill off a specific user session
     *
     * @param request the request containing the cookies to be deleted
     * @param response used to delete any security session cookies etc.
     */
    void logout(HttpServletRequest request, HttpServletResponse response);

    String getSessionIdIfValid(HttpServletRequest request);

    /**
     * Retrieve the session ID from a request.
     *
     * @param request the request to obtain the cookies from.
     * @return null if the request does not contain the {@link SecurityToken#SECURITY_TOKEN_NAME} cookie or the data can't be parsed, otherwise returns
     *         the sessionID.
     */
    String getSessionIdFrom(HttpServletRequest request);

    /**
     * Login as an anonymous consumer.
     *
     * @param response the response that is modified (cookies added)
     *
     * @return the principal that is identifies the user in Spring Security
     */
    Authentication anonymousLoginIfNotLoggedIn(HttpServletRequest httpServletRequest, HttpServletResponse response);

    /**
     * Returns the {@link Authentication} linked to the supplied login request.
     *
     * @param request the request containing the required sessionId
     * @return {@link Authentication} linked to the supplied request
     */
    Authentication getAuthentication(HttpServletRequest request);

    /**
     * returns whether a {@link LoggedInSeller} is a valid enabled consumer.
     * 
     * @param consumer
     * @return
     */
    boolean isValidUser(LoggedInSeller seller);

    void invalidateSessionIfExpired(HttpServletRequest request, HttpServletResponse response);

    LoggedInSeller login(String sessionId, HttpServletRequest request, HttpServletResponse response);
}
