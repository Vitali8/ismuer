package com.upfordown.ismuer.core.persistance.service;

import com.upfordown.ismuer.core.dto.MeterCreateDto;
import com.upfordown.ismuer.core.factory.DateTimeFactory;
import com.upfordown.ismuer.core.persistance.DatabaseSchema;
import com.upfordown.ismuer.core.persistance.model.Meter;
import com.upfordown.ismuer.core.persistance.model.UserMeter;
import com.upfordown.ismuer.core.persistance.repository.MeterRepository;
import com.upfordown.ismuer.core.persistance.repository.UserMeterRepository;
import org.joda.time.DateTime;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeterService {
    private final MeterRepository meterRepository;
    private final UserMeterRepository userMeterRepository;

    public MeterService(MeterRepository meterRepository, UserMeterRepository userMeterRepository) {
        this.meterRepository = meterRepository;
        this.userMeterRepository = userMeterRepository;
    }

    public List<Meter> findAllByUser(String user) {
        final var ids = Collections.singleton(BasicMapId.id(DatabaseSchema.Field.USER, user));
        final var meterIds = userMeterRepository.findAllById(ids).stream()
                .map(UserMeter::getMeterId)
                .collect(Collectors.toList());
        return meterRepository.findAllById(meterIds);
    }

    public Optional<Meter> findById(String meterId) {
        return meterRepository.findById(meterId);
    }

    public Meter create(MeterCreateDto dto) {
        final var meter = new Meter(dto.getMeterId(), dto.getUser(), dto.getUnit(), DateTimeFactory.now());
        userMeterRepository.save(new UserMeter(meter.getUser(), meter.getMeterId()));
        return meterRepository.save(meter);
    }

    public void deactivate(String meterId, DateTime deactivateAt) {
        meterRepository.findById(meterId).ifPresent(meter -> {
            meter.setDeactivatedAt(deactivateAt);
            meterRepository.save(meter);
        });
    }
}
