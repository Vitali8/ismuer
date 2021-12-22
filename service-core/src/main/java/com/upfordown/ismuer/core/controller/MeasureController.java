package com.upfordown.ismuer.core.controller;

import com.upfordown.ismuer.core.dto.MeasureCreateDto;
import com.upfordown.ismuer.core.dto.MeasureDto;
import com.upfordown.ismuer.core.dto.RestSlice;
import com.upfordown.ismuer.core.dto.SliceRequestPageable;
import com.upfordown.ismuer.core.factory.RestSliceFactory;
import com.upfordown.ismuer.core.mapper.MeasureMapper;
import com.upfordown.ismuer.core.persistance.model.Measure;
import com.upfordown.ismuer.core.persistance.service.MeasureService;
import com.upfordown.ismuer.core.persistance.service.MeterService;
import org.joda.time.DateTime;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@CrossOrigin(origins = "http://localhost:8081")
@Validated
@RestController
@RequestMapping("/measures")
public class MeasureController {
    private final MeasureService measureService;
    private final MeterService meterService;

    public MeasureController(MeasureService measureService, MeterService meterService) {
        this.measureService = measureService;
        this.meterService = meterService;
    }

    @GetMapping("/{meterId}")
    public ResponseEntity<RestSlice<MeasureDto>> getAllMeasures(@PathVariable @NotNull String meterId,
                                                                @RequestParam(required = false) DateTime from,
                                                                @RequestParam(required = false) DateTime to,
                                                                SliceRequestPageable pageable) {
        final var meter = meterService.findById(meterId).orElseThrow();
        final Slice<Measure> measures;
        if (from == null || to == null) {
            measures = measureService.getMeasures(meterId, pageable.toPageRequest());
        } else {
            measures = measureService.getMeasures(meterId, from, to, pageable.toPageRequest());
        }
        return ResponseEntity.ok(RestSliceFactory.toRestSlice(
                measures.map(measure -> MeasureMapper.toDto(measure, meter))
        ));
    }

    @PostMapping
    public ResponseEntity<MeasureDto> measure(@RequestBody MeasureCreateDto dto) {
        final var meter = meterService.findById(dto.getMeterId()).orElseThrow();
        return ResponseEntity.ok(MeasureMapper.toDto(measureService.create(dto), meter));
    }
}
