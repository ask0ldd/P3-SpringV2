package com.example.immo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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
    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    @Column(name = "description", nullable = false, length = 2000)
    @Size(min = 2, message = "Description must be at least 2 characters long")
    private String description;

    @Column(name = "picture", length = 255)
    private String picture;

    @OneToMany(mappedBy = "rental")
    private List<Message> messages;

    @Column(name = "surface")
    @Min(value = 0, message = "Surface must be at least 0")
    @Max(value = 1000, message = "Surface must not exceed 1000")
    private Integer surface;

    @Column(name = "price")
    @Min(value = 0, message = "Price must be at least 0")
    @Max(value = 10000, message = "Price must not exceed 10,000")
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