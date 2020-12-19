package com.udacity.jdnd.course3.critter.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.Set;

@Entity
@Data
@Table(name="employee")
public class Employee extends User {

    @ElementCollection
    @CollectionTable(
            name="employee_skill",
            joinColumns = @JoinColumn(name="id"))//, uniqueConstraints = @UniqueConstraint(columnNames = {"ID", "SKILL"}))
    @Column(name="skill")
    private Set<EmployeeSkill> skills;

    @ElementCollection
    @CollectionTable(
            name="day_of_week",
            joinColumns = @JoinColumn(name="id"))//, uniqueConstraints = @UniqueConstraint(columnNames = {"ID", "DAY"}))
    @Column(name="day")
    private Set<DayOfWeek> daysAvailable;
}
