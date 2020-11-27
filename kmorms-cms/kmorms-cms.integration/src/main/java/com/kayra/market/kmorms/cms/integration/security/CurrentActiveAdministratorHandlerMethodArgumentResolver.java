package com.kayra.market.kmorms.cms.integration.security;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.kayra.market.kmorms.model.security.LoggedInSeller;

/**
 * Resolves a {@link LoggedInAdministrator} when annotated with {@link ActiveAdministratorPrincipal} in a controller method.<br>
 * See {@link com.kayra.market.kmorms.model.security.LoggedInAdministrator} for example usage in controllers.<br>
 * Configure me in spring mvc via...
 *
 * <pre>
 * {@code
 * <mvc:annotation-driven>
 *   <mvc:argument-resolvers>
 *     <bean class="com.kayra.market.erp.integration.security.CurrentActiveAdmnistratorHandlerMethodArgumentResolver"/>
 *   </mvc:argument-resolvers>
 * </mvc:annotation-driven>
 * }
 * </pre>
 * 
 * @author rdubey
 */
@Component
public class CurrentActiveAdministratorHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger LOG = LoggerFactory.getLogger(CurrentActiveAdministratorHandlerMethodArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation(ActiveAdministratorPrincipal.class) != null
                && methodParameter.getParameterType().equals(LoggedInSeller.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        if (null == webRequest.getUserPrincipal()) {
            return WebArgumentResolver.UNRESOLVED;
        }
        LOG.debug("Resolving current user: " + webRequest.getUserPrincipal().getName());
        if (this.supportsParameter(methodParameter)) {
            Principal principal = webRequest.getUserPrincipal();
            return ((Authentication) principal).getPrincipal();
        } else {
            return WebArgumentResolver.UNRESOLVED;
        }
    }
}
