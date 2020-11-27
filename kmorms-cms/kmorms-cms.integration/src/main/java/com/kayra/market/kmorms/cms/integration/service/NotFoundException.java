package com.kayra.market.kmorms.cms.integration.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A {@link AbstractServiceException} that yields a 404 to the FE or REST end-point
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends AbstractServiceException {
    private static final long serialVersionUID = 1L;

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message) {
        super(message);
    }
}
