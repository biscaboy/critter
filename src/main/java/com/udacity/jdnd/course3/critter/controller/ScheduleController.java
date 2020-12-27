package com.udacity.jdnd.course3.critter.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.exceptions.*;
import com.udacity.jdnd.course3.critter.filter.Views;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private static final String []  PROPERTIES_TO_IGNORE_ON_COPY = { "id" };

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    PetService petService;

    @Autowired
    UserService userService;

    @Transactional
    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO)
            throws EmployeeNotFoundException, PetNotFoundException,
                   MissingParameterException, EmployeeNotAvaliableException {
        validateDTO(scheduleDTO);

        long scheduleId = Optional.ofNullable(scheduleDTO.getId()).orElse(-1L);

        Schedule s = scheduleService
                .findSchedule(Long.valueOf(scheduleId))
                .orElseGet(Schedule::new);

        s = copyDTOToSchedule(scheduleDTO, s);
        s.setEmployees(userService.findEmployees(scheduleDTO.getEmployeeIds()));
        s.setPets(petService.findPets(scheduleDTO.getPetIds()));

        s = scheduleService.save(s);

        // save the schedule to employees
        for (Employee employee : s.getEmployees()){
            employee.getSchedules().add(s);
            userService.save(employee);
        }

        // save the schedule to pets
        for (Pet pet : s.getPets()) {
            pet.getSchedules().add(s);
            petService.save(pet);
        }

        return copyScheduleToDTO(s);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.findAllSchedules();
        return copyScheduleToDTO(schedules);
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) throws PetNotFoundException {
        Pet p = petService.findPet(petId).orElseThrow(() -> new PetNotFoundException("ID: " + petId));
        return copyScheduleToDTO(p.getSchedules());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        Employee e = userService.findEmployee(employeeId);
        return copyScheduleToDTO(e.getSchedules());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) throws CustomerNotFoundException {
        Customer c = userService.findCustomer(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("ID: " + customerId));
        List<Schedule> customerSchedules = c.getPets()
                .stream()
                .map(Pet::getSchedules)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return copyScheduleToDTO(customerSchedules);
    }

    private Schedule copyDTOToSchedule(ScheduleDTO dto, Schedule s) {
        if (dto.getDate() != null) {
            s.setDate(dto.getDate());
        }
        if (dto.getActivities() != null && dto.getActivities().size() > 0) {
            s.setActivities(dto.getActivities());
        }
        return s;
    }

    private List<ScheduleDTO> copyScheduleToDTO(List<Schedule> schedules) {
        return schedules
                .stream()
                .map(s -> { return copyScheduleToDTO(s); })
                .collect(Collectors.toList());
    }

    private ScheduleDTO copyScheduleToDTO(Schedule s) {
        ScheduleDTO dto = new ScheduleDTO();
        BeanUtils.copyProperties(s, dto);
        s.getEmployees().forEach(employee -> {dto.getEmployeeIds().add(employee.getId());});
        s.getPets().forEach(pet -> {dto.getPetIds().add(pet.getId());});
        return dto;
    }

    private void validateDTO(ScheduleDTO dto) throws MissingParameterException {
        // TODO rewrite using reflection and reuse with other DTOs.
        String message = "Missing request parameter(s): ";
        int count = 0;
        if (dto.getDate() == null) {
            message += "service date";
            count++;
        }
        if (dto.getActivities() == null || dto.getActivities().size() == 0) {
            message = (count > 0) ? message + ", " : message;
            message += "activites";
            count++;
        }
        if (dto.getEmployeeIds() == null || dto.getEmployeeIds().size() == 0) {
            message = (count > 0) ? message + ", " : message;
            message += "Employee id(s)";
            count++;
        }
        if (dto.getPetIds() == null || dto.getPetIds().size() == 0) {
            message = (count > 0) ? message + ", " : message;
            message += "Pet id(s)";
            count++;

        }
        if (count > 0) {
            throw new MissingParameterException(message + ".");
        }
    }
}
