package com.upfordown.ismuer.core.controller;

import com.upfordown.ismuer.core.dto.MeterCreateDto;
import com.upfordown.ismuer.core.dto.MeterDto;
import com.upfordown.ismuer.core.mapper.MeterMapper;
import com.upfordown.ismuer.core.persistance.service.MeterService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8081")
@Validated
@RestController
@RequestMapping("/meters")
public class MeterController {
    private final MeterService meterService;

    public MeterController(MeterService meterService) {
        this.meterService = meterService;
    }

    @GetMapping("/{meterId}")
    public ResponseEntity<MeterDto> getMeter(@PathVariable String meterId) {
        return ResponseEntity.of(meterService.findById(meterId).map(MeterMapper::toDto));
    }

    @PostMapping
    public ResponseEntity<MeterDto> addMeter(@RequestBody MeterCreateDto dto) {
        return ResponseEntity.ok(MeterMapper.toDto(meterService.create(dto)));
    }
}
