package com.upfordown.ismuer.core.persistance.model;

import com.upfordown.ismuer.core.persistance.DatabaseSchema;
import org.joda.time.DateTime;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

@Table(DatabaseSchema.Table.METERS)
public class Meter {
    @PrimaryKey(DatabaseSchema.Field.METER_ID)
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 0, name = DatabaseSchema.Field.METER_ID)
    private String meterId;

    @Column(DatabaseSchema.Field.USER)
    private String user;

    @Column(DatabaseSchema.Field.UNIT)
    private String unit;

    @Column(DatabaseSchema.Field.ADDED_AT)
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    private DateTime addedAt;

    @Column(DatabaseSchema.Field.DEACTIVATED_AT)
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    private DateTime deactivatedAt;

    public Meter() {
    }

    public Meter(String meterId, String user, String unit, DateTime addedAt) {
        this.meterId = meterId;
        this.user = user;
        this.unit = unit;
        this.addedAt = addedAt;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public DateTime getDeactivatedAt() {
        return deactivatedAt;
    }

    public void setDeactivatedAt(DateTime deactivatedAt) {
        this.deactivatedAt = deactivatedAt;
    }

    public DateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(DateTime addedAt) {
        this.addedAt = addedAt;
    }
}
