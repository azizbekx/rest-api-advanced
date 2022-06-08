package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

public class OrderDto extends RepresentationModel<OrderDto> {
    private long id;
    private BigDecimal price;
    private String create_date;
    private long user_id;
    private long gift_certificate_id;

    public OrderDto() {
    }

    public OrderDto(long user_id, long gift_certificate_id) {
        this.user_id = user_id;
        this.gift_certificate_id = gift_certificate_id;
    }

    public OrderDto(long id, BigDecimal price, String create_date, long user_id, long gift_certificate_id) {
        this.id = id;
        this.price = price;
        this.create_date = create_date;
        this.user_id = user_id;
        this.gift_certificate_id = gift_certificate_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getGift_certificate_id() {
        return gift_certificate_id;
    }

    public void setGift_certificate_id(long gift_certificate_id) {
        this.gift_certificate_id = gift_certificate_id;
    }
}
