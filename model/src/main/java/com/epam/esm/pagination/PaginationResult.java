package com.epam.esm.pagination;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class PaginationResult<E> extends RepresentationModel<PaginationResult<E>> {

    private Page page;

    private List<E> records;

    public PaginationResult() {
    }

    public PaginationResult(Page page, List<E> records) {
        this.page = page;
        this.records = records;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<E> getRecords() {
        return records;
    }

    public void setRecords(List<E> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "PaginationResult{" +
                "page=" + page +
                ", records=" + records +
                '}';
    }
}

