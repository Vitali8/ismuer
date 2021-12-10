package com.upfordown.ismuer.liquibase;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.upfordown.ismuer.liquibase.exception.ScriptExecutionException;
import com.upfordown.ismuer.liquibase.model.ChangeLog;
import org.springframework.data.cassandra.core.cql.support.CachedPreparedStatementCreator;
import org.springframework.data.cassandra.core.cql.support.PreparedStatementCache;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;

public class ChangeLogRepository {

    private static final String CREATE_CHANGE_LOG_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s "
            + "(order_executed int, keyspace_name text, file_name text, executed_at bigint, md5sum text, " +
            "PRIMARY KEY (order_executed))", DatabaseSchema.CHANGE_LOG_TABLE);

    private static final String CREATE_CHANGE_LOG_LOCK_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s "
            + "(id text, locked boolean, locker_id text, PRIMARY KEY (id))", DatabaseSchema.CHANGE_LOG_LOCK_TABLE);

    private static final String DEFAULT_LOCK_ID = "1";

    private final SimpleStatement createChangeLogTable = SimpleStatement.builder(CREATE_CHANGE_LOG_TABLE)
            .setTimeout(Duration.ofSeconds(5))
            .build();

    private final SimpleStatement createChangeLogLockTable = SimpleStatement.builder(CREATE_CHANGE_LOG_LOCK_TABLE)
            .setTimeout(Duration.ofSeconds(5))
            .build();

    private final SimpleStatement insertChangeLog = insertInto(DatabaseSchema.CHANGE_LOG_TABLE)
            .value(DatabaseSchema.ORDER_EXECUTED_COLUMN, bindMarker())
            .value(DatabaseSchema.KEYSPACE_NAME_COLUMN, bindMarker())
            .value(DatabaseSchema.FILE_NAME_COLUMN, bindMarker())
            .value(DatabaseSchema.EXECUTED_AT_COLUMN, bindMarker())
            .value(DatabaseSchema.MD5SUM_COLUMN, bindMarker())
            .build();

    private final SimpleStatement updateChangeLogLock = update(DatabaseSchema.CHANGE_LOG_LOCK_TABLE)
            .setColumn(DatabaseSchema.LOCKED_COLUMN, bindMarker())
            .setColumn(DatabaseSchema.LOCKER_ID_COLUMN, bindMarker())
            .whereColumn(DatabaseSchema.ID_COLUMN).isEqualTo(bindMarker())
            .build();

    private final SimpleStatement selectChangeLog = selectFrom(DatabaseSchema.CHANGE_LOG_TABLE).all()
            .build();

    private final SimpleStatement selectChangeLogLock = selectFrom(DatabaseSchema.CHANGE_LOG_LOCK_TABLE).all()
            .whereColumn(DatabaseSchema.ID_COLUMN).isEqualTo(bindMarker())
            .build();

    private final PreparedStatementCache statementCache = PreparedStatementCache.create();

    private final CqlSession cqlSession;

    private final ConsistencyLevel defaultConsistencyLevel;

    public ChangeLogRepository(final CqlSession cqlSession, final ConsistencyLevel defaultConsistencyLevel) {
        this.cqlSession = cqlSession;
        this.defaultConsistencyLevel = defaultConsistencyLevel;

        // Create tables if needed
        cqlSession.execute(this.createChangeLogTable.setConsistencyLevel(defaultConsistencyLevel));
        cqlSession.execute(this.createChangeLogLockTable.setConsistencyLevel(defaultConsistencyLevel));
    }

    public List<ChangeLog> getChangeLogs() {
        return cqlSession.execute(selectChangeLog.setConsistencyLevel(defaultConsistencyLevel)).all().stream()
                .map(Utils::convertToChangeLog)
                .sorted(Comparator.comparingInt(ChangeLog::getOrderExecuted))
                .collect(Collectors.toList());
    }

    public void insertChangeLog(final int order, final String keyspace, final String fileName, final String content) {
        final BoundStatement statement = CachedPreparedStatementCreator.of(statementCache, insertChangeLog)
                .createPreparedStatement(cqlSession)
                .bind(order, keyspace, fileName, System.currentTimeMillis(), Utils.md5(content))
                .setConsistencyLevel(defaultConsistencyLevel);

        cqlSession.execute(statement);
    }

    public boolean isLockedFor(final String lockerId) {
        final BoundStatement statement = CachedPreparedStatementCreator.of(statementCache, selectChangeLogLock)
                .createPreparedStatement(cqlSession)
                .bind(DEFAULT_LOCK_ID)
                .setConsistencyLevel(defaultConsistencyLevel);

        final ResultSet result = cqlSession.execute(statement);
        final Row changeLogLock = result.one();
        if (changeLogLock == null) {
            return false;
        }

        final boolean locked = changeLogLock.getBoolean(DatabaseSchema.LOCKED_COLUMN);
        final String locker = changeLogLock.getString(DatabaseSchema.LOCKER_ID_COLUMN);

        if (locker == null || !locked) {
            return false;
        }

        return !locker.equals(lockerId);
    }

    public void lock(final String lockerId) {
        if (isLockedFor(lockerId)) {
            throw new ScriptExecutionException("Can't lock. Database is locked by another locker");
        }
        cqlSession.execute(prepareUpdateChangeLock(true, lockerId));
    }

    public void checkServiceTableCreation(final String keyspace) throws InterruptedException {
        int tries = 60;
        while (!(Utils.isTableCreated(cqlSession, DatabaseSchema.CHANGE_LOG_TABLE, keyspace)
                && Utils.isTableCreated(cqlSession, DatabaseSchema.CHANGE_LOG_LOCK_TABLE, keyspace))
                && tries > 0) {
            Thread.sleep(1000);
            tries--;
        }
    }

    public void releaseLock(final String lockerId) {
        if (!isLockedFor(lockerId)) {
            cqlSession.execute(prepareUpdateChangeLock(false, null));
        }
    }

    private Statement<?> prepareUpdateChangeLock(final boolean isLocked, final String lockerId) {
        return CachedPreparedStatementCreator.of(statementCache, updateChangeLogLock)
                .createPreparedStatement(cqlSession)
                .bind(isLocked, lockerId, DEFAULT_LOCK_ID)
                .setConsistencyLevel(defaultConsistencyLevel);
    }
}

