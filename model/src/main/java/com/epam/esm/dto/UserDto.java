package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDto extends RepresentationModel<UserDto> {
    private long id;
    private String name;
    private List<OrderDto> orders;

    public UserDto() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDto> orders) {
        this.orders = orders;
    }
}
