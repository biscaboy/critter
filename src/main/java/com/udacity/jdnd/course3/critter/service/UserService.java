package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeManagedRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.exceptions.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeManagedRepository employeeManagedRepository;

    public Customer findCustomer(Long id) throws CustomerNotFoundException {
        return customerRepository.findById(id).orElseThrow(CustomerNotFoundException::new);
    }

    public Customer save(Customer c) {
        return customerRepository.save(c);
    }

    public Employee save(Employee e) {
        return employeeRepository.save(e);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Employee findEmployee(Long id) throws EmployeeNotFoundException {
        return employeeRepository.findById(id).orElseThrow(EmployeeNotFoundException::new);
    }

    public Customer findOwnerByPet(Long id) throws CustomerNotFoundException {
        return customerRepository.findOptionalByPetId(id).orElseThrow(CustomerNotFoundException::new);
    }

    public List<Employee> findEmployeesBySkill(Set<EmployeeSkill> skills) {
        // if there is more than one skill Hibernate does not support queries on @EnumeratedCollections
        // So get the ids of the employees with all skills and then pull just those employees from the database.
        if (skills.size() > 1){
            List<Long> employeesIdsWithAllSkills = employeeManagedRepository.findEmployeeWithAllSkills(skills);
            return employeeRepository.findAllById(employeesIdsWithAllSkills);
        } else {
            return employeeRepository.findBySkillsIn(skills);
        }
    }
}
