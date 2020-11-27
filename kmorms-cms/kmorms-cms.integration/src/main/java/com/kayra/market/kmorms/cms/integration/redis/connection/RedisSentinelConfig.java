package com.kayra.market.kmorms.cms.integration.redis.connection;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.split;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis sentinel (cluster) configuration. You configure a cluster of sentinels that allow high availability and failover of redis servers.
 *
 * @see RedisConfig for a non-sentinel based configuration
 * @see JedisPoolConfig for configuring the connection pooling
 */
@Getter
@Setter
@ToString
public class RedisSentinelConfig {

    private String master;

    /**
     * List of sentinel hosts with ports ("georgia:26379", "elahna:26379")
     */
    private Set<String> sentinels;

    private int timeout = 5000;

    private String password = null;

    private int database = 0;

    public void setSentinels(Set<String> sentinels) {
        if (isEmpty(sentinels)) {
            throw new NullPointerException("sentinels must be a comma delimied host:port pairs");
        }
        this.sentinels = sentinels;
    }

    /**
     * @param commaDelimitedHostPorts eg "georgia:26379,elahna:26379"
     */
    public void setSentinelsFromString(String commaDelimitedHostPorts) {
        String[] list = split(commaDelimitedHostPorts, ",");
        if (list == null || list.length <= 0) {
            throw new IllegalArgumentException("sentinels must be a comma delimied host:port pairs");
        }
        setSentinels(new HashSet<String>(Arrays.asList(list)));
    }

    public void validate() {
        Objects.requireNonNull(master, "master is required");
        if (isEmpty(this.sentinels)) {
            throw new NullPointerException("sentinels must be a comma delimied host:port pairs");
        }
    }

}
