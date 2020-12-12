package com.udacity.jdnd.course3.critter.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.filter.Views;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="CUSTOMER")
public class Customer extends User {

    @JsonView(Views.Public.class)
    private String phoneNumber;

    @JsonView(Views.Public.class)
    @Column(length=5000)
    private String notes;

    @JsonView(Views.Internal.class)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets;
}
