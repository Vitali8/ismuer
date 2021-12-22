package com.upfordown.ismuer.core.dto;

import com.datastax.oss.driver.api.core.cql.PagingState;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Max;
import java.nio.ByteBuffer;

public class SliceRequestPageable {

    private String pagingState;
    @Max(value = 1000)
    private Integer size = 10;
    private Sort sort = Sort.unsorted();

    public SliceRequestPageable() {
    }

    public SliceRequestPageable(String pagingState, Integer size, Sort sort) {
        this.pagingState = pagingState;
        this.size = size;
        this.sort = sort;
    }

    public String getPagingState() {
        return pagingState;
    }

    public void setPagingState(String pagingState) {
        this.pagingState = pagingState;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public CassandraPageRequest toPageRequest() {
        ByteBuffer pagingState = null;
        if (this.pagingState != null) {
            pagingState = PagingState.fromString(this.pagingState).getRawPagingState();
        }
        final var pageRequest = PageRequest.of(0, size, sort);
        return CassandraPageRequest.of(pageRequest, pagingState);
    }
}

