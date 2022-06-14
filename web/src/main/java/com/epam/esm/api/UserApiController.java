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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class {@code UserApiController} controller api which operation of all user system.
 */

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
    @Autowired
    private PaginationHateoasAdderImpl<OrderDto> hateoasAdderForPaginationForOrder;

    /**
     * Method for getting all object from db
     *
     * @param entityPage - EntityPage is collection of page parameters in object
     * @return PaginationResult<UserDTo> entity is found entities in db
     */
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

    /**
     * Method for getting object with id from db
     *
     * @param id it is id of object which is getting
     * @return UserDto entity is found
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable long id) {
        UserDto userDto = userService.getById(id);
        hateoasAdder.addFullLinks(userDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Method for getting orders with id of users
     *
     * @param id         ID is user's id
     * @param entityPage page and sorting parameters
     * @return PaginationResult<OrderDto>
     */
    @GetMapping("/{id}/orders")
    public ResponseEntity<PaginationResult<OrderDto>> getOrders(@PathVariable long id, EntityPage entityPage) {
        PaginationResult<OrderDto> paginationResult = orderService.getOrderByUser(id, entityPage);
        //adding pagination hal links
        hateoasAdderForPagination.setResourceName("users/" + id + "/order");
        hateoasAdderForPaginationForOrder.addSelfLinks(paginationResult);
        //adding hateoas for each object
        List<OrderDto> orderList = paginationResult.getRecords()
                .stream()
                .peek(hateoasAdderForOrder::addSelfLinks)
                .collect(Collectors.toList());
        paginationResult.setRecords(orderList);

        if (entityPage.getSize() == paginationResult.getPage().getTotalRecords()) {
            return new ResponseEntity<>(paginationResult, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(paginationResult, HttpStatus.PARTIAL_CONTENT);
        }
    }

    /**
     * Method for inserting order for user
     *
     * @param id       ID is user's id
     * @param orderDto OrderDto is needed to inserted
     * @return UserDto which is updated
     */
    @PostMapping("/{id}/order")
    public ResponseEntity<UserDto> saveOrder(@PathVariable long id,
                                             @RequestBody OrderDto orderDto) {
        UserDto userDto = orderService.saveByUser(id, orderDto.getGift_certificates());
        hateoasAdder.addFullLinks(userDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

}
