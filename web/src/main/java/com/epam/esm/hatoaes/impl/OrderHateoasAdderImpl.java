package com.epam.esm.hatoaes.impl;

import com.epam.esm.api.GiftCertificateApiController;
import com.epam.esm.api.OrderApiController;
import com.epam.esm.api.TagApiController;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
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
    private static final Class<TagApiController> TAG_CONTROLLER =
            TagApiController.class;

    @Override
    public void addSelfLinks(OrderDto orderDto) {
        orderDto.add(linkTo(methodOn(ORDER_CONTROLLER)
                .getById(orderDto.getId())).withRel("self"));

        for (GiftCertificateDto gift_certificate : orderDto.getGift_certificates()) {
            gift_certificate.add(linkTo(methodOn(GIFT_CONTROLLER)
                    .getById(gift_certificate.getId())).withRel("self gift certificate"));
            if (gift_certificate.getTags() != null) {
                for (TagDto tag : gift_certificate.getTags()) {
                    tag.add(linkTo(methodOn(TAG_CONTROLLER)
                            .getById(tag.getId())).withRel("self tag"));
                }
            }
        }
    }

    @Override
    public void addFullLinks(OrderDto orderDto) {
        orderDto.add(linkTo(methodOn(ORDER_CONTROLLER)
                .insert(orderDto)).withRel("insert"));

    }

}
