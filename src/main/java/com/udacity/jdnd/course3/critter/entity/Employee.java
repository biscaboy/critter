package com.udacity.jdnd.course3.critter.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.Set;

@Entity
@Data
@Table(name="EMPLOYEE")
public class Employee extends User {

    @ElementCollection
    @CollectionTable(
            name="EMPLOYEE_SKILL",
            joinColumns = @JoinColumn(name="ID"))//, uniqueConstraints = @UniqueConstraint(columnNames = {"ID", "SKILL"}))
    @Column(name="SKILL")
    private Set<EmployeeSkill> skills;

    @ElementCollection
    @CollectionTable(
            name="DAY_OF_WEEK",
            joinColumns = @JoinColumn(name="ID"))//, uniqueConstraints = @UniqueConstraint(columnNames = {"ID", "DAY"}))
    @Column(name="DAY")
    private Set<DayOfWeek> daysAvailable;
}
