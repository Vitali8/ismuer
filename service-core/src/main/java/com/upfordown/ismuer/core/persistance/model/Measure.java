package com.upfordown.ismuer.core.persistance.model;

import com.upfordown.ismuer.core.persistance.DatabaseSchema;
import org.joda.time.DateTime;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;

@Table(DatabaseSchema.Table.MEASURES)
public class Measure {

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 0, name = DatabaseSchema.Field.METER_ID)
    private String meterId;

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING, ordinal = 1,
            name = DatabaseSchema.Field.CHECKED_AT)
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    private DateTime checkedAt;

    @Column(DatabaseSchema.Field.AMOUNT)
    private BigDecimal amount;

    public Measure() {
    }

    public Measure(String meterId, DateTime checkedAt, BigDecimal amount) {
        this.meterId = meterId;
        this.checkedAt = checkedAt;
        this.amount = amount;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public DateTime getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(DateTime checkedAt) {
        this.checkedAt = checkedAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Measure{" +
                "meterId='" + meterId + '\'' +
                ", checkedAt=" + checkedAt +
                ", amount=" + amount +
                '}';
    }
}
