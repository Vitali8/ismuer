package com.upfordown.ismuer.liquibase;

public interface DatabaseSchema {

    String CHANGE_LOG_TABLE = "DATABASE_CHANGE_LOG";
    String CHANGE_LOG_LOCK_TABLE = "DATABASE_CHANGE_LOG_LOCK";

    String ORDER_EXECUTED_COLUMN = "order_executed";
    String KEYSPACE_NAME_COLUMN = "keyspace_name";
    String FILE_NAME_COLUMN = "file_name";
    String EXECUTED_AT_COLUMN = "executed_at";
    String MD5SUM_COLUMN = "md5sum";

    String ID_COLUMN = "id";
    String LOCKED_COLUMN = "locked";
    String LOCKER_ID_COLUMN = "locker_id";
}
