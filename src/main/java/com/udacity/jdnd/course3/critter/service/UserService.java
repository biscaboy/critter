package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.User;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    CustomerRepository customerRepository;

    public Optional<Customer> findCustomer(Long id) {
        return customerRepository.findById(id);
    }

    public Customer save(Customer c) {
        return customerRepository.save(c);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
