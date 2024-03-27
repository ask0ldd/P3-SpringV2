package com.example.immo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "messages")
@Table(name = "messages")
// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "message_id")
    @Column(name = "id")
    private Integer messageId;

    @Column(name = "message", nullable = false, length = 2000)
    private String message;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date creation;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date update;

}
