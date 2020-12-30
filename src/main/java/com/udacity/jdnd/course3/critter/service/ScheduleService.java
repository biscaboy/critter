package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.exceptions.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotAvaliableException;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.PetNotFoundException;
import com.udacity.jdnd.course3.critter.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    UserService userService;

    @Autowired
    PetService petService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmployeeManagedRepository employeeManagedRepository;

    public Optional<Schedule> findSchedule(Long id) {
        return scheduleRepository.findById(id);
    }

    public List<Schedule> findAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Transactional
    public Schedule save(Schedule s, List<Long> employeeIds, List<Long> petIds)
            throws PetNotFoundException, EmployeeNotAvaliableException, EmployeeNotFoundException {

        // Validate and populate
        // populate the schedule with Pets and Employees
        // use the service calls as these services validate if all ids are found.
        s.setEmployees(userService.findEmployees(employeeIds));
        s.setPets(petService.findPets(petIds));

        // TODO Get information from mentors why this type of validation cannot be performed to include in submission.
//        // validate the employee has the skills and is available for the date given
//        List<Long> availableEmployeeIds = employeeManagedRepository.findEmployeeIdsWithAllSkillsOnDay(s.getActivities(), s.getDate().getDayOfWeek());
//        for (Employee e : s.getEmployees()) {
//            if (!availableEmployeeIds.contains(e.getId())){
//                throw new EmployeeNotAvaliableException();
//            }
//        }

        s = scheduleRepository.save(s);

        // save the schedule to employees
        for (Employee employee : s.getEmployees()){
            employee.getSchedules().add(s);
            employeeRepository.save(employee);
        }

        // save the schedule to pets
        for (Pet pet : s.getPets()) {
            pet.getSchedules().add(s);
            petRepository.save(pet);
        }

        return s;
    }

    public List<Schedule> findSchedulesForPet(long petId) {
        Pet p = petRepository.findById(petId).orElseThrow(() -> new PetNotFoundException("ID: " + petId));
        return p.getSchedules();
    }

    public List<Schedule> findSchedulesForEmployee(long employeeId) {
        Employee e = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("ID: " + employeeId));
        return e.getSchedules();
    }

    public List<Schedule> findSchedulesForCustomer(long customerId) {
        Customer c = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("ID: " + customerId));
        List<Schedule> customerSchedules = c.getPets()
                .stream()
                .map(Pet::getSchedules)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return customerSchedules;
    }
}
