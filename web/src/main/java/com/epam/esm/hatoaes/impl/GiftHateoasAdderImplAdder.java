package com.epam.esm.hatoaes.impl;

import com.epam.esm.api.GiftCertificateApiController;
import com.epam.esm.api.TagApiController;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.hatoaes.HateoasAdder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftHateoasAdderImplAdder implements HateoasAdder<GiftCertificateDto> {

    private static final Class<GiftCertificateApiController> GIFT_CONTROLLER =
            GiftCertificateApiController.class;
    private static final Class<TagApiController> TAG_CONTROLLER =
            TagApiController.class;

    @Override
    public void addSelfLinks(GiftCertificateDto giftDto) {
        giftDto.add(linkTo(methodOn(GIFT_CONTROLLER)
                .getById(giftDto.getId())).withRel("self"));

        if (giftDto.getTags() != null) {
            giftDto.getTags().forEach(
                    tagDto -> tagDto.add(linkTo(methodOn(TAG_CONTROLLER)
                            .getById(tagDto.getId())).withRel("self"))
            );
        }
    }

    @Override
    public void addFullLinks(GiftCertificateDto giftDto) {
        giftDto.add(linkTo(methodOn(GIFT_CONTROLLER)
                .getAll(new EntityPage())).withRel("get all"));
        giftDto.add(linkTo(methodOn(GIFT_CONTROLLER)
                .insert(giftDto)).withRel("insert"));
        giftDto.add(linkTo(methodOn(GIFT_CONTROLLER)
                .update(giftDto.getId(), giftDto)).withRel("update"));
        giftDto.add(linkTo(methodOn(GIFT_CONTROLLER)
                .deleteById(giftDto.getId())).withRel("delete"));
    }
}
