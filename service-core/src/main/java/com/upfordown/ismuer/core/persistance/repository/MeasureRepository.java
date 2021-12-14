package com.upfordown.ismuer.core.persistance.repository;

import com.upfordown.ismuer.core.persistance.model.Measure;
import org.joda.time.DateTime;
import org.springframework.data.cassandra.repository.MapIdCassandraRepository;

import java.util.List;

public interface MeasureRepository extends MapIdCassandraRepository<Measure> {
    List<Measure> getAllByMeterIdAndCheckedAtBetween(String meterId, DateTime from, DateTime to);
}
