package com.epam.esm.hatoaes.impl;

import com.epam.esm.hatoaes.HateoasAdder;
import com.epam.esm.pagination.PaginationResult;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class PaginationHateoasAdderImpl<T> implements HateoasAdder<PaginationResult<T>> {
    private String resourceName;

    public static String createLinkHeader(final String uri) {
        return uri;
    }

    @Override
    public void addSelfLinks(PaginationResult<T> paginationResult) {
        int totalPages = paginationResult.getPage().getLastPageNumber();
        int page = paginationResult.getPage().getCurrentPageNumber();
        int pageSize = paginationResult.getPage().getPageSize();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("localhost:8080/");
        uriBuilder.path("/" + resourceName);

        final StringBuilder linkHeader = new StringBuilder();
        if (hasNextPage(page, totalPages)) {
            final String uriForNextPage = constructNextPageUri(uriBuilder, page, pageSize);
            paginationResult.add(Link.of(createLinkHeader(uriForNextPage)).withRel("next"));
        }
        if (hasPreviousPage(page)) {
            final String uriForPrevPage = constructPrevPageUri(uriBuilder, page, pageSize);
            appendCommaIfNecessary(linkHeader);
            paginationResult.add(Link.of(createLinkHeader(uriForPrevPage)).withRel("prev"));
        }
        if (hasFirstPage(page)) {
            final String uriForFirstPage = constructFirstPageUri(uriBuilder, pageSize);
            appendCommaIfNecessary(linkHeader);
            paginationResult.add(Link.of(createLinkHeader(uriForFirstPage)).withRel("first"));
        }
        if (hasLastPage(page, totalPages)) {
            final String uriForLastPage = constructLastPageUri(uriBuilder, totalPages, pageSize);
            appendCommaIfNecessary(linkHeader);
            paginationResult.add(Link.of(createLinkHeader(uriForLastPage)).withRel("last"));
        }
    }

    @Override
    public void addFullLinks(PaginationResult<T> entity) {
    }

    String constructNextPageUri(final UriComponentsBuilder uriBuilder, final int page, final int size) {
        return uriBuilder.replaceQueryParam("page", page + 1).replaceQueryParam("size", size).build().encode().toUriString();
    }

    String constructPrevPageUri(final UriComponentsBuilder uriBuilder, final int page, final int size) {
        int prevPage = page != 1 ? page - 1 : 1;
        return uriBuilder.replaceQueryParam("page", prevPage).replaceQueryParam("size", size).build().encode().toUriString();
    }

    String constructFirstPageUri(final UriComponentsBuilder uriBuilder, final int size) {
        return uriBuilder.replaceQueryParam("page", 1).replaceQueryParam("size", size).build().encode().toUriString();
    }

    String constructLastPageUri(final UriComponentsBuilder uriBuilder, final int totalPages, final int size) {
        return uriBuilder.replaceQueryParam("page", totalPages).replaceQueryParam("size", size).build().encode().toUriString();
    }

    boolean hasNextPage(final int page, final int totalPages) {
        return page < totalPages;
    }

    boolean hasPreviousPage(final int page) {
        return page > 1;
    }

    boolean hasFirstPage(final int page) {
        return hasPreviousPage(page);
    }

    boolean hasLastPage(final int page, final int totalPages) {
        return totalPages > 1 && hasNextPage(page, totalPages);
    }

    void appendCommaIfNecessary(final StringBuilder linkHeader) {
        if (linkHeader.length() > 0) {
            linkHeader.append(", ");
        }
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

}
