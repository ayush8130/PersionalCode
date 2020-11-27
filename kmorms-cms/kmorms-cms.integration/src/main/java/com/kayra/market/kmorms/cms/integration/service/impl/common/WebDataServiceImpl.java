package com.kayra.market.kmorms.cms.integration.service.impl.common;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.join;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.kayra.market.kmorms.cms.integration.service.ServiceUnavailableException;
import com.kayra.market.kmorms.model.enums.SessionType;

/**
 * Web Data Service Impl.
 */
public class WebDataServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDataServiceImpl.class);

    private final RestTemplate restTemplate;

    public WebDataServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T call(String url, Class<T> responseType, Object... urlVariables) throws ServiceUnavailableException {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(format("Calling '%s' with urlVariables [%s] and transforming with '%s'", url, responseType, join(urlVariables)));
        }

        // CHECKSTYLE:OFF
        T result = null;
        try {
            result = restTemplate.getForObject(url, responseType, urlVariables);
        } catch (HttpClientErrorException e) {
            LOGGER.warn("Unable to find resource", e);
        } catch (RestClientException e) {
            LOGGER.error("Unable to access API", e);
            throw new ServiceUnavailableException("Unable to access API", e);
        }
        // CHECKSTYLE:ON

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Returned ({}) with {}", url, result);
        }
        return result;
    }

    public <T, K> T getUsingSession(String url, Class<T> responseType, String sessionId, SessionType sessionType, Object... urlVariables)
            throws ServiceUnavailableException {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(format("Calling '%s' with urlVariables [%s] and transforming with '%s'", url, responseType, join(urlVariables)));
        }

        // CHECKSTYLE:OFF
        T result = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            if (sessionType.equals(SessionType.ADMIN)) {
                headers.add("Cookie", "JSESSIONID=" + sessionId);
            } else if (sessionType.equals(SessionType.SELLER)) {
                headers.add("Cookie", "XS-SEC-TOKEN=" + sessionId);
            } else if (sessionType.equals(SessionType.EMPLOYEE)) {
                headers.add("Cookie", "XE-SEC-TOKEN=" + sessionId);
            }
            HttpEntity<T> entity = new HttpEntity<>(null, headers);

            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType, urlVariables);
            result = response.getBody();
        } catch (RestClientException e) {
            LOGGER.error("Unable to access API", e);
            throw new ServiceUnavailableException("Unable to access API", e);
        }
        // CHECKSTYLE:ON

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Returned ({}) with {}", url, result);
        }
        return result;
    }

    public <T, K> T put(String url, K payload, Class<T> responseType, Object... urlVariables) throws ServiceUnavailableException {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(format("Posting '%s' with urlVariables [%s] and transforming with '%s'", url, payload, join(urlVariables)));
        }

        ResponseEntity<T> responseEntity;

        // CHECKSTYLE:OFF
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<K> request = new HttpEntity<K>(payload, requestHeaders);

            responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, responseType, urlVariables);
        } catch (Exception e) {
            LOGGER.error("Unable to access API", e);
            throw new ServiceUnavailableException("Unable to access API", e);
        }
        // CHECKSTYLE:ON

        return responseEntity.getBody();
    }

    public void delete(String url, Object... urlVariables) throws RestClientException {
        try {
            restTemplate.delete(url, urlVariables);
        } catch (RestClientException e) {
            LOGGER.error("Unable to access API", e);
            throw new ServiceUnavailableException("Unable to access API", e);
        }
    }

}
