package com.kayra.market.kmorms.cms.integration.security.web;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.Assert;

/**
 * Basic utility for encrypting and checking passwords
 */
public final class Encryptor {

    /**
     * Hash a {@link java.lang.String}
     * 
     * @param value the value to hash
     * @return the hashed value
     */
    public static String encrypt(final String value) {
        Assert.hasText(value, "Value to be encrypted should not be empty");
        return BCrypt.hashpw(value, BCrypt.gensalt(12));
    }

    /**
     * Check that a plaintext password value a previously hashed one
     * 
     * @param value the plaintext value to verify
     * @param hashed the previously-hashed value
     * @return true if the values match, false otherwise
     */
    public static boolean check(final String value, final String hashed) {
        Assert.hasText(value, "Value to be checked should not be empty");
        Assert.hasText(hashed, "Value to check against should not be empty");
        return BCrypt.checkpw(value, hashed);
    }
}
