package com.kayra.market.kmorms.cms.integration.service.util;

import static org.apache.commons.lang3.StringUtils.substring;

/**
 * Helper class for anonymizer utility methods
 */
public class AnonymizerUtils {
    public static final int LAST_DIGITS_TO_SHOW = 4;

    protected AnonymizerUtils() {
    }

    public static String anonymized(String value) {
        return substring(value, -1 * LAST_DIGITS_TO_SHOW);
    }
}
