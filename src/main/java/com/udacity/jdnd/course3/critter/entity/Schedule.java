package com.udacity.jdnd.course3.critter.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.filter.Views;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Public.class)
    private Long id;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.TRUE)
    @JoinTable(
            name = "schedule_employee",
            joinColumns = { @JoinColumn(name = "schedule_id")},
            inverseJoinColumns = { @JoinColumn(name = "employee_id")}
    )
    @JsonBackReference
    @JsonIgnoreProperties("schedules")
    @JsonView(Views.Public.class)
    private List<Employee> employees;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.TRUE)
    @JoinTable(
            name = "schedule_pet",
            joinColumns = { @JoinColumn(name = "schedule_id")},
            inverseJoinColumns = { @JoinColumn(name = "pet_id")}
    )
    @JsonBackReference
    @JsonIgnoreProperties("schedules")
    @JsonView(Views.Public.class)
    private List<Pet> pets;

    @JsonView(Views.Public.class)
    private LocalDate date;

    @ElementCollection
    @CollectionTable(
            name="schedule_activities",
            joinColumns = @JoinColumn(name="id"))//, uniqueConstraints = @UniqueConstraint(columnNames = {"ID", "SKILL"}))
    @Column(name="activities")
    @JsonView(Views.Public.class)
    private Set<EmployeeSkill> activities;



}
