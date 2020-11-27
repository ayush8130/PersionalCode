package com.kayra.market.kmorms.cms.integration.spring.security;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A web application returns a response marked as cache-able but also returns a <i>Set-Cookie</i> header to store a user-specific cookie. Some proxies cache the
 * cookie along with the response, and return both in subsequent requests. This can lead to a user receiving cookies destined to another user. If the cookie
 * contains authentication information, this could lead to a complete account compromise.<br>
 *
 * This HttpServletResponseWrapper wraps a {@link HttpServletResponse} and throws a {@link RuntimeException} if a "Cache-Control: public" header is set while a
 * cookie has been added and vice versa. This is a preventive/defensive programming measure.
 *
 * @author rajeev dubey
 */
public class CookieLeakViaCacheResponseWrapper extends HttpServletResponseWrapper {
    private boolean hasCookies = false;
    private List<StackTraceElement[]> addCookieStack = new ArrayList<>();
    private boolean hasPubCache = false;
    private List<StackTraceElement[]> cachePubStack = new ArrayList<>();
    private String context;
    private boolean simulate = false;

    /**
     * @param response the {@link HttpServletResponse} to wrap
     * @param context the context of the call (i.e. the URI) that is logged
     * @param simulate if false, this wrapper will throw {@link RuntimeException}s when it detects an error
     *            otherwise it will just log error messages
     */
    public CookieLeakViaCacheResponseWrapper(HttpServletResponse response, String context, boolean simulate) {
        super(response);
        this.context = context;
        this.simulate = simulate;
    }

    @Override
    public void setHeader(String name, String value) {
        if (equalsIgnoreCase(name, "Cache-Control")) {
            if (contains(value, "public")) {
                recordCacheControl();
                if (hasCookies) {
                    String msg = format("Can't set Cache-Control header to '%s' when cookies have been set in response. "
                            + "This may cause private cookies to be exposed through a cache leak! URL=%s %s", value, context,
                            getCause("addCookie called at:", addCookieStack));
                    logError(msg);
                }
            }
        }
        super.setHeader(name, value);
    }

    private void logError(String msg) {
        LOG.error(msg + getCause("Detected at:", Collections.singleton(Thread.currentThread().getStackTrace())));
        if (!this.simulate) {
            throw new RuntimeException(msg);
        }
    }

    private void recordCacheControl() {
        this.hasPubCache = true;
        if (traceEnabled()) {
            cachePubStack.add(Thread.currentThread().getStackTrace());
        }
    }

    @Override
    public void addCookie(Cookie cookie) {
        recordAddCookie();
        if (this.hasPubCache) {
            String msg = format("Can't add a cookie '%s' when a public Cache-Control header has been set! "
                    + "This may cause the private cookie to be exposed through a cache leak! URL=%s\n%s:", cookie.getName(), context,
                    getCause("Cache-Control header set at:", cachePubStack));
            logError(msg);
        }
        super.addCookie(cookie);
    }

    private String getCause(String cause, Collection<StackTraceElement[]> traces) {
        if (!traces.isEmpty()) {
            return printStackTraces(cause, traces);
        }
        return StringUtils.EMPTY;
    }

    private String printStackTraces(String cause, Collection<StackTraceElement[]> traces) {
        StringBuilder sb = new StringBuilder(cause).append("\n");
        for (Iterator<StackTraceElement[]> tracesIter = traces.iterator(); tracesIter.hasNext();) {
            StackTraceElement[] trace = tracesIter.next();
            for (int i = 3; i < trace.length; i++) {
                sb.append("\tat ").append(trace[i]).append("\n");
            }
            if (tracesIter.hasNext()) {
                sb.append("and also at:\n");
            }
        }
        return sb.toString();
    }

    private void recordAddCookie() {
        this.hasCookies = true;
        if (traceEnabled()) {
            addCookieStack.add(Thread.currentThread().getStackTrace());
        }
    }

    private boolean traceEnabled() {
        return LOG.isTraceEnabled();
    }

    private static final Logger LOG = LoggerFactory.getLogger(CookieLeakViaCacheResponseWrapper.class);
}
