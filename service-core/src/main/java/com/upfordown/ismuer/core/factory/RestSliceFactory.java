package com.upfordown.ismuer.core.factory;

import com.upfordown.ismuer.core.dto.RestSlice;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;

import java.nio.ByteBuffer;
import java.util.Base64;

public final class RestSliceFactory {

    private RestSliceFactory() {
    }

    public static <T> RestSlice<T> toRestSlice(final Slice<T> slice) {
        final RestSlice<T> result = new RestSlice<>();

        result.setContent(slice.getContent());
        result.setNumberOfElements(slice.getNumberOfElements());
        result.setSize(slice.getSize());
        if (slice.nextOrLastPageable() instanceof CassandraPageRequest) {
            final ByteBuffer pagingState = ((CassandraPageRequest) slice.nextOrLastPageable()).getPagingState();
            if (pagingState != null) {
                byte[] bytes = new byte[pagingState.remaining()];
                pagingState.get(bytes);
                result.setNext(Base64.getEncoder().encodeToString(bytes));
            }
        }

        return result;
    }
}
