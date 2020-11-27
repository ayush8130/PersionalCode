package com.kayra.market.kmorms.cms.web.common;

import org.springframework.context.MessageSource;

import com.kayra.market.kmorms.cms.integration.service.AbstractServiceException;
import com.kayra.market.kmorms.cms.integration.service.ServiceException;

/**
 * Provides messages returned by the CMS api
 */
public class CMSApiMessageFacade extends AbstractMessageFacade {

    public CMSApiMessageFacade(MessageSource messageSource) {
        super(messageSource);
    }

    public String get(AbstractServiceException ex) {
        return get(ex.getMessageKey(), ex.getMessage());
    }

    public String get(ServiceException ex, Object[] substitutions) {
        return get(ex.getMessageKey(), ex.getMessage(), substitutions);
    }

    protected final String get(String key, String defaultValue) {
        return super.get(key, defaultValue);
    }

    protected final String get(String key, String defaultValue, Object[] substitutions) {
        return super.get(key, defaultValue, substitutions);
    }
}
