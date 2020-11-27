package com.kayra.market.kmorms.cms.integration.security.csrf;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Custom matcher used to configure the calls to be filtered
 */
public class CustomRequestMatcher implements RequestMatcher {
    private static final Logger LOG = LoggerFactory.getLogger(CustomRequestMatcher.class);
    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    private Map<String, String> allowedPaths;
    private Map<String, String> allowedPathPatterns;

    public CustomRequestMatcher(Map<String, String> allowedPaths, Map<String, String> allowedPathPatterns) {
        this.allowedPaths = allowedPaths;
        this.allowedPathPatterns = allowedPathPatterns;
    }

    public CustomRequestMatcher(Map<String, String> allowedPaths, Map<String, String> allowedPathPatterns, String allowedMethods) {
        this.allowedPaths = allowedPaths;
        this.allowedPathPatterns = allowedPathPatterns;
        this.allowedMethods = Pattern.compile("^(" + allowedMethods + ")$");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.web.util.matcher.RequestMatcher#matches(javax.servlet.http.HttpServletRequest)
     * 
     * @see https://github.com/spring-projects/spring-boot/issues/179
     */

    /**
     * Returns whether the request matches for Filtering
     * 
     * @param request the request to check
     * @return true if the request should be filtered, otherwise false
     */
    @Override
    public boolean matches(HttpServletRequest request) {
        LOG.debug("Matching url: " + request.getServletPath());

        return !(isAllowedMethod(request) || isAllowedPath(request));
    }

    private boolean isAllowedPath(HttpServletRequest request) {
        return (matches(allowedPaths, request)) || (matches(allowedPathPatterns, request));
    }

    private boolean isAllowedMethod(HttpServletRequest request) {
        return allowedMethods.matcher(request.getMethod()).matches();
    }

    private boolean matches(Map<String, String> paths, HttpServletRequest request) {
        for (String path : paths.keySet()) {
            String allowedMethodsForThisPath = paths.get(path);
            if (StringUtils.isNotBlank(allowedMethodsForThisPath)) {
                Pattern allowedMethodsForThisPathPattern = Pattern.compile("^(" + allowedMethodsForThisPath + ")$");
                if ((allowedMethodsForThisPathPattern.matcher(request.getMethod()).matches()) && (new RegexRequestMatcher(path, null).matches(request))) {
                    return true;
                }
            } else if (new RegexRequestMatcher(path, null).matches(request)) {
                return true;
            }
        }
        return false;
    }
}
