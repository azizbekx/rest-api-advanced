package com.epam.esm.hatoaes.impl;

import com.epam.esm.api.OrderApiController;
import com.epam.esm.api.UserApiController;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.hatoaes.HateoasAdder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserHateoasAdderImplAdder implements HateoasAdder<UserDto> {
    private static final Class<UserApiController> USER_CONTROLLER =
            UserApiController.class;
    private static final Class<OrderApiController> ORDER_CONTROLLER =
            OrderApiController.class;

    @Override
    public void addSelfLinks(UserDto userDto) {
        userDto.add(linkTo(methodOn(USER_CONTROLLER)
                .getById(userDto.getId())).withSelfRel());
        if (userDto.getOrders() != null) {
            userDto.getOrders().forEach(
                    tagDto -> tagDto.add(linkTo(methodOn(ORDER_CONTROLLER)
                            .getById(tagDto.getId())).withSelfRel())
            );
        }
    }

    @Override
    public void addFullLinks(UserDto userDto) {
        userDto.add(linkTo(methodOn(USER_CONTROLLER)
                .getAll(new EntityPage())).withRel("get all"));
    }
}
