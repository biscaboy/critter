package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findBySkillsIn(Set<EmployeeSkill> skills);
}
