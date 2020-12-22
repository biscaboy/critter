package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.exceptions.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.PetNotFoundException;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) throws EmployeeNotFoundException, PetNotFoundException {
        long scheduleId = Optional.ofNullable(scheduleDTO.getId()).orElse(-1L);

        Schedule s = scheduleService
                .findSchedule(Long.valueOf(scheduleId))
                .orElseGet(Schedule::new);

        s = copyDTOToSchedule(scheduleDTO, s);

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
        Pet p = petService.findPet(petId).orElseThrow(PetNotFoundException::new);
        return copyScheduleToDTO(p.getSchedules());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        Employee e = userService.findEmployee(employeeId);
        return copyScheduleToDTO(e.getSchedules());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) throws CustomerNotFoundException {
        Customer c = userService.findCustomer(customerId).orElseThrow(CustomerNotFoundException::new);
        List<Schedule> customerSchedules = c.getPets()
                .stream()
                .map(Pet::getSchedules)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return copyScheduleToDTO(customerSchedules);
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

    private Schedule copyDTOToSchedule(ScheduleDTO dto, Schedule s) throws EmployeeNotFoundException, PetNotFoundException {
        BeanUtils.copyProperties(dto, s);
        s.setEmployees(userService.findEmployees(dto.getEmployeeIds()));
        s.setPets(petService.findPets(dto.getPetIds()));
        return s;
    }
}
