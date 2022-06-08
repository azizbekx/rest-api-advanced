package com.epam.esm.api;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.hatoaes.HateoasAdder;
import com.epam.esm.hatoaes.impl.PaginationHateoasAdderImpl;
import com.epam.esm.pagination.PaginationResult;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HateoasAdder<OrderDto> hateoasAdder;

    @Autowired
    private PaginationHateoasAdderImpl<OrderDto> hateoasAdderForPagination;


    @GetMapping
    public ResponseEntity<PaginationResult<OrderDto>> getAll(EntityPage entityPage) {
        PaginationResult<OrderDto> paginationResult = orderService.getAll(entityPage);
        //adding pagination hal links
        hateoasAdderForPagination.setResourceName("orders");
        hateoasAdderForPagination.addSelfLinks(paginationResult);
        //adding hateoas for each object
        List<OrderDto> orderDtoList = paginationResult.getRecords()
                .stream()
                .peek(hateoasAdder::addSelfLinks)
                .collect(Collectors.toList());
        paginationResult.setRecords(orderDtoList);

        if (entityPage.getSize() == paginationResult.getPage().getTotalRecords()) {
            return new ResponseEntity<>(paginationResult, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(paginationResult, HttpStatus.PARTIAL_CONTENT);
        }
    }

    @GetMapping("/{id}")
    public OrderDto getById(@PathVariable long id) {
        OrderDto orderDto = orderService.getById(id);
        hateoasAdder.addFullLinks(orderDto);
        return orderDto;
    }

    @PostMapping
    public OrderDto insert(@RequestBody OrderDto orderDto) {
        return orderService.insert(orderDto);
    }


}
