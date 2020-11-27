package com.kayra.market.kmorms.cms.integration.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.kayra.market.kmorms.model.common.ErrorCode;

/**
 * A {@link AbstractServiceException} that yields a service unavailable to the FE or REST end-point
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceException extends AbstractServiceException {
    private static final long serialVersionUID = 1L;

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message, Throwable cause, ErrorCode code) {
        super(message, cause, code.toString());
    }

    public ServiceException(String message) {
        super(message);
    }
}
