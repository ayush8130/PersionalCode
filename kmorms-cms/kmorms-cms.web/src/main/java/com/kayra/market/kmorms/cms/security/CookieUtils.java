package com.kayra.market.kmorms.cms.security;

import static com.kayra.market.kmorms.cms.security.SecurityToken.SECURITY_TOKEN_NAME;
import static com.kayra.market.kmorms.cms.security.SecurityToken.SELLER_SECURITY_TOKEN_NAME;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for managing cookies
 */
public class CookieUtils {
    private static final Logger LOG = LoggerFactory.getLogger(CookieUtils.class);
    public static final int SESSION_COOKIE_MAX_AGE = -1;

    private CookieUtils() {
    }

    public static boolean isValidSessionCookie(Cookie cookie) {
        if (cookie == null) {
            return false;
        }
        boolean isValid = true;
        isValid = isValid && StringUtils.equalsIgnoreCase(cookie.getName(), SECURITY_TOKEN_NAME.toString());
        return isValid;
    }

    public static boolean isValidSellerSessionCookie(Cookie cookie) {
        if (cookie == null) {
            return false;
        }
        boolean isValid = true;
        isValid = isValid && StringUtils.equalsIgnoreCase(cookie.getName(), SELLER_SECURITY_TOKEN_NAME.toString());
        return isValid;
    }

}
