package com.upfordown.ismuer.core.controller;

import com.upfordown.ismuer.core.dto.MeasureCreateDto;
import com.upfordown.ismuer.core.dto.MeasureDto;
import com.upfordown.ismuer.core.mapper.MeasureMapper;
import com.upfordown.ismuer.core.persistance.model.Measure;
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
@RequestMapping("/measures")
public class MeasureController {
    private final MeasureService measureService;

    public MeasureController(MeasureService measureService) {
        this.measureService = measureService;
    }

    @GetMapping("/{meterId}")
    public ResponseEntity<List<MeasureDto>> getAllMeasures(@PathVariable @NotNull String meterId, @RequestParam DateTime from, @RequestParam DateTime to) {
        final List<Measure> measures;
        if (from == null || to == null) {
            measures = measureService.getMeasures(meterId);
        } else {
            measures = measureService.getMeasures(meterId, from, to);
        }
        return ResponseEntity.ok(
                measures.stream()
                        .map(MeasureMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResponseEntity<MeasureDto> measure(@RequestBody MeasureCreateDto dto) {
        return ResponseEntity.ok(MeasureMapper.toDto(measureService.create(dto)));
    }
}
