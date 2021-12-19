package com.upfordown.ismuer.core.factory;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.UUID;

public class DateTimeFactory {

    public static DateTime toStartOfDay(final DateTime dateTime) {
        return dateTime.toLocalDate().toDateTimeAtStartOfDay(DateTimeZone.UTC);
    }

    public static Long toMillis(final UUID uuid) {
        return uuid == null ? null : Uuids.unixTimestamp(uuid);
    }

    public static Long toMillis(final DateTime dateTime) {
        return dateTime == null ? null : dateTime.getMillis();
    }

    public static DateTime toDateTime(final UUID uuid) {
        return uuid == null ? null : new DateTime(Uuids.unixTimestamp(uuid));
    }

    public static UUID startOf(final DateTime dateTime) {
        return dateTime == null ? null : Uuids.startOf(dateTime.getMillis());
    }

    public static UUID endOf(final DateTime dateTime) {
        return dateTime == null ? null : Uuids.endOf(dateTime.getMillis());
    }

    public static DateTime now() {
        return DateTime.now(DateTimeZone.UTC);
    }
}
