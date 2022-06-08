package com.epam.esm.api;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.hatoaes.HateoasAdder;
import com.epam.esm.hatoaes.impl.PaginationHateoasAdderImpl;
import com.epam.esm.pagination.PaginationResult;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserApiController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private HateoasAdder<UserDto> hateoasAdder;
    @Autowired
    private HateoasAdder<OrderDto> hateoasAdderForOrder;

    @Autowired
    private PaginationHateoasAdderImpl<UserDto> hateoasAdderForPagination;

    @GetMapping
    public ResponseEntity<PaginationResult<UserDto>> getAll(EntityPage entityPage) {
        PaginationResult<UserDto> paginationResult = userService.getAll(entityPage);
        //adding pagination hal links
        hateoasAdderForPagination.setResourceName("users");
        hateoasAdderForPagination.addSelfLinks(paginationResult);
        //adding hateoas for each object
        List<UserDto> giftList = paginationResult.getRecords()
                .stream()
                .peek(hateoasAdder::addSelfLinks)
                .collect(Collectors.toList());
        paginationResult.setRecords(giftList);

        if (entityPage.getSize() == paginationResult.getPage().getTotalRecords()) {
            return new ResponseEntity<>(paginationResult, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(paginationResult, HttpStatus.PARTIAL_CONTENT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable long id) {
        UserDto userDto = userService.getById(id);
        hateoasAdder.addFullLinks(userDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/{id}/order")
    public ResponseEntity<List<OrderDto>> getOrders(@PathVariable long id) {
        List<OrderDto> orderDtos = orderService.getOrderByUser(id)
                .stream().peek(hateoasAdderForOrder::addSelfLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }

}
