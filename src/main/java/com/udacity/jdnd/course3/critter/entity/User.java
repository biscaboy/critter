package com.udacity.jdnd.course3.critter.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.filter.Views;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Entity
@Table(name = "USER")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Public.class)      // TODO: Is this JsonView necessary??
    private Long id;

    @JsonView(Views.Public.class)      // TODO: Is this JsonView necessary??
    @Nationalized
    @Column(length=500)
    private String name;
}