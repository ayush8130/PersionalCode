package com.kayra.market.kmorms.cms.integration.redis.cache;

import java.io.IOException;
import java.nio.charset.Charset;

import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTypeResolverBuilder;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.kayra.market.kmorms.cms.integration.redis.JedisCodec;

@Setter
public class JedisJacksonCodec implements JedisCodec {

    private ObjectMapper mapper;

    public JedisJacksonCodec() {
        mapper = init(new ObjectMapper());
    }

    protected ObjectMapper init(ObjectMapper mapper) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        TypeResolverBuilder<?> typer = new DefaultTypeResolverBuilder(DefaultTyping.NON_FINAL);
        typer.init(JsonTypeInfo.Id.CLASS, null);
        typer.inclusion(JsonTypeInfo.As.PROPERTY);
        mapper.setDefaultTyping(typer);

        mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE).withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

        return mapper;
    }

    @Override
    public byte[] encodeKey(String prefix, String name, Object key) {
        return String.format("%s:%s:%s", prefix, name, key.toString()).getBytes(Charset.forName("ASCII"));
    }

    @Override
    public byte[] encode(Object value) {
        try {
            JedisHibernateProxyUtil.cleanBean(value);
            return mapper.writeValueAsBytes(value);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to encode", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to clean hibernate proxies", e);
        }
    }

    @Override
    public Object decode(byte[] value) {
        try {
            return mapper.readValue(value, Object.class);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to decode", e);
        }
    }

}
