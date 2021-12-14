package com.upfordown.ismuer.core.configuration;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.upfordown.ismuer.core.configuration.codec.TimeUuidToDateTimeCodec;
import com.upfordown.ismuer.core.configuration.codec.TimestampToDateTimeCodec;
import com.upfordown.ismuer.liquibase.configuration.CassandraLiquibaseAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories
@Import(CassandraLiquibaseAutoConfiguration.class)
public class CassandraConfiguration {

    @Bean
    @Primary
    public CqlSession cqlSession(CqlSessionBuilder builder) {
        return builder.addTypeCodecs(
                new TimestampToDateTimeCodec(),
                new TimeUuidToDateTimeCodec()
        ).build();
    }
}
