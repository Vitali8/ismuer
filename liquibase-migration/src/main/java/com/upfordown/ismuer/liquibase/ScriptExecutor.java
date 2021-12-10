package com.upfordown.ismuer.liquibase;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.upfordown.ismuer.liquibase.exception.ScriptExecutionException;
import com.upfordown.ismuer.liquibase.model.CqlScript;
import org.springframework.util.StringUtils;

import java.io.Closeable;
import java.time.Duration;
import java.util.regex.Pattern;

public class ScriptExecutor implements Closeable {

    private static final Pattern STATEMENT_DIVIDER = Pattern.compile(";(?=(?:[^']*'[^']*')*[^']*$)");

    private final ChangeLogRepository changeLogRepository;
    private final CqlSession cqlSession;

    public ScriptExecutor(final ChangeLogRepository changeLogRepository, final CqlSession cqlSession) {
        this.changeLogRepository = changeLogRepository;
        this.cqlSession = cqlSession;
    }

    public void execute(final CqlScript script) {
        // Remove comments
        final String cqlWithoutComments = Utils.removeComments(script.getContent());
        // Prepare statements
        final String[] statements = STATEMENT_DIVIDER.split(cqlWithoutComments);
        for (final String cqlStatement : statements) {
            final String trimmedStatement = cqlStatement.trim();
            if (!StringUtils.hasText(cqlStatement)) {
                continue;
            }
            // We will have schema manipulations, let's increase default timeout
            final SimpleStatement statement = new SimpleStatementBuilder(trimmedStatement)
                    .setConsistencyLevel(script.getConsistencyLevel())
                    .setTimeout(Duration.ofSeconds(5))
                    .build();
            final ResultSet resultSet = cqlSession.execute(statement);
            if (!checkSchemaAgreement(resultSet)) {
                throw new ScriptExecutionException(String.format("Problems while executing script with order = '%s'. " +
                        "Schema agreement could not be reached.", script.getOrder()));
            }
        }

        changeLogRepository.insertChangeLog(script.getOrder(), script.getKeyspace(), script.getLocation(), script.getContent());
    }

    @Override
    public void close() {
        if (!cqlSession.isClosed()) {
            cqlSession.close();
        }
    }

    private boolean checkSchemaAgreement(final ResultSet resultSet) {
        // Everything is ok
        if (resultSet.getExecutionInfo().isSchemaInAgreement()) {
            return true;
        }

        // Hmm, let's wait a little bit and check by session
        int retries = 3;
        while (retries > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new ScriptExecutionException("Thread interrupted");
            }
            if (cqlSession.checkSchemaAgreement()) {
                return true;
            }
            retries--;
        }

        return false;
    }
}
