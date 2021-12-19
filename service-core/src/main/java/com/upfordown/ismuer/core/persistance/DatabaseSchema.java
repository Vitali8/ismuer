package com.upfordown.ismuer.core.persistance;

public interface DatabaseSchema {

    interface Table {
        String MEASURES = "measures";
        String METERS = "meters";
        String USER_METERS = "user_meters";
    }

    interface Field {
        String METER_ID = "meter_id";
        String CHECKED_AT = "checked_at";
        String AMOUNT = "amount";
        String UNIT = "unit";
        String DEACTIVATED_AT = "deactivated_at";
        String USER = "user";
        String ADDED_AT = "added_at";
    }
}
