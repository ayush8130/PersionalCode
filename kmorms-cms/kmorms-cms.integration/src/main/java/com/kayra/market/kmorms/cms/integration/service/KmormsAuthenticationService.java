package com.kayra.market.kmorms.cms.integration.service;

import com.kayra.market.kmorms.model.admin.AdminLoginStatus;

public interface KmormsAuthenticationService {

    AdminLoginStatus authenticate(String sessionId);

    AdminLoginStatus authenticateAdmin(String sessionId);

}
