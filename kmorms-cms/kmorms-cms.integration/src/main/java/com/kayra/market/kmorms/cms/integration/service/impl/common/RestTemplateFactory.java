package com.kayra.market.kmorms.cms.integration.service.impl.common;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;

/**
 * Rest Template Factory.
 */
public class RestTemplateFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateFactory.class);

    private final ClientHttpRequestFactory httpRequestFactory;

    private final List<ScopedCredentials> scopedCredentials;

    public RestTemplateFactory(ClientHttpRequestFactory httpRequestFactory, List<ScopedCredentials> scopedCredentials) {
        this.httpRequestFactory = httpRequestFactory;
        this.scopedCredentials = scopedCredentials;
    }

    public RestTemplate createJsonRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(httpRequestFactory);

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

        MappingJackson2HttpMessageConverter convertor = new MappingJackson2HttpMessageConverter();
        convertor.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        messageConverters.add(convertor);
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }

    @PostConstruct
    private void init() {
        if (CollectionUtils.isNotEmpty(scopedCredentials)) {
            final DefaultHttpClient httpClient = (DefaultHttpClient) ((HttpComponentsClientHttpRequestFactory) httpRequestFactory).getHttpClient();
            final CredentialsProvider credentialsProvider = httpClient.getCredentialsProvider();

            for (ScopedCredentials creds : scopedCredentials) {
                LOGGER.info("Setting credentials: {} with {}", creds.getAuthScope(), creds.getCredentials());
                credentialsProvider.setCredentials(creds.getAuthScope(), creds.getCredentials());
            }
        }
    }

}
