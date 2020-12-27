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
    private Long id;

    private PetType type;

    private String name;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.TRUE)
    @JoinColumn(name="customer_id")
    @JsonIgnoreProperties("pets")
    @JsonBackReference
    private Customer owner;

    private LocalDate birthDate;

    @Column(length=5000)
    private String notes;

    @ManyToMany(
            mappedBy = "pets")
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonManagedReference
    @JsonIgnoreProperties("schedules")
    private List<Schedule> schedules = new ArrayList<>();
}
