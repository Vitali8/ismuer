package com.upfordown.ismuer.core.configuration.codec;

import com.datastax.oss.driver.api.core.ProtocolVersion;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.datastax.oss.driver.internal.core.type.codec.UuidCodec;
import com.upfordown.ismuer.core.factory.DateTimeFactory;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.joda.time.DateTime;

import java.nio.ByteBuffer;

public class TimeUuidToDateTimeCodec implements TypeCodec<DateTime> {

    private final UuidCodec uuidCodec = new UuidCodec();

    @NonNull
    @Override
    public GenericType<DateTime> getJavaType() {
        return GenericType.of(DateTime.class);
    }

    @NonNull
    @Override
    public DataType getCqlType() {
        return DataTypes.TIMEUUID;
    }

    @Nullable
    @Override
    public ByteBuffer encode(final DateTime value, final ProtocolVersion protocolVersion) {
        return uuidCodec.encode(DateTimeFactory.startOf(value), protocolVersion);
    }

    @Nullable
    @Override
    public DateTime decode(final ByteBuffer bytes, final ProtocolVersion protocolVersion) {
        return (bytes == null || bytes.remaining() == 0) ? null : 
                DateTimeFactory.toDateTime(uuidCodec.decode(bytes, protocolVersion));
    }

    @Nullable
    @Override
    public DateTime parse(final String value) {
        return DateTimeFactory.toDateTime(uuidCodec.parse(value));
    }

    @NonNull
    @Override
    public String format(final DateTime value) {
        return uuidCodec.format(DateTimeFactory.startOf(value));
    }
}
