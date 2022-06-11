package com.epam.esm.entity.creteria;

public class GiftSearchCriteria {
    private String name;
    private String description;
    private String tag_name;
    private String price;
    private String duration;
    private String create_date;
    private String last_update_date;

    public GiftSearchCriteria() {
    }

    public GiftSearchCriteria(String name, String description, String tag_name,
                              String price, String duration,
                              String create_date, String last_update_date) {
        this.name = name;
        this.description = description;
        this.tag_name = tag_name;
        this.price = price;
        this.duration = duration;
        this.create_date = create_date;
        this.last_update_date = last_update_date;
    }


    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
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
}
