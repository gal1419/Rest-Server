package com.app.module;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="picture")
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Id;

    @NotEmpty
    @Column(name = "location")
    private String location;

    @NotNull
    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="user_owner_id")
    private ApplicationUser owner;


    public Picture() {
    }

    public Picture(String location, ApplicationUser owner) {
        this.location = location;
        this.owner = owner;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ApplicationUser getOwner() {
        return owner;
    }

    public void setOwner(ApplicationUser owner) {
        this.owner = owner;
    }
}
