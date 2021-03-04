package com.example.demo.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Where(clause="deleted=0") //hibernate way of deletion
public class Invoice {


    @Id
    @SequenceGenerator(
            name = "invoice_sequence",
            sequenceName = "invoice_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "invoice_sequence"
    )
    private Integer id;

//    @CreatedBy
//    private User user;

    private Long number;

    private String name;

    private Float total;

    @Column(name="issued", columnDefinition="datetime DEFAULT CURRENT_TIMESTAMP")
    private Date issued;

//    @CreatedDate
//    private Instant created;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice")
//    @JsonBackReference
    private List<Item> items;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice")
    private List<Track> tracks;


    @CreationTimestamp
    @Column(name="created", columnDefinition="datetime DEFAULT CURRENT_TIMESTAMP")
    private Instant created;

//    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "updated", columnDefinition ="datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Instant updated;

    @Column(name="deleted", columnDefinition = "tinyint(1) NOT NULL DEFAULT '0'")
    private boolean deleted = false;

    public Invoice() {
    }

    public Invoice(Long number, String name, Float total, Date issued) {
        this.number = number;
        this.name = name;
        this.total = total;
        this.issued = issued;
    }

    public Invoice(Integer id, Long number, String name, Float total, Date issued, List<Item> items, List<Track> tracks) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.total = total;
        this.issued = issued;
        this.items = items;
        this.tracks = tracks;
    }

    public Invoice(Long number, String name, Float total, Date issued, List<Item> items) {
        this.number = number;
        this.name = name;
        this.total = total;
        this.issued = issued;
        this.items = items;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Date getIssued() {
        return issued;
    }

    public void setIssued(Date issued) {
        this.issued = issued;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
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
        return "Invoice{" +
                "id=" + id +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", total=" + total +
                ", issued=" + issued +
                ", created=" + created +
                ", updated=" + updated +
                ", deleted=" + deleted +
                '}';
    }
}
