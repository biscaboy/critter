package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.User;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        // is the id null?
        Long id = Optional.ofNullable(customerDTO.getId()).orElse(Long.valueOf(-1));

        // get the customer if it exists
        Customer c = userService.findCustomer(id).orElse(new Customer());

        // copy user input to the existing customer
        // TODO validate data presences not to lose data?
        this.copyDTOtoCustomer(c, customerDTO);
        // save the merged customer and get the updated copy
        c = userService.save(c);
        // return the updated DTO
        return convertCustomerToDTO(c);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        // get all the customers
        List customers = userService.getAllCustomers();

        List dtos = new ArrayList<CustomerDTO>();
        // convert to DTO
        customers.forEach( c -> {
            dtos.add(this.convertCustomerToDTO((Customer)c));
        });
        return dtos;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        throw new UnsupportedOperationException();
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        throw new UnsupportedOperationException();
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        throw new UnsupportedOperationException();
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        throw new UnsupportedOperationException();
    }

    private CustomerDTO convertCustomerToDTO(Customer c){
        CustomerDTO dto = new CustomerDTO();
        BeanUtils.copyProperties(c, dto);
        Optional.ofNullable(c.getPets())
                .ifPresent( pets -> {
                    pets.forEach( pet -> {
                        dto.getPetIds().add(pet.getId());
                    });
                });
        return dto;
    }

    private Customer copyDTOtoCustomer(Customer c, CustomerDTO dto) {
        BeanUtils.copyProperties(c, dto); // TODO buggy? May step on something?
        Optional.ofNullable(dto.getPetIds())
                .ifPresent((List petIdsList) -> {
                    petIdsList.forEach( pId -> {
                        Pet p = new Pet(); // TODO replace these two lines with a call to service when it exists
                        p.setId((Long) pId);
                        c.getPets().add(p);
                    });
                });

        return c;
    }
}
