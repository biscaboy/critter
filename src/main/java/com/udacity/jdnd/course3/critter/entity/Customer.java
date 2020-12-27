package com.udacity.jdnd.course3.critter.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.filter.Views;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="customer")
public class Customer extends User {

    private String phoneNumber;

    @Column(length=5000)
    private String notes;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnoreProperties("owner")
    @JsonManagedReference
    private List<Pet> pets = new ArrayList<>();
}
