package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotAvaliableException;
import com.udacity.jdnd.course3.critter.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    EmployeeManagedRepository employeeRepository;

    public Optional<Schedule> findSchedule(Long id) {
        return scheduleRepository.findById(id);
    }

    public Schedule save(Schedule s) {
        // validate the employee has the skills and is available for the date given
        List<Long> employeeIds = employeeRepository.findEmployeeIdsWithAllSkillsOnDay(s.getActivities(), s.getDate().getDayOfWeek());

        for (Employee e : s.getEmployees()) {
            if (!employeeIds.contains(e.getId())){
                throw new EmployeeNotAvaliableException();
            }
        }

        return scheduleRepository.save(s);
    }

    public List<Schedule> findAllSchedules() {
        return scheduleRepository.findAll();
    }

}
