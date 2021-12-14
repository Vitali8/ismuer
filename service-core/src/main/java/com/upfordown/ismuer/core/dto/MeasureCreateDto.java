package com.upfordown.ismuer.core.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class MeasureCreateDto {
    @NotNull
    private String meterId;
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
