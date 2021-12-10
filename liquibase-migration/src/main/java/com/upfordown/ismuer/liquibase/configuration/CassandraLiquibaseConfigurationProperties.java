package com.upfordown.ismuer.liquibase.configuration;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cassandra.liquibase")
public class CassandraLiquibaseConfigurationProperties {

    private Boolean enabled = false;
    private String changeLog = "db/changelog.json";
    private ConsistencyLevel defaultConsistencyLevel = ConsistencyLevel.ALL;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public ConsistencyLevel getDefaultConsistencyLevel() {
        return defaultConsistencyLevel;
    }

    public void setDefaultConsistencyLevel(DefaultConsistencyLevel defaultConsistencyLevel) {
        this.defaultConsistencyLevel = defaultConsistencyLevel;
    }
}
