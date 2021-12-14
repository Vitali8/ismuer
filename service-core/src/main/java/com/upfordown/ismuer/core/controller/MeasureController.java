package com.upfordown.ismuer.core.controller;

import com.upfordown.ismuer.core.dto.MeasureCreateDto;
import com.upfordown.ismuer.core.dto.MeasureDto;
import com.upfordown.ismuer.core.mapper.MeasureMapper;
import com.upfordown.ismuer.core.persistance.service.MeasureService;
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
@RequestMapping("/api/measures")
public class MeasureController {
    private final MeasureService measureService;

    public MeasureController(MeasureService measureService) {
        this.measureService = measureService;
    }

    @GetMapping
    public ResponseEntity<List<MeasureDto>> getAllMeasures(@RequestParam @NotNull String meterId) {
        return ResponseEntity.ok(
                measureService.getMeasures(meterId).stream()
                        .map(MeasureMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping
    public ResponseEntity<List<MeasureDto>> getAllMeasures(@RequestParam @NotNull String meterId, DateTime from, DateTime to) {
        return ResponseEntity.ok(
                measureService.getMeasures(meterId, from, to).stream()
                        .map(MeasureMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResponseEntity<MeasureDto> measure(@RequestBody MeasureCreateDto dto) {
        return ResponseEntity.ok(MeasureMapper.toDto(measureService.create(dto)));
    }
}
