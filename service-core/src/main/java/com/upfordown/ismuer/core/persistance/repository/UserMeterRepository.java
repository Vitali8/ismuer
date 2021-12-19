package com.upfordown.ismuer.core.persistance.repository;

import com.upfordown.ismuer.core.persistance.model.UserMeter;
import org.springframework.data.cassandra.repository.MapIdCassandraRepository;

public interface UserMeterRepository extends MapIdCassandraRepository<UserMeter> {
}
