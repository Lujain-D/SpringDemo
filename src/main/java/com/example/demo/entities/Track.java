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
public class Track {

    @Id
    @SequenceGenerator(
            name = "track_sequence",
            sequenceName = "track_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "track_sequence"
    )

    private Integer id;

    @ManyToOne
    @JoinColumn(name="invoice_id")
    @JsonIgnore
    private Invoice invoice;

    private String action;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;


    @CreationTimestamp
    @Column(name="created", columnDefinition="datetime DEFAULT CURRENT_TIMESTAMP")
    private Instant created;

    @UpdateTimestamp
    @Column(name = "updated", columnDefinition ="datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Instant updated;

    @Column(name="deleted", columnDefinition = "tinyint(1) NOT NULL DEFAULT '0'")
    private boolean deleted = false;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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


    public Track() {
    }

    public Track(Invoice invoice, String action, User user) {
        this.invoice = invoice;
        this.action = action;
        this.user = user;
    }

    public Track(Integer id, Invoice invoice, String action, User user, Instant created, Instant updated, boolean deleted) {
        this.id = id;
        this.invoice = invoice;
        this.action = action;
        this.user = user;
        this.created = created;
        this.updated = updated;
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", invoice=" + invoice +
                ", action='" + action + '\'' +
                ", user=" + user +
                ", created=" + created +
                ", updated=" + updated +
                ", deleted=" + deleted +
                '}';
    }
}
