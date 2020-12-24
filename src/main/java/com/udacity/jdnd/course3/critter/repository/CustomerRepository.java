package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = "SELECT * FROM Customer AS c INNER JOIN User AS u ON u.id = c.id INNER JOIN Pet AS p ON c.id = p.owner_id where p.id = :id", nativeQuery = true)
    Optional<Customer> findOptionalByPetId(@Param("id") Long id);
}
