package com.kayra.market.kmorms.cms.integration.redis;

public interface JedisCodec {

    byte[] encodeKey(String prefix, String name, Object key);

    byte[] encode(Object value);

    Object decode(byte[] value);

}
