package com.upfordown.ismuer.core.persistance;

public interface DatabaseSchema {

    interface Table {
        String MEASURES = "measures";
    }

    interface Field {
        String METER_ID = "meter_id";
        String CHECKED_AT = "checked_at";
        String AMOUNT = "amount";
        String UNIT = "unit";
    }
}
