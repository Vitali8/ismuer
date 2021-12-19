package com.upfordown.ismuer.core.persistance.repository;

import com.upfordown.ismuer.core.persistance.model.Meter;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface MeterRepository extends CassandraRepository<Meter, String> {
}
