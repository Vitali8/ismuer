package com.upfordown.ismuer.core.persistance.repository;

import com.upfordown.ismuer.core.persistance.model.Measure;
import org.springframework.data.cassandra.repository.MapIdCassandraRepository;

public interface MeasureRepository extends MapIdCassandraRepository<Measure> {
}
