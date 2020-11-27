package com.kayra.market.kmorms.cms.integration.security.web.common;

import org.springframework.http.MediaType;

/**
 * Constants used in controllers
 */
public final class ViewConstants {
    private ViewConstants() {
    }

    public static final String PUB_OUTPUT = MediaType.APPLICATION_JSON_VALUE;
    public static final String PUB_INPUT = MediaType.APPLICATION_JSON_VALUE;

    public static final String SUPER_ADMINISTRATORS_SEGMENT = "/super";
    public static final String ORGANIZATION_ADMINISTRATORS_SEGMENT = "/organization";
    public static final String CHANNEL_PARTNER_ADMINISTRATORS_SEGMENT = "/channelpartner";

    public static final String ID_PARAM = "id";
    public static final String ORGANIZATION_ID_PARAM = "organizationId";
    public static final String CODE_PARAM = "code";

    public static final String PUB_ORGANIZATIONS = "/pub/organizations";
    public static final String PUB_ORGANIZATION_LEVEL_CARD_DETAILS = "/{organizationId}/mydetails/carddetails";
    public static final String PUB_CHANNEL_PARTNER_LEVEL_CARD_DETAILS = "/{organizationId}/channelpartners/{code}/mydetails/carddetails";
    public static final String PUB_CHANNEL_PARTNER_LEVEL_SUBSCRIBE = "/{organizationId}/channelpartners/{code}/subscribe";
    public static final String PUB_ORGANIZATION_LEVEL_RESERVATIONS = "/{organizationId}/mydetails/reservations";
    public static final String PUB_CHANNEL_PARTNER_LEVEL_RESERVATIONS = "/{organizationId}/channelpartners/{code}/mydetails/reservations";
    public static final String PUB_ORGANIZATION_LEVEL_RESET_PASSWORD = "/{organizationId}/reset/password";
    public static final String PUB_CHANNEL_PARTNER_LEVEL_RESET_PASSWORD = "/{organizationId}/channelpartners/{code}/reset/password";
    public static final String PUB_ORGANIZATION_LEVEL_RESET_TOKEN = "/{organizationId}/reset/token";
    public static final String PUB_CHANNEL_PARTNER_LEVEL_RESET_TOKEN = "/{organizationId}/channelpartners/{code}/reset/token";
    public static final String PUB_ORGANIZATION_LEVEL_MY_DETAILS = "/{organizationId}/mydetails";
    public static final String PUB_CHANNEL_PARTNER_LEVEL_MY_DETAILS = "/{organizationId}/channelpartners/{code}/mydetails";
    public static final String PUB_CHANNEL_PARTNER_LEVEL_ORDER_DETAILS_CONSUMER_TAX_RETURN = "/{organizationId}/channelpartners/{code}/order-details/{orderId}/consumer/taxreturn";
    public static final String PUB_ORGANIZATION_LEVEL_REGISTER = "/{organizationId}/register";
    public static final String PUB_CHANNEL_PARTNER_LEVEL_REGISTER = "/{organizationId}/channelpartners/{code}/register";
    public static final String PUB_CHANNEL_PARTNER_LEVEL_REGISTER_VALIDATE = "/{organizationId}/channelpartners/{code}/otp-validate";
    public static final String PUB_ORGANIZATION_LEVEL_AUTHENTICATED = "/{organizationId}/authenticated";
    public static final String PUB_ORGANIZATION_OR_MARKET_LEVEL_AUTHENTICATED = "/{id}/authenticated";
    public static final String PUB_CHANNEL_PARTNER_LEVEL_AUTHENTICATED = "/{organizationId}/channelpartners/{code}/authenticated";
    public static final String PUB_ORGANIZATION_LEVEL_LOGIN = "/{organizationId}/login";
    public static final String PUB_CHANNEL_PARTNER_LEVEL_LOGIN = "/{organizationId}/channelpartners/{code}/login";
    public static final String PUB_ANONYMOUS_LOGIN = "/login/anonymous";
    public static final String PUB_LOGOUT = "logout";
    public static final String PUB_CHANNEL_PARTNER_LEVEL_UNSUBSCRIBE = "/{organizationId}/channelpartners/{code}/unsubscribe";

    public static final String EMPLOYER_ANONYMOUS_LOGIN = "/login/anonymous";
    public static final String EMPLOYER_LOGOUT = "logout";
    public static final String EMPLOYER_LOGIN = "login";

}
