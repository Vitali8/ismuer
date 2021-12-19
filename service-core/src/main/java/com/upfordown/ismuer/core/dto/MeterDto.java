package com.upfordown.ismuer.core.dto;

import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class MeterDto {
    @NotNull
    private String meterId;
    @NotNull
    private String user;
    @NotNull
    private String unit;
    @NotNull
    private DateTime addedAt;
    private DateTime deactivatedAt;

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

    public DateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(DateTime addedAt) {
        this.addedAt = addedAt;
    }

    public DateTime getDeactivatedAt() {
        return deactivatedAt;
    }

    public void setDeactivatedAt(DateTime deactivatedAt) {
        this.deactivatedAt = deactivatedAt;
    }
}
