package com.upfordown.ismuer.liquibase.configuration;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.upfordown.ismuer.liquibase.CassandraLiquibaseV2;
import com.upfordown.ismuer.liquibase.ChangeLogRepository;
import com.upfordown.ismuer.liquibase.ScriptRepository;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CassandraLiquibaseConfigurationProperties.class)
@AutoConfigureAfter(CassandraAutoConfiguration.class)
@ConditionalOnBean(CqlSession.class)
@ConditionalOnProperty(prefix = "cassandra.liquibase", name = "enabled", havingValue = "true")
public class CassandraLiquibaseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ScriptRepository.class)
    public ScriptRepository scriptRepository(final CassandraLiquibaseConfigurationProperties properties) {
        return new ScriptRepository(properties.getChangeLog());
    }

    @Bean
    @ConditionalOnMissingBean(ChangeLogRepository.class)
    public ChangeLogRepository changeLogRepository(final CqlSession cqlSession,
                                                   final CassandraLiquibaseConfigurationProperties properties) {
        return new ChangeLogRepository(cqlSession, properties.getDefaultConsistencyLevel());
    }

    @Bean
    @ConditionalOnMissingBean(CassandraLiquibaseV2.class)
    public CassandraLiquibaseV2 cassandraLiquibase(final ScriptRepository scriptRepository,
                                                   final ChangeLogRepository changeLogRepository,
                                                   final CqlSessionBuilder cqlSessionBuilder,
                                                   final CassandraProperties cassandraProperties) {
        return new CassandraLiquibaseV2(scriptRepository, changeLogRepository, cqlSessionBuilder,
                cassandraProperties.getKeyspaceName());
    }
}
