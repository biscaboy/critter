package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exceptions.MissingParameterException;
import com.udacity.jdnd.course3.critter.exceptions.PetNotFoundException;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.UserService;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        Long id = Optional.ofNullable(customerDTO.getId()).orElse(Long.valueOf(-1));
        Customer c = userService.findCustomer(id).orElseGet(Customer::new);
        this.copyDTOtoCustomer(customerDTO, c);
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
        Pet p = petService.findPet(petId).orElseThrow(PetNotFoundException::new);
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
        // TODO validate data presences not to lose data?
        BeanUtils.copyProperties(employeeDTO, e, PROPERTIES_TO_IGNORE_ON_COPY);
        // save the merged customer and get the updated copy
        e = userService.save(e);
        // return the updated DTO
        return copyEmployeeToDTO(e);
    }

    @PostMapping("/employee/{employeeId}")
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
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) throws MissingParameterException {
        // got skills?
        Set<EmployeeSkill> skills = Optional.ofNullable(employeeDTO.getSkills()).orElseThrow(() -> new MissingParameterException("Employee skills missing."));
        List<Employee> employees = userService.findEmployeesBySkill(skills);
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

    private Customer copyDTOtoCustomer(CustomerDTO dto, Customer c) {
        BeanUtils.copyProperties(dto, c, PROPERTIES_TO_IGNORE_ON_COPY);
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
