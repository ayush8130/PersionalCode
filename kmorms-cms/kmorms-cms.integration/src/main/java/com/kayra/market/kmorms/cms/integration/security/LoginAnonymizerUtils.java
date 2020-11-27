package com.kayra.market.kmorms.cms.integration.security;

import com.kayra.market.kmorms.cms.integration.service.util.AnonymizerUtils;

/**
 * Helper class for anonymizer utility methods for login functionality
 */
public class LoginAnonymizerUtils extends AnonymizerUtils {
    private LoginAnonymizerUtils() {
    }

    public static String anonymizedSessionToken(String value) {
        return String.format("session token ...%s", anonymized(value));
    }

    public static String anonymizedCsrfToken(String value) {
        return String.format("csrf token ...%s", anonymized(value));
    }
}
