package com.kayra.market.kmorms.cms.integration.security;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Custom {@link javax.servlet.http.HttpServletResponseWrapper} class that provides records the cookies added
 */
public class SecureResponseWrapper extends HttpServletResponseWrapper {

    private final Set<String> addedCookies;

    public SecureResponseWrapper(HttpServletResponse response) {
        super(response);
        this.addedCookies = new HashSet<>();
    }

    @Override
    public void addCookie(Cookie cookie) {
        if (!addedCookies.contains(stringFrom(cookie))) {
            addedCookies.add(stringFrom(cookie));
            super.addCookie(cookie);
        }
    }

    private String stringFrom(Cookie cookie) {
        StringBuffer sb = new StringBuffer();
        sb.append(cookie.getName());
        sb.append(cookie.getValue());
        sb.append(cookie.getMaxAge());
        sb.append(cookie.getPath());
        sb.append(cookie.getDomain());
        sb.append(cookie.getSecure());
        sb.append(cookie.isHttpOnly());
        sb.append(cookie.getVersion());
        return sb.toString();
    }

}
