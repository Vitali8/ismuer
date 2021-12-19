package com.upfordown.ismuer.core.controller;

import com.upfordown.ismuer.core.dto.MeasureCreateDto;
import com.upfordown.ismuer.core.dto.MeasureDto;
import com.upfordown.ismuer.core.mapper.MeasureMapper;
import com.upfordown.ismuer.core.persistance.model.Measure;
import com.upfordown.ismuer.core.persistance.service.MeasureService;
import com.upfordown.ismuer.core.persistance.service.MeterService;
import org.joda.time.DateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<MeasureDto>> getAllMeasures(@PathVariable @NotNull String meterId,
                                                           @RequestParam(required = false) DateTime from,
                                                           @RequestParam(required = false) DateTime to) {
        final var meter = meterService.findById(meterId).orElseThrow();
        final List<Measure> measures;
        if (from == null || to == null) {
            measures = measureService.getMeasures(meterId);
        } else {
            measures = measureService.getMeasures(meterId, from, to);
        }
        return ResponseEntity.ok(
                measures.stream()
                        .map(measure -> MeasureMapper.toDto(measure, meter))
                        .collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResponseEntity<MeasureDto> measure(@RequestBody MeasureCreateDto dto) {
        final var meter = meterService.findById(dto.getMeterId()).orElseThrow();
        return ResponseEntity.ok(MeasureMapper.toDto(measureService.create(dto), meter));
    }
}
