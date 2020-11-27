package com.kayra.market.kmorms.cms.web.common;

import org.slf4j.Logger;
import org.springframework.web.context.request.WebRequest;

import com.kayra.market.kmorms.model.common.RestErrorResponse;
import com.kayra.market.kmorms.cms.integration.service.ServiceException;

/**
 * Used to create user transaction (order) error responses
 */
public class UserTransactionErrorFactory {

    public static final String API_ORDER = "api.order";
    public static final String INVALID_EMAIL = "INVALID_EMAIL";

    private CMSApiMessageFacade messageFacade;

    public UserTransactionErrorFactory(CMSApiMessageFacade messageFacade) {
        this.messageFacade = messageFacade;
    }

    public RestErrorResponse createOrderErrorsWithChannelPartnerNumber(ServiceException ex, WebRequest webRequest, Object[] substitutions, Logger log) {
        return createErrorsWithChannelPartnerNumber(ex, webRequest, UserTransactionErrorFactory.API_ORDER, substitutions, log);
    }

    private RestErrorResponse createErrorsWithChannelPartnerNumber(ServiceException ex, WebRequest webRequest, String apiCode, Object[] substitutions,
            Logger log) {
        String errorCode = (null == ex.getCode()) ? "GENERIC" : ex.getCode().toString();
        String messageKey = apiCode + "." + errorCode;
        if (ex.getMessage() != null && ex.getMessage().equals("Email address is invalid")) {
            messageKey = apiCode + "." + INVALID_EMAIL;
        }
        ex.setMessageKey(messageKey);
        log(ex, webRequest, messageKey, log);
        String message = messageFacade.get(ex, substitutions);
        return new RestErrorResponse(errorCode, message);
    }

    private static void log(Throwable ex, WebRequest webRequest, String code, Logger log) {
        log.warn("Error Code: {}: {} - request=[{}]", code, ex.getMessage(), webRequest.getDescription(true), ex);
    }
}
