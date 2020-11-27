package com.kayra.market.kmorms.cms.integration.redis.connection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

public class RedisPoolFactoryBean {

    private JedisPoolConfig poolConfig;

    private RedisSentinelConfig sentinelConfig;

    private RedisConfig redisConfig;

    private volatile Pool<Jedis> pool;

    /**
     * Basic redis non-clustered configuration
     */
    public void setRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    /**
     * A clustered redis configuration
     */
    public void setSentinelConfig(RedisSentinelConfig sentinelConfig) {
        this.sentinelConfig = sentinelConfig;
    }

    public void setPoolConfig(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    @PostConstruct
    void init() {
        try {
            this.pool = createPool();
        } catch (JedisException e) {
            LOG.warn("Failed to connect to redis: {}", e.getMessage(), e);
        }
    }

    public synchronized Pool<Jedis> getPool() {
        if (this.pool == null) {
            this.pool = createPool();
        }
        if (this.pool.isClosed()) {
            throw new IllegalArgumentException("Pool has already been destroyed!");
        }
        return this.pool;
    }

    private Pool<Jedis> createPool() {
        if (this.sentinelConfig != null) {
            this.pool = createRedisSentinelPool(this.sentinelConfig);
            return this.pool;
        }
        if (this.redisConfig != null) {
            this.pool = createRedisPool(this.redisConfig);
            return this.pool;
        }
        throw new NullPointerException("Either sentinelConfig or redisConfig must be provided");
    }

    @PreDestroy
    void destroy() {
        if (this.pool != null) {
            this.pool.destroy();
        }
    }

    private JedisSentinelPool createRedisSentinelPool(RedisSentinelConfig config) {
        config.validate();
        return new JedisSentinelPool(config.getMaster(), config.getSentinels(), poolConfig(), config.getTimeout(), config.getPassword(), config.getDatabase());
    }

    private Pool<Jedis> createRedisPool(RedisConfig config) {
        return new JedisPool(poolConfig(), config.getHost(), config.getPort(), config.getTimeout(), config.getPassword(), config.getDatabase());
    }

    private GenericObjectPoolConfig poolConfig() {
        return this.poolConfig == null ? new JedisPoolConfig() : this.poolConfig;
    }

    private static final Logger LOG = LoggerFactory.getLogger(RedisPoolFactoryBean.class);
}
