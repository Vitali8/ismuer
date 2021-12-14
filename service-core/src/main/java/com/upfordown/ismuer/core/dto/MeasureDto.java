package com.upfordown.ismuer.core.dto;

import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class MeasureDto {
    @NotNull
    private String meterId;
    @NotNull
    private DateTime checkedAt;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private String unit;

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
