package com.upfordown.ismuer.core.mapper;

import com.upfordown.ismuer.core.dto.MeasureDto;
import com.upfordown.ismuer.core.persistance.model.Measure;
import com.upfordown.ismuer.core.persistance.model.Meter;

public class MeasureMapper {
    private MeasureMapper() {
    }

    public static MeasureDto toDto(Measure measure, Meter meter) {
        final var dto = new MeasureDto();
        dto.setMeterId(measure.getMeterId());
        dto.setCheckedAt(measure.getCheckedAt());
        dto.setAmount(measure.getAmount());
        dto.setUnit(meter.getUnit());
        return dto;
    }
}
