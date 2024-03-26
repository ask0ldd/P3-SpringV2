package com.example.immo.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "rentals")
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer rentalId;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @Column(name = "picture", length = 255)
    private String picture;

    @OneToMany(mappedBy = "rental")
    private List<Message> messages;

    @Column(name = "surface")
    private Integer surface;

    @Column(name = "price")
    private Integer price;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date creation;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date update;
}

// cascade all : if a rental row is linked to a user and this user hasn't been
// created yet then this user is autocreated
/*
 * @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, orphanRemoval =
 * true)
 * private List<Message> messages = new ArrayList<Message>();
 */