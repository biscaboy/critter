package com.udacity.jdnd.course3.critter.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.filter.Views;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
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
