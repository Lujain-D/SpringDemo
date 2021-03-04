package com.example.demo.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table
@Where(clause="deleted=0") //hibernate way of deletion
public class Item {

    @Id
    @SequenceGenerator(
            name = "item_sequence",
            sequenceName = "item_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "item_sequence"
    )
    private Integer id;

    private String name;
    private Integer count;
    private Float price;

    @ManyToOne
    @JoinColumn(name="invoice_id")
//    @JsonManagedReference
    @JsonIgnore
    private Invoice invoice;


    @CreationTimestamp
    @Column(name="created", columnDefinition="datetime DEFAULT CURRENT_TIMESTAMP")
    private Instant created;

    @UpdateTimestamp
    @Column(name = "updated", columnDefinition ="datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Instant updated;

    @Column(name="deleted", columnDefinition = "tinyint(1) NOT NULL DEFAULT '0'")
    private boolean deleted = false;

    public Item() {
    }

    public Item(String name, Integer count, Float price, Invoice invoice) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.invoice = invoice;
    }

    public Item(Integer id, String name, Integer count, Float price, Invoice invoice, Instant created, Instant updated, boolean deleted) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.price = price;
        this.invoice = invoice;
        this.created = created;
        this.updated = updated;
        this.deleted = deleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", price=" + price +
                ", invoice=" + invoice +
                ", created=" + created +
                ", updated=" + updated +
                ", deleted=" + deleted +
                '}';
    }
}
