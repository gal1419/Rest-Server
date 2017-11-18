package com.app.module;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="user")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Id;

    @NotEmpty
    @Column(name = "first_name")
    private String FirstName;

    @NotEmpty
    @Column(name = "last_name")
    private String LastName;

    @NotEmpty
    @Email
    @Column(name="email", unique = true)
    private String email;

    @NotEmpty
    @Column(name = "password")
    private String password;


    @ManyToMany(fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="event_user",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="event_id")
    )
    private List<Event> events;



    public ApplicationUser() {
    }

    public ApplicationUser(String firstName, String lastName, String email) {
        FirstName = firstName;
        LastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> event) {
        this.events = event;
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    @Override
    public String toString() {
        return "ApplicationUser{" +
                "Id=" + Id +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", email='" + email + '\'' +
                ", events=" + events +
                '}';
    }
}
