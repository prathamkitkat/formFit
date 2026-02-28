package com.pm.formfitbackend.user.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "ux_user_username",
                        columnNames = "username"
                ),
                @UniqueConstraint(
                        name = "ux_user_email",
                        columnNames = "email"
                )
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", sequenceName = "user_id_seq", allocationSize = 50)
    private Long id;

    @Column(nullable = false,length = 50)
    private String username;

    @Column(nullable = false,length =  100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "weight_unit", nullable = false, length = 5)
    private String weightUnit; //KG OR LB

    @Column(nullable = false)
    private String timezone;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }



}
