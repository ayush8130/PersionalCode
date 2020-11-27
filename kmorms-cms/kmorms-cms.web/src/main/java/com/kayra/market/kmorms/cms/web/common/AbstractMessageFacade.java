package com.kayra.market.kmorms.cms.web.common;

import java.util.Locale;

import org.springframework.context.MessageSource;

/**
 * Abstract message facade class to be used by message facade classes
 */
public abstract class AbstractMessageFacade {
    private static final Locale LOCALE = Locale.getDefault();

    protected MessageSource messageSource;

    AbstractMessageFacade() {
    }

    public AbstractMessageFacade(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    protected String get(String key, String defaultValue) {
        return messageSource.getMessage(key, null, defaultValue, LOCALE);
    }

    protected String get(String key, String defaultValue, Object[] substitutions) {
        return messageSource.getMessage(key, substitutions, defaultValue, LOCALE);
    }
}
