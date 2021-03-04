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
public class User{

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )

    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    @ManyToOne
    @JoinColumn(name="role_id")
    @JsonIgnore
    private Role role;

    @CreationTimestamp
    @Column(name="created", columnDefinition="datetime DEFAULT CURRENT_TIMESTAMP")
    private Instant created;

    @UpdateTimestamp
    @Column(name = "updated", columnDefinition ="datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Instant updated;

    @Column(name="deleted", columnDefinition = "tinyint(1) NOT NULL DEFAULT '0'")
    private boolean deleted = false;

}
