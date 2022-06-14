package com.epam.esm.api;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.hatoaes.HateoasAdder;
import com.epam.esm.hatoaes.impl.PaginationHateoasAdderImpl;
import com.epam.esm.pagination.PaginationResult;
import com.epam.esm.response.SuccessResponse;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class {@code OrderApiController} controller api which operation of all order system.
 */

@RestController
@RequestMapping("/orders")
public class OrderApiController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HateoasAdder<OrderDto> hateoasAdder;

    @Autowired
    private PaginationHateoasAdderImpl<OrderDto> hateoasAdderForPagination;

    /**
     * Method for getting all object from db
     *
     * @return List of OrderDto entity is found entities in db
     */
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

    /**
     * Method for getting object with id from db
     *
     * @param id it is id of object which is getting
     * @return OrderDto entity is found
     */
    @GetMapping("/{id}")
    public OrderDto getById(@PathVariable long id) {
        OrderDto orderDto = orderService.getById(id);
        hateoasAdder.addFullLinks(orderDto);
        return orderDto;
    }

    /**
     * Method for inserting object to db
     *
     * @param orderDto is id of object which is getting
     * @return OrderDto entity is inserted
     */
    @PostMapping
    public OrderDto insert(@RequestBody OrderDto orderDto) {
        OrderDto savedOrder = orderService.insert(orderDto);
        hateoasAdder.addFullLinks(savedOrder);
        return savedOrder;
    }

    /**
     * Method for inserting object to db
     *
     * @param orderDtos List of Order Objects is inserting
     * @return OrderDto entity is inserted
     */
    @PostMapping("/list")
    public SuccessResponse insertList(@RequestBody List<OrderDto> orderDtos) {
        for (OrderDto orderDto : orderDtos) {
            orderService.insert(orderDto);

        }
        return new SuccessResponse(true, "Objects was successfully created");
    }

}
