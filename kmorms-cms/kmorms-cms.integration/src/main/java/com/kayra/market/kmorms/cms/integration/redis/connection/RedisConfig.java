package com.kayra.market.kmorms.cms.integration.redis.connection;

import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis configuration.
 *
 * @see RedisSentinelConfig for sentinel type configuration
 * @see JedisPoolConfig for configuring the connection pooling
 */
public class RedisConfig {

    private String host;

    private int port = 6380;

    private int timeout = 5000;

    private String password = null;

    private int database = 0;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

}
