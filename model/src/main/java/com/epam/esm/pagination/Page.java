package com.epam.esm.pagination;

public class Page {

    private int currentPageNumber;
    private int lastPageNumber;
    private int pageSize;
    private long totalRecords;

    public Page() {
    }

    public Page(int currentPageNumber, int lastPageNumber, int pageSize, long totalRecords) {
        this.currentPageNumber = currentPageNumber;
        this.lastPageNumber = lastPageNumber;
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(int currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    public int getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(int lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    @Override
    public String toString() {
        return "Page{" +
                "currentPageNumber=" + currentPageNumber +
                ", lastPageNumber=" + lastPageNumber +
                ", pageSize=" + pageSize +
                ", totalRecords=" + totalRecords +
                '}';
    }
}
