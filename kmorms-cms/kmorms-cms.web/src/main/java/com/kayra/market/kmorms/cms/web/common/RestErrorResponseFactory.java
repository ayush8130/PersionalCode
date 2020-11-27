package com.kayra.market.kmorms.cms.web.common;

import com.kayra.market.kmorms.model.common.RestErrorResponse;
import com.kayra.market.kmorms.cms.integration.service.AbstractServiceException;

/**
 * Generates error responses for given exceptions
 */
public class RestErrorResponseFactory {
    private CMSApiMessageFacade messageFacade;

    public RestErrorResponseFactory(CMSApiMessageFacade messageFacade) {
        this.messageFacade = messageFacade;
    }

    public RestErrorResponse from(AbstractServiceException ex) {
        String message = messageFacade.get(ex);
        String code = ex.getCode();
        return new RestErrorResponse(code, message);
    }

    public RestErrorResponse from(RuntimeException ex) {
        return new RestErrorResponse("OTHER", ex.getMessage());
    }
}
