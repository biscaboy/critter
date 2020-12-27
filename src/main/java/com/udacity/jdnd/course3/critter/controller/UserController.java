package com.udacity.jdnd.course3.critter.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exceptions.MissingParameterException;
import com.udacity.jdnd.course3.critter.exceptions.PetNotFoundException;
import com.udacity.jdnd.course3.critter.filter.Views;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.UserService;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final String []  PROPERTIES_TO_IGNORE_ON_COPY = { "id" };

    private UserService userService;

    private PetService petService;

    public UserController(UserService userService, PetService petService) {
        this.userService = userService;
        this.petService = petService;
    }

    @Transactional
    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){

        // TODO - if this is an update handle it.  Updates currently throwing errors and returning status code 500
        // TODO - the bean utils copy is stepping on values if the dto is missing values.  Validate before copy.
        Long id = Optional.ofNullable(customerDTO.getId()).orElse(Long.valueOf(-1));
        Customer c = userService.findCustomer(id).orElseGet(Customer::new);
        BeanUtils.copyProperties(customerDTO, c, PROPERTIES_TO_IGNORE_ON_COPY);
        List<Long> petIds = Optional.ofNullable(customerDTO.getPetIds()).orElseGet(ArrayList::new);
        c.setPets(petIds.stream().map((petId) -> {
                return petService.findPet(petId)
                    .orElseThrow(() -> new PetNotFoundException("ID: " + petId));
            }).collect(Collectors.toList()));
        c = userService.save(c);
        return copyCustomerToDTO(c);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = userService.getAllCustomers();
        return copyCustomersToDTOs(customers);
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) throws PetNotFoundException{
        Pet p = petService.findPet(petId).orElseThrow(() -> new PetNotFoundException("ID: " + petId));
        return copyCustomerToDTO(p.getOwner());
    }

    @Transactional
    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        // get the customer if it exists
        Employee e = null;
        try {
            e = userService.findEmployee(employeeDTO.getId());
        } catch (EmployeeNotFoundException exception) {
            e = new Employee();
        }
        // copy user input to the existing customer
        BeanUtils.copyProperties(employeeDTO, e, PROPERTIES_TO_IGNORE_ON_COPY);
        // save the merged customer and get the updated copy
        e = userService.save(e);
        // return the updated DTO
        return copyEmployeeToDTO(e);
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) throws EmployeeNotFoundException {
        // is the id null?
        Long id = Optional.ofNullable(employeeId).orElse(Long.valueOf(-1));
        Employee e = userService.findEmployee(id);
        return copyEmployeeToDTO(e);
    }

    @Transactional
    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) throws EmployeeNotFoundException {
        Employee e = userService.findEmployee(employeeId);
        e.setDaysAvailable(daysAvailable);
        userService.save(e);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTO) throws MissingParameterException {
        // got skills?
        Set<EmployeeSkill> skills = Optional.ofNullable(employeeRequestDTO.getSkills())
                .orElseThrow(() -> new MissingParameterException("Request is missing employee skills required."));
        LocalDate date = Optional.ofNullable(employeeRequestDTO.getDate())
                .orElseThrow(() -> new MissingParameterException("Request is missing the date."));
        List<Employee> employees = userService.findAvailableEmployees(skills, date);
        return employees.stream().map(this::copyEmployeeToDTO).collect(Collectors.toList());
    }

    private EmployeeDTO copyEmployeeToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(employee, dto);
        return dto;
    }

    private CustomerDTO copyCustomerToDTO(Customer c){
        CustomerDTO dto = new CustomerDTO();
        BeanUtils.copyProperties(c, dto);
        c.getPets().forEach( pet -> {
            dto.getPetIds().add(pet.getId());
        });
        return dto;
    }

    private List<CustomerDTO> copyCustomersToDTOs (List<Customer> customers) {
        List dtos = new ArrayList<CustomerDTO>();
        // convert to DTO
        customers.forEach( c -> {
            dtos.add(this.copyCustomerToDTO((Customer)c));
        });
        return dtos;
    }

}
