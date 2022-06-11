package com.epam.esm.repository.pagination;

import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.pagination.Page;
import com.epam.esm.pagination.PaginationResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneratePaginate<T> {
    public PaginationResult<T> generatePaginate(
            EntityPage page,
            int lastPageNumber,
            long totalRecords,
            List<T> entityList

    ) {
        PaginationResult<T> result = new PaginationResult<>();
        Page presentPage = new Page();
        presentPage.setCurrentPageNumber(page.getPage());
        presentPage.setPageSize(page.getSize());
        presentPage.setLastPageNumber(lastPageNumber);
        presentPage.setTotalRecords(totalRecords);
        result.setPage(presentPage);
        result.setRecords(entityList);
        return result;
    }
}
