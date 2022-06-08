package com.epam.esm.hatoaes;

import org.springframework.hateoas.RepresentationModel;

public interface HateoasAdder<T extends RepresentationModel<T>> {

    void addSelfLinks(T entity);

    void addFullLinks(T entity);


}
