package com.kayra.market.kmorms.cms.integration.service.impl;

import com.kayra.market.kmorms.cms.integration.service.KmormsAuthenticationService;
import com.kayra.market.kmorms.cms.integration.service.impl.common.WebDataServiceImpl;
import com.kayra.market.kmorms.model.admin.AdminLoginStatus;
import com.kayra.market.kmorms.model.enums.SessionType;

public class KmormsAuthenticationServiceImpl implements KmormsAuthenticationService {

    private final WebDataServiceImpl webDataService;

    private final String kmormsSellerURL;

    private final String kmormsAdminURL;

    public KmormsAuthenticationServiceImpl(WebDataServiceImpl webDataService, String kmormsSellerURL, String kmormsAdminURL) {
        this.webDataService = webDataService;
        this.kmormsSellerURL = kmormsSellerURL;
        this.kmormsAdminURL = kmormsAdminURL;
    }

    public AdminLoginStatus authenticate(String sessionId) {
        return webDataService.getUsingSession(kmormsSellerURL + "/login-status", AdminLoginStatus.class, sessionId, SessionType.SELLER, new Object[] {});
    }

    public AdminLoginStatus authenticateAdmin(String sessionId) {
        return webDataService.getUsingSession(kmormsAdminURL + "/login-status", AdminLoginStatus.class, sessionId, SessionType.ADMIN, new Object[] {});
    }

}
