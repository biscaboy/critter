package com.udacity.jdnd.course3.critter.entity;

import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "PET")
@Data
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private PetType type;
    @Nationalized
    private String name;
    @ManyToOne
    private Customer owner;
    private LocalDate birthDate;
    @Column(length=5000)
    private String notes;
}
