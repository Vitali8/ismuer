package com.upfordown.ismuer.core.configuration.codec;

import com.datastax.oss.driver.api.core.ProtocolVersion;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.datastax.oss.driver.internal.core.type.codec.TimestampCodec;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.joda.time.DateTime;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.ZoneOffset;

public class TimestampToDateTimeCodec implements TypeCodec<DateTime> {

    private final TimestampCodec timestampCodec = new TimestampCodec(ZoneOffset.UTC.normalized());

    @NonNull
    @Override
    public GenericType<DateTime> getJavaType() {
        return GenericType.of(DateTime.class);
    }

    @NonNull
    @Override
    public DataType getCqlType() {
        return DataTypes.TIMESTAMP;
    }

    @Nullable
    @Override
    public ByteBuffer encode(final DateTime value, final ProtocolVersion protocolVersion) {
        return value == null ? null : TypeCodecs.BIGINT.encodePrimitive(value.getMillis(), protocolVersion);
    }

    @Nullable
    @Override
    public DateTime decode(final ByteBuffer bytes, final ProtocolVersion protocolVersion) {
        return (bytes == null || bytes.remaining() == 0) ? null : new DateTime(TypeCodecs.BIGINT
                .decodePrimitive(bytes, protocolVersion));
    }

    @Nullable
    @Override
    public DateTime parse(final String value) {
        Instant instant = timestampCodec.parse(value);
        return instant == null ? null : new DateTime(instant.toEpochMilli());
    }

    @NonNull
    @Override
    public String format(final DateTime value) {
        Instant instant = value == null ? null : Instant.ofEpochMilli(value.getMillis());
        return timestampCodec.format(instant);
    }
}
