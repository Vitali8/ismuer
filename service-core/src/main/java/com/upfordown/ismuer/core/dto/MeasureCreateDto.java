package com.upfordown.ismuer.core.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class MeasureCreateDto {
    @NotNull
    private String meterId;
    @NotNull
    private BigDecimal amount;

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
}
