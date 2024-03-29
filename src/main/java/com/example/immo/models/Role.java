package com.example.immo.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

/*
 * Roles_seq : When using @GeneratedValue(strategy=GenerationType.AUTO) in an entity class,
 * Spring Boot may create a sequence table to manage the generation of primary key values for entities.
 * This table stores the next value to be used for auto-incrementing primary keys.
 */
@Data
@Entity(name = "roles")
@Builder
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer roleId;

    private String authority;

    public Role() {
        super();
    }

    public Role(String authority) {
        this.authority = authority;
    }

    public Role(Integer roleId, String authority) {
        this.roleId = roleId;
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

}
