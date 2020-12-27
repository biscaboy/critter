package com.udacity.jdnd.course3.critter;

import com.google.common.collect.Sets;
import com.udacity.jdnd.course3.critter.controller.UserController;
import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.PetType;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.PetNotFoundException;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = CritterApplication.class)
public class CritterAdditionalTests {

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;

    @Test
    @DisplayName("Test PetNotFoundException message")
    public void testPetNotFoundException (){
        Long nonExistingId = 1000L;
        String expectedMessage = "Could not find pet(s) with id(s): " + nonExistingId;
        String actualMessage = null;
        Pet pet = createPet("Figaro", PetType.CAT);
        pet = petService.save(pet);
        List<Long> idList = new ArrayList<>();
        idList.add(pet.getId());
        idList.add(nonExistingId);

        Assertions.assertThrows(PetNotFoundException.class, () -> {
            petService.findPets(idList);
        });
        // one missing id
        try {
            petService.findPets(idList);
        } catch (PetNotFoundException petNotFoundException){
            actualMessage = petNotFoundException.getMessage();
        }
        Assertions.assertEquals(expectedMessage, actualMessage);

        // two missing ids
        nonExistingId++;
        expectedMessage += ", " + nonExistingId;
        idList.add(nonExistingId);
        try {
            petService.findPets(idList);
        } catch (PetNotFoundException petNotFoundException){
            actualMessage = petNotFoundException.getMessage();
        }
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Test EmployeeNotFoundException message")
    public void testEmployeeNotFoundExceptionMessage (){
        Long nonExistingId = 1000L;
        String expectedMessage = "Could not find employee(s) with id(s): " + nonExistingId;
        String actualMessage = null;
        Employee employee = createEmployee("Joe");
        employee = userService.save(employee);
        List<Long> idList = new ArrayList<>();
        idList.add(employee.getId());
        idList.add(nonExistingId);

        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            userService.findEmployees(idList);
        });
        // one missing id
        try {
            userService.findEmployees(idList);
        } catch (EmployeeNotFoundException employeeNotFoundException){
            actualMessage = employeeNotFoundException.getMessage();
        }
        Assertions.assertEquals(expectedMessage, actualMessage);

        // two missing ids
        nonExistingId++;
        expectedMessage += ", " + nonExistingId;
        idList.add(nonExistingId);
        try {
            userService.findEmployees(idList);
        } catch (EmployeeNotFoundException employeeNotFoundException){
            actualMessage = employeeNotFoundException.getMessage();
        }
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    private static Pet createPet(String name, PetType type) {
        Pet pet = new Pet();
        pet.setName(name);
        pet.setType(type);
        return pet;
    }

    private static Employee createEmployee(String name) {
        Employee employee = new Employee();
        employee.setName(name);
        return employee;
    }

    private static EmployeeDTO createEmployeeDTO() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("TestEmployee");
        employeeDTO.setSkills(Sets.newHashSet(EmployeeSkill.FEEDING, EmployeeSkill.PETTING));
        return employeeDTO;
    }

}