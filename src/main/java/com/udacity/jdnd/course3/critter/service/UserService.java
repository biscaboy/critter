package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.service.exceptions.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.service.exceptions.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

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
}
