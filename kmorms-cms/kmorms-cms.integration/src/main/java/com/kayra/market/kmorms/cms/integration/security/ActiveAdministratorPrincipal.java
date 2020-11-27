package com.kayra.market.kmorms.cms.integration.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation on a controller to obtain the active {@link LoggedInAdministrator.mactec.sbus.global.model.security.LoggedInAdmnistrator} in a controller.
 *
 * <pre>
 * {@code
 * public void someControllerMethod(@ActiveUserPrincipal UserAccount account) {
 *    // account = is the logged in user
 * }
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActiveAdministratorPrincipal {
}
