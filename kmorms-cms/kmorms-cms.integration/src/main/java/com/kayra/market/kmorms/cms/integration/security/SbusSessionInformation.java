package com.kayra.market.kmorms.cms.integration.security;

import java.security.Principal;
import java.util.Date;

import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.session.SessionInformation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Encapsulates {@link SessionInformation} to work with JSON Serialisation/deserialisation
 */
public class SbusSessionInformation extends SessionInformation {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    /**
     * Constructor with fields.
     * 
     * @param principal the {@link Principal}
     * @param sessionId the session id
     * @param lastRequest the date of last request
     */
    @JsonCreator
    public SbusSessionInformation(@JsonProperty("principal") Object principal, @JsonProperty("sessionId") String sessionId,
            @JsonProperty("lastRequest") Date lastRequest) {
        super(principal, sessionId, lastRequest);
    }

}
