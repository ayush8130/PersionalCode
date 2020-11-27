/**
 * 
 */
package com.kayra.market.kmorms.cms.integration.security;

import static com.kayra.market.kmorms.cms.integration.security.LoginAnonymizerUtils.anonymizedSessionToken;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.util.Assert;

/**
 * {@link SessionRegistry} implementation that saves the session tokens to Redis Server
 */
@Getter
@Setter
public class RedisSessionRegistry implements SessionRegistry {

    private Logger logger = LoggerFactory.getLogger(RedisSessionRegistry.class);

    @Override
    @Cacheable(value = "session-registry", key = "ALL_LIST")
    public List<Object> getAllPrincipals() {
        return new ArrayList<>();
    }

    @Cacheable(value = "session-registry", key = "#principal.username", unless = "#result == null")
    protected Set<String> getSessionsUsedByPrincipal(Object principal) {
        return null;
    }

    private int lifespan = 30;

    @Override
    public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
        final Set<String> sessionsUsedByPrincipal = getSessionsUsedByPrincipal(principal);

        if (sessionsUsedByPrincipal == null) {
            return Collections.emptyList();
        }

        List<SessionInformation> list = new ArrayList<>(sessionsUsedByPrincipal.size());

        for (String sessionId : sessionsUsedByPrincipal) {
            SessionInformation sessionInformation = getSessionInformation(sessionId);

            if (sessionInformation == null) {
                continue;
            }

            if (includeExpiredSessions || !sessionInformation.isExpired()) {
                list.add(sessionInformation);
            }
        }

        return list;
    }

    @Override
    public SessionInformation getSessionInformation(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        SbusSessionInformation sbusSessionInformation = getSbusSessionInformation(sessionId);
        expireIfNeeded(sbusSessionInformation);
        return sbusSessionInformation;
    }

    private void expireIfNeeded(SessionInformation sessionInformation) {
        if ((null == sessionInformation) || (null == sessionInformation.getLastRequest())) {
            return;
        }
        final Date lastRequest = sessionInformation.getLastRequest();
        final Date sessionExpiry = DateUtils.addMinutes(lastRequest, lifespan);
        if (!sessionExpiry.after(new Date())) {
            sessionInformation.expireNow();
        }
    }

    @Cacheable(value = "session-registry", unless = "#result == null")
    private SbusSessionInformation getSbusSessionInformation(String sessionId) {
        return null;
    }

    @Override
    public void refreshLastRequest(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");

        SbusSessionInformation info = getSbusSessionInformation(sessionId);

        if (info != null) {
            info.refreshLastRequest();
            setAndGetSessionInformation(sessionId, info);
        }
    }

    @Override
    public void registerNewSession(String sessionId, Object principal) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        Assert.notNull(principal, "Principal required as per interface contract");

        if (logger.isDebugEnabled()) {
            logger.debug("Registering {}, for principal {}", anonymizedSessionToken(sessionId), principal);
        }

        if (getSessionInformation(sessionId) != null) {
            removeSessionInformation(sessionId);
        }

        setAndGetSessionInformation(principal, sessionId);
        addAndGetSessions(principal, sessionId);
    }

    private SbusSessionInformation setAndGetSessionInformation(Object principal, String sessionId) {
        return setAndGetSessionInformation(sessionId, new SbusSessionInformation(principal, sessionId, new Date()));
    }

    @CachePut(value = "session-registry", key = "#sessionId", unless = "#result == null")
    private SbusSessionInformation setAndGetSessionInformation(String sessionId, SbusSessionInformation sessionInformation) {
        return sessionInformation;
    }

    @CachePut(value = "session-registry", key = "#principal.username")
    private Set<String> addAndGetSessions(Object principal, String sessionId) {
        Set<String> principalSessions = getSessionsUsedByPrincipal(principal);
        if (null == principalSessions) {
            principalSessions = new HashSet<>(1);
        }
        principalSessions.add(sessionId);

        return principalSessions;
    }

    @CachePut(value = "session-registry", key = "#principal.username")
    private Set<String> setAndGetSessions(Set<String> sessions, Object principal) {
        return sessions;
    }

    @Override
    @CacheEvict(value = "session-registry", key = "#sessionId")
    public void removeSessionInformation(final String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");

        SessionInformation info = getSessionInformation(sessionId);

        if (info == null) {
            return;
        }

        logger.trace("Removing {} from set of registered sessions", anonymizedSessionToken(sessionId));

        Set<String> sessionsUsedByPrincipal = getSessionsUsedByPrincipal(info.getPrincipal());

        if ((null != sessionsUsedByPrincipal) && (sessionsUsedByPrincipal.contains(sessionId))) {
            removeSessionIdFrom(sessionsUsedByPrincipal, info.getPrincipal(), sessionId);
        }
    }

    private void removeSessionIdFrom(Set<String> sessionsUsedByPrincipal, Object principal, final String sessionId) {
        boolean filtered = CollectionUtils.filterInverse(sessionsUsedByPrincipal, new Predicate<String>() {
            @Override
            public boolean evaluate(String value) {
                return sessionId.equals(value);
            }
        });

        if (filtered) {
            setAndGetSessions(sessionsUsedByPrincipal, principal);
        } else {
            logger.warn("Could not remove {} from the sessions of principal", anonymizedSessionToken(sessionId));
        }
    }

    @CacheEvict(value = "session-registry", key = "#principal.username")
    private void removePrincipalFromCache(Principal principal, String sessionId, Set<String> sessionsUsedByPrincipal, SessionInformation info) {
        if (logger.isDebugEnabled()) {
            logger.debug("Removing {} from principal's set of registered sessions", anonymizedSessionToken(sessionId));
        }

        sessionsUsedByPrincipal.remove(sessionId);

        if (sessionsUsedByPrincipal.isEmpty()) {
            // No need to keep object in principals Map anymore
            if (logger.isDebugEnabled()) {
                logger.debug("Removing principal " + info.getPrincipal() + " from registry");
            }
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Sessions used by '" + info.getPrincipal() + "' : " + sessionsUsedByPrincipal);
        }
    }
}
