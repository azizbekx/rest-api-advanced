package com.epam.esm.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private BigDecimal price;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "orders_gift_certificates",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "gift_certificate_id"))
    private Set<GiftCertificate> giftCertificates;

    public Order() {
    }

    public Order(User user, Set<GiftCertificate> giftCertificates) {
        this.user = user;
        this.giftCertificates = giftCertificates;
    }

    public Order(BigDecimal price, User user, Set<GiftCertificate> giftCertificates) {
        this.price = price;
        this.user = user;
        this.giftCertificates = giftCertificates;
    }

    public Order(long id, BigDecimal price, LocalDateTime createDate,
                 User user, Set<GiftCertificate> giftCertificates) {
        this.id = id;
        this.price = price;
        this.createDate = createDate;
        this.user = user;
        this.giftCertificates = giftCertificates;
    }


    @PrePersist
    public void onPrePersist() {
        setCreateDate(LocalDateTime.now());
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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<GiftCertificate> getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(Set<GiftCertificate> giftCertificates) {
        this.giftCertificates = giftCertificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return id == order.id && Objects.equals(price, order.price) && Objects.equals(createDate, order.createDate) && Objects.equals(user, order.user) && Objects.equals(giftCertificates, order.giftCertificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, createDate, user, giftCertificates);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", price=" + price +
                ", createDate=" + createDate +
                ", user=" + user +
                ", giftCertificates=" + giftCertificates +
                '}';
    }
}
