package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exceptions.PetNotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeManagedRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.exceptions.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeManagedRepository employeeManagedRepository;

    @Autowired
    PetRepository petRepository;

    public Optional<Customer> findCustomer(Long id) {
        return customerRepository.findById(id);
    }

    @Transactional
    public Customer save(Customer c, List<Long> petIds) {
        c.getPets().clear();
        for (Long petId : petIds) {
            Pet p = petRepository.findById(petId).orElseThrow(() -> new PetNotFoundException("ID: " + petId));
            c.getPets().add(p);
        }

        return customerRepository.save(c);
    }

    @Transactional
    public Employee save(Employee e) {
        return employeeRepository.save(e);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Employee> findEmployee(Long id) throws EmployeeNotFoundException {
        return employeeRepository.findById(id);
    }

    public Customer findOwnerByPet(Long id) throws CustomerNotFoundException {
        return customerRepository.findOptionalByPetId(id).orElseThrow(() -> new EmployeeNotFoundException("ID: " + id));
    }

    public List<Employee> findAvailableEmployees(Set<EmployeeSkill> skills, LocalDate date) {
        // if there is more than one skill Hibernate does not support queries on @EnumeratedCollections
        // So get the ids of the employees with all skills and then pull just those employees from the database.
        List<Long> employeesIds = employeeManagedRepository.findEmployeeIdsWithAllSkillsOnDay(skills, date.getDayOfWeek());
        List<Employee> employees = employeeRepository.findAllById(employeesIds);
        return employees;
    }

    public List<Employee> findEmployees(List<Long> employeeIds) throws EmployeeNotFoundException {
        List<Employee> employees = employeeRepository.findAllById(employeeIds);

        if (employeeIds.size() != employees.size()) {
            List<Long> found = employees.stream().map(e -> e.getId()).collect(Collectors.toList());
            String missing = (String) employeeIds
                    .stream()
                    .filter( id -> !found.contains(id) )
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            throw new EmployeeNotFoundException("Could not find employee(s) with id(s): " + missing);
        }
        return employees;
    }
}
