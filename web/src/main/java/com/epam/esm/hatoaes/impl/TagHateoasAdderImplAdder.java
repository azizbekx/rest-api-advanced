package com.epam.esm.hatoaes.impl;

import com.epam.esm.api.TagApiController;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.hatoaes.HateoasAdder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagHateoasAdderImplAdder implements HateoasAdder<TagDto> {

    private static final Class<TagApiController> TAG_CONTROLLER =
            TagApiController.class;

    @Override
    public void addSelfLinks(TagDto tagDto) {
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER)
                .getById(tagDto.getId())).withRel("self"));
    }

    @Override
    public void addFullLinks(TagDto tagDto) {
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER)
                .getAll(new EntityPage())).withRel("get all"));
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER)
                .insert(tagDto)).withRel("insert"));
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER)
                .deleteById(tagDto.getId())).withRel("delete"));
    }
}
