package com.upfordown.ismuer.core.persistance.service;

import com.upfordown.ismuer.core.dto.MeasureCreateDto;
import com.upfordown.ismuer.core.factory.DateTimeFactory;
import com.upfordown.ismuer.core.persistance.DatabaseSchema;
import com.upfordown.ismuer.core.persistance.model.Measure;
import com.upfordown.ismuer.core.persistance.repository.MeasureRepository;
import org.joda.time.DateTime;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeasureService {
    private final MeasureRepository repository;

    public MeasureService(MeasureRepository repository) {
        this.repository = repository;
    }

    public Optional<Measure> getLatestMeasure(String meterId) {
        return repository.findById(BasicMapId.id(DatabaseSchema.Field.METER_ID, meterId));
    }

    public Slice<Measure> getMeasures(String meterId, CassandraPageRequest pageable) {
        return repository.findAllByMeterId(meterId, pageable);
    }

    public Slice<Measure> getMeasures(String meterId, DateTime from, DateTime to, CassandraPageRequest pageable) {
        return repository.findAllByMeterIdAndCheckedAtBetween(meterId, from, to, pageable);
    }

    public Measure create(MeasureCreateDto dto) {
        final var measure = new Measure(dto.getMeterId(), DateTimeFactory.now(), dto.getAmount());
        return repository.save(measure);
    }
}
