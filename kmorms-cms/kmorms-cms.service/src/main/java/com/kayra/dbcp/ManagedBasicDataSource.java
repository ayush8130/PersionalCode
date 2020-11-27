package com.kayra.dbcp;

import static org.apache.commons.lang3.StringUtils.substringAfterLast;
import static org.apache.commons.lang3.StringUtils.substringBefore;
import static org.springframework.jmx.support.MetricType.COUNTER;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedMetric;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.naming.SelfNaming;

/**
 * A managed {@link BasicDataSource} that exposes basic statistics about the underlying DBCP connection pool.
 */
@ManagedResource
public class ManagedBasicDataSource extends BasicDataSource implements SelfNaming {

    @ManagedMetric(category = "utilisation", displayName = "Num Active Connections", description = "The current number of active connections that have been allocated from this data source.", metricType = COUNTER, unit = "connections")
    public int getNumActive() {
        return super.getNumActive();
    }

    @ManagedMetric(category = "utilisation", displayName = "Num Idle Connections", description = "The current number of idle connections that are waiting to be allocated from this data source.", metricType = COUNTER, unit = "connections")
    public int getNumIdle() {
        return super.getNumIdle();
    }

    @ManagedAttribute(description = "Maximum number of active connections that can be allocated.")
    public int getMaxActive() {
        return super.getMaxActive();
    }

    @ManagedAttribute(description = "Maximum number of connections that can remain idle in the pool.")
    public int getMaxIdle() {
        return super.getMaxIdle();
    }

    @ManagedAttribute(description = "The maximum number of open statements that can be allocated from the statement pool (non-positive for no limit)")
    public int getMaxOpenPreparedStatements() {
        return super.getMaxOpenPreparedStatements();
    }

    @ManagedAttribute(description = "The number of connections created when the pool is initialized")
    public int getInitialSize() {
        return super.getInitialSize();
    }

    @Override
    public ObjectName getObjectName() throws MalformedObjectNameException {
        final String db = substringAfterLast(substringBefore(getUrl(), "?"), "/");
        return new ObjectName(BasicDataSource.class.getPackage().getName(), "dbConnectionPool", db);
    }
}
