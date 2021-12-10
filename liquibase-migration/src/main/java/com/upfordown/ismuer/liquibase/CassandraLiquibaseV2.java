package com.upfordown.ismuer.liquibase;

import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.upfordown.ismuer.liquibase.exception.ScriptExecutionException;
import com.upfordown.ismuer.liquibase.model.ChangeLog;
import com.upfordown.ismuer.liquibase.model.CqlScript;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;
import java.util.stream.Collectors;

public class CassandraLiquibaseV2 implements InitializingBean {

    private final String id = UUID.randomUUID().toString();
    private final ScriptRepository scriptRepository;
    private final ChangeLogRepository changeLogRepository;
    private final CqlSessionBuilder cqlSessionBuilder;
    private final String defaultKeyspace;

    public CassandraLiquibaseV2(final ScriptRepository scriptRepository, final ChangeLogRepository changeLogRepository,
                                final CqlSessionBuilder cqlSessionBuilder, final String defaultKeyspace) {
        this.scriptRepository = scriptRepository;
        this.changeLogRepository = changeLogRepository;
        this.cqlSessionBuilder = cqlSessionBuilder;
        this.defaultKeyspace = defaultKeyspace;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final Map<String, ScriptExecutor> executors = new HashMap<>();
        try {
            changeLogRepository.checkServiceTableCreation(defaultKeyspace);
            changeLogRepository.lock(id);

            final List<CqlScript> scripts = scriptRepository.getScripts(defaultKeyspace);
            checkAndMarkExecuted(scripts, changeLogRepository.getChangeLogs());

            final List<CqlScript> toExecute = scripts.stream()
                    .filter(script -> !script.isExecuted())
                    .collect(Collectors.toList());

            executors.putAll(toExecute.stream()
                    .map(CqlScript::getKeyspace)
                    .distinct()
                    .collect(Collectors.toMap(keyspace -> keyspace, keyspace -> new ScriptExecutor(changeLogRepository,
                            cqlSessionBuilder.withKeyspace(keyspace).build()))));

            toExecute.stream()
                    .sorted(Comparator.comparingInt(CqlScript::getOrder))
                    .forEachOrdered(script -> executors.get(script.getKeyspace()).execute(script));
        } finally {
            // Revert builder keyspace (this is spring's builder)
            cqlSessionBuilder.withKeyspace(defaultKeyspace);

            executors.values().forEach(ScriptExecutor::close);
            changeLogRepository.releaseLock(id);
        }
    }

    private void checkAndMarkExecuted(final List<CqlScript> scripts, final List<ChangeLog> changeLogs) {
        if (changeLogRepository.isLockedFor(id)) {
            throw new ScriptExecutionException("Database is locked by another locker");
        }

        final List<Integer> scriptsDuplicatedOrders = getDuplicatedOrders(scripts);
        if (!scriptsDuplicatedOrders.isEmpty()) {
            throw new ScriptExecutionException("Found duplicated orders in cql scripts: " +
                    Arrays.toString(scriptsDuplicatedOrders.toArray()));
        }

        for (int i = 0; i < scripts.size(); i++) {
            if (scripts.get(i).getOrder() != i + 1) {
                throw new ScriptExecutionException(String.format("Can't find script for order %s.", i + 1));
            }
        }

        for (int i = 0; i < changeLogs.size(); i++) {
            if (scripts.size() <= i) {
                throw new ScriptExecutionException(String.format("No script in %s place", i + 1));
            }
            checkChangeLog(scripts.get(i), changeLogs.get(i));
            scripts.get(i).setExecuted(true);
        }
    }

    private void checkChangeLog(final CqlScript script, final ChangeLog changeLog) {

        if (!Objects.equals(changeLog.getKeyspace(), script.getKeyspace())) {
            throw new ScriptExecutionException(String.format("Keyspace is not the same for change log with order '%s'. " +
                    "Expected '%s', got '%s'", changeLog.getOrderExecuted(), changeLog.getKeyspace(), script.getKeyspace()));
        }

        if (!Objects.equals(changeLog.getOrderExecuted(), script.getOrder())) {
            throw new ScriptExecutionException(String.format("Order of execution is not valid for change log with order '%s'. " +
                    "Expected '%s', got '%s'", changeLog.getOrderExecuted(), changeLog.getOrderExecuted(), script.getOrder()));
        }

        if (!Objects.equals(changeLog.getMd5sum(), Utils.md5(script.getContent()))) {
            throw new ScriptExecutionException(String.format("Check sum is not the same for script with order '%s'",
                    changeLog.getOrderExecuted()));
        }
    }

    private List<Integer> getDuplicatedOrders(final List<CqlScript> scripts) {
        final Set<Integer> unique = new HashSet<>();
        return scripts.stream()
                .map(CqlScript::getOrder)
                .filter(order -> !unique.add(order))
                .distinct()
                .collect(Collectors.toList());
    }
}
