package com.kayra.market.kmorms.cms.integration.redis.cache;

import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import com.kayra.market.kmorms.cms.integration.redis.JedisCodec;
import com.kayra.market.kmorms.cms.integration.redis.connection.RedisPoolFactoryBean;

public abstract class JedisCache implements Cache {

    private final String prefix;

    private final String name;

    private final RedisPoolFactoryBean poolFactory;

    private final JedisCodec codec;

    private final int expiry;

    /**
     * @param expiryInSeconds is less than or equal to zero then it will never expire
     */
    public JedisCache(String prefix, String name, RedisPoolFactoryBean poolFactory, JedisCodec codec, int expiryInSeconds) {
        this.prefix = prefix;
        this.name = name;
        this.poolFactory = poolFactory;
        this.codec = codec;
        this.expiry = expiryInSeconds;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return poolFactory;
    }

    @Override
    public ValueWrapper get(Object key) {
        try (Jedis jedis = getResource()) {
            byte[] value = jedis.get(codec.encodeKey(prefix, name, key));
            if (value != null) {
                return new SimpleValueWrapper(codec.decode(value));
            }
        } catch (JedisException e) {
            LOG.warn("Unable to get entry from the cache", e);
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        try {
            Object value = get(key);
            if (value != null) {
                value = ((SimpleValueWrapper) value).get();
                if (type.isInstance(value)) {
                    return type.cast(value);
                }
            }
        } catch (ClassCastException e) {
            LOG.error("Unable to convert cached value", e);
        }

        return null;
    }

    @Override
    public void put(Object key, Object value) {
        try (Jedis jedis = getResource()) {
            byte[] encodedKey = codec.encodeKey(prefix, name, key);
            jedis.set(encodedKey, codec.encode(value));
            if (expiry > 0) {
                jedis.expire(encodedKey, expiry);
            }
        } catch (JedisException e) {
            LOG.warn("Unable to put entry into the cache", e);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        throw new NotImplementedException("Unsupported");
    }

    @Override
    public void evict(Object key) {
        try (Jedis jedis = getResource()) {
            jedis.del(codec.encodeKey(prefix, name, key));
        } catch (JedisException e) {
            LOG.warn("Unable to put evict from the cache", e);
        }
    }

    @Override
    public void clear() {
        try (Jedis jedis = getResource()) {
            Set<byte[]> keys = jedis.keys(codec.encodeKey(prefix, name, "*"));
            if (keys != null) {
                for (byte[] key : keys) {
                    jedis.del(key);
                }
            }
        } catch (JedisException e) {
            LOG.warn("Unable to clear the cache", e);
        }
    }

    private Jedis getResource() throws JedisException {
        return poolFactory.getPool().getResource();
    }

    private static final Logger LOG = LoggerFactory.getLogger(JedisCache.class);

}
