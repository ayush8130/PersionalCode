package com.kayra.market.kmorms.cms.integration.service;

/**
 * Root runtime exception for the service layer
 */
public abstract class AbstractServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String messageKey;

    private String code;

    public AbstractServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AbstractServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractServiceException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public AbstractServiceException(String message) {
        super(message);
    }

    public AbstractServiceException(String message, String messageKey, String code) {
        super(message);
        this.messageKey = messageKey;
        this.code = code;
    }

    public AbstractServiceException(Throwable cause) {
        super(cause);
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getCode() {
        return code;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
}
