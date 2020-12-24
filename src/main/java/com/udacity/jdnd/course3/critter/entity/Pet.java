package com.udacity.jdnd.course3.critter.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.filter.Views;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pet")
@Getter
@Setter
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Public.class)
    private Long id;

    @JsonView(Views.Public.class)
    private PetType type;

    @JsonView(Views.Public.class)
    private String name;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.TRUE)
    @JoinColumn(name="customer_id")
    @JsonIgnoreProperties("pets")
    @JsonBackReference
    @JsonView(Views.Public.class)
    private Customer owner;

    @JsonView(Views.Public.class)
    private LocalDate birthDate;

    @JsonView(Views.Public.class)
    @Column(length=5000)
    private String notes;

    @JsonView(Views.Internal.class)
    @ManyToMany(
            mappedBy = "pets")
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonManagedReference
    @JsonIgnoreProperties("schedules")
    private List<Schedule> schedules = new ArrayList<>();
}
