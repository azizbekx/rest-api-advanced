package com.epam.esm.hatoaes.impl;

import com.epam.esm.api.GiftCertificateApiController;
import com.epam.esm.api.OrderApiController;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.hatoaes.HateoasAdder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderHateoasAdderImpl implements HateoasAdder<OrderDto> {
    private static final Class<OrderApiController> ORDER_CONTROLLER =
            OrderApiController.class;
    private static final Class<GiftCertificateApiController> GIFT_CONTROLLER =
            GiftCertificateApiController.class;

    @Override
    public void addSelfLinks(OrderDto orderDto) {
        orderDto.add(linkTo(methodOn(ORDER_CONTROLLER)
                .getById(orderDto.getId())).withRel("self"));

        orderDto.add(linkTo(methodOn(GIFT_CONTROLLER)
                .getById(orderDto.getGift_certificate_id())).withRel("self gift certificate"));
    }

    @Override
    public void addFullLinks(OrderDto orderDto) {
        orderDto.add(linkTo(methodOn(ORDER_CONTROLLER)
                .insert(orderDto)).withRel("insert"));

    }

}
