package com.upfordown.ismuer.core.mapper;

import com.upfordown.ismuer.core.dto.MeterDto;
import com.upfordown.ismuer.core.persistance.model.Meter;

public class MeterMapper {
    private MeterMapper() {
    }

    public static MeterDto toDto(Meter meter) {
        final var dto = new MeterDto();
        dto.setMeterId(meter.getMeterId());
        dto.setUser(meter.getUser());
        dto.setUnit(meter.getUnit());
        dto.setAddedAt(meter.getAddedAt());
        dto.setDeactivatedAt(meter.getDeactivatedAt());
        return dto;
    }
}
