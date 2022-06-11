package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.List;

public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private String create_date;
    private String last_update_date;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TagDto> tags;

    public GiftCertificateDto() {
    }

    public GiftCertificateDto(long id) {
        this.id = id;
    }

    public GiftCertificateDto(long id, String name, String description, BigDecimal price, int duration, String create_date, String last_update_date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.create_date = create_date;
        this.last_update_date = last_update_date;
    }

    public GiftCertificateDto(long id, String name, String description, BigDecimal price, int duration, String create_date, String last_update_date, List<TagDto> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.create_date = create_date;
        this.last_update_date = last_update_date;
        this.tags = tags;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getLast_update_date() {
        return last_update_date;
    }

    public void setLast_update_date(String last_update_date) {
        this.last_update_date = last_update_date;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        this.tags = tags;
    }

}
