package com.upfordown.ismuer.core.persistance.service;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.Statement;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.upfordown.ismuer.core.dto.MeasureCreateDto;
import com.upfordown.ismuer.core.persistance.DatabaseSchema;
import com.upfordown.ismuer.core.persistance.model.Measure;
import com.upfordown.ismuer.core.persistance.repository.MeasureRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;

@Service
public class MeasureService {
    private static final SimpleStatement selectMeasures = selectFrom(DatabaseSchema.Table.MEASURES).all()
            .whereColumn(DatabaseSchema.Field.METER_ID).isEqualTo(bindMarker())
            .whereColumn(DatabaseSchema.Field.CHECKED_AT).isGreaterThanOrEqualTo(bindMarker())
            .whereColumn(DatabaseSchema.Field.CHECKED_AT).isLessThanOrEqualTo(bindMarker())
            .orderBy(DatabaseSchema.Field.CHECKED_AT, ClusteringOrder.DESC)
            .build();
    private final MeasureRepository repository;
    private final CqlSession cqlSession;
    private final CassandraOperations cassandraTemplate;

    public MeasureService(MeasureRepository repository, CqlSession cqlSession, CassandraOperations cassandraTemplate) {
        this.repository = repository;
        this.cqlSession = cqlSession;
        this.cassandraTemplate = cassandraTemplate;
    }

    public Optional<Measure> getLatestMeasure(String meterId) {
        return repository.findById(BasicMapId.id(DatabaseSchema.Field.METER_ID, meterId));
    }

    public List<Measure> getMeasures(String meterId) {
        return repository.findAllById(Collections.singleton(BasicMapId.id(DatabaseSchema.Field.METER_ID, meterId)));
    }

    public List<Measure> getMeasures(String meterId, DateTime from, DateTime to) {
        final Statement<?> getUserById = cqlSession.prepare(selectMeasures)
                .bind(meterId, from, to);

        return cassandraTemplate.select(getUserById, Measure.class);
    }

    public Measure create(MeasureCreateDto dto) {
        final var measure = new Measure(dto.getMeterId(), DateTime.now(DateTimeZone.UTC), dto.getAmount(), dto.getUnit());
        return repository.save(measure);
    }
}
