package com.upfordown.ismuer.core.persistance.model;

import com.upfordown.ismuer.core.persistance.DatabaseSchema;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(DatabaseSchema.Table.MEASURES)
public class Measure {

}
