package com.kayra.market.kmorms.cms.security;

/**
 * Enum for defining names of security cookies
 */
public enum SecurityToken {
    
    /**
     * The name of the cookie used to hold the security token that identifies a user.
     */
    SECURITY_TOKEN_NAME("JSESSIONID"),

    /**
     * The name of the cookie used to hold the security token that identifies a admin/seller.
     */
    SELLER_SECURITY_TOKEN_NAME("XS-SEC-TOKEN"),
    
    DEFAULT_CSRF_HEADER_NAME("X-CSRF-TOKEN"),

    /**
     * Parameter name of the CSRF provided value.
     */
    DEFAULT_CSRF_PARAMETER_NAME("_csrf");

    @Override
    public String toString() {
        return value;
    }

    private final String value;

    private SecurityToken(String value) {
        this.value = value;
    }
}
