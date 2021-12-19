package com.upfordown.ismuer.core.persistance.model;

import com.upfordown.ismuer.core.persistance.DatabaseSchema;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(DatabaseSchema.Table.USER_METERS)
public class UserMeter {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 0, name = DatabaseSchema.Field.USER)
    private String user;
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 1, name = DatabaseSchema.Field.METER_ID)
    private String meterId;

    public UserMeter() {
    }

    public UserMeter(String user, String meterId) {
        this.user = user;
        this.meterId = meterId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }
}
