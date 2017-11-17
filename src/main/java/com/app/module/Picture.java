package com.app.module;

import javax.persistence.*;

@Entity
@Table(name="picture")
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Id;

    @Column(name = "location")
    private String location;

    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="user_owner_id")
    private User owner;


    public Picture() {
    }

    public Picture(String location, User owner) {
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
