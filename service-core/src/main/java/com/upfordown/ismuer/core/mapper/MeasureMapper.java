package com.upfordown.ismuer.core.mapper;

import com.upfordown.ismuer.core.dto.MeasureDto;
import com.upfordown.ismuer.core.persistance.model.Measure;

public class MeasureMapper {
    private MeasureMapper() {
    }

    public static MeasureDto toDto(Measure measure) {
        final var dto = new MeasureDto();
        dto.setMeterId(measure.getMeterId());
        dto.setCheckedAt(measure.getCheckedAt());
        dto.setAmount(measure.getAmount());
        dto.setUnit(measure.getUnit());
        return dto;
    }
}
