package com.kayra.market.kmorms.cms.integration.security.web;

import static com.kayra.market.kmorms.cms.integration.security.web.ControllerUtils.responseEntityFor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.kayra.market.kmorms.model.security.Administrator;
import com.kayra.market.kmorms.model.security.LoggedInSeller;

@Component
public class ControllerSecurityUtils {
    private static final String FAILED_MSG_KEY = "failed";
    public static final String INVALID_EMAIL_PASSWORD = "E-mail Address, and/or password incorrect";

    /**
     * Logs in the user with the supplied username / password.
     *
     * @param administrator
     *            the user to login
     * @param password
     *            the password of the user logging in
     * @param servletResponse
     *            the {@link javax.servlet.http.HttpServletResponse}
     * @return
     */
    public ResponseEntity<Object> login(Administrator administrator, String password, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        if ((null != administrator) && (correctPasswordWasGivenFor(administrator, password))) {
            return null;
        }
        return responseEntityFor(FAILED_MSG_KEY, INVALID_EMAIL_PASSWORD, HttpStatus.UNAUTHORIZED);
    }

    private boolean correctPasswordWasGivenFor(Administrator administrator, String password) {
        return Encryptor.check(password, administrator.getPassword());
    }

    public ResponseEntity<Object> responseFor(LoggedInSeller employee) {
        if (null == employee) {
            return responseEntityFor(FAILED_MSG_KEY, INVALID_EMAIL_PASSWORD, HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<Object>(employee, HttpStatus.OK);
        }
    }

}
