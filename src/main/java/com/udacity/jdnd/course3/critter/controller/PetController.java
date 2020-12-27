package com.udacity.jdnd.course3.critter.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.dto.PetDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exceptions.MissingParameterException;
import com.udacity.jdnd.course3.critter.filter.Views;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.UserService;
import com.udacity.jdnd.course3.critter.exceptions.CustomerNotFoundException;
import com.udacity.jdnd.course3.critter.exceptions.PetNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private static final String []  PROPERTIES_TO_IGNORE_ON_COPY = { "id" };

    @Autowired
    PetService petService;

    @Autowired
    UserService userService;

    @Transactional
    @PostMapping("/{ownerId}")
    public PetDTO updatePet(@PathVariable(name="ownerId") Long ownerId, @RequestBody PetDTO petDTO){
        petDTO.setOwnerId(ownerId);
        return savePet(petDTO);
    }

    @Transactional
    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) throws CustomerNotFoundException, MissingParameterException {
        // is the id null?
        long petId = Optional.ofNullable(petDTO.getId()).orElse(-1L);
        Long ownerId = Optional.ofNullable(petDTO.getOwnerId())
                .orElseThrow(() -> new MissingParameterException("Owner ID missing."));

        // get the pet if it exists
        Pet p = petService.findPet(Long.valueOf(petId)).orElseGet(Pet::new);

        // copy user input to the existing pet
        BeanUtils.copyProperties(petDTO, p, PROPERTIES_TO_IGNORE_ON_COPY);

        // get the owner ids
        Customer owner = userService.findCustomer(ownerId)
                .orElseThrow(() -> new CustomerNotFoundException("ID: " + ownerId));
        p.setOwner(owner);
        // save the merged customer and get the updated copy
        p = petService.save(p);

        // update the owner
        owner.getPets().add(p);
        userService.save(owner);

        // return the updated DTO
        PetDTO dto = new PetDTO();
        BeanUtils.copyProperties(p, dto);
        dto.setOwnerId(p.getOwner().getId());
        return dto;
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) throws PetNotFoundException {
        PetDTO dto = new PetDTO();
        Pet p = petService.findPet(petId).orElseThrow(() -> new PetNotFoundException("ID: " + petId));
        BeanUtils.copyProperties(p, dto);
        dto.setOwnerId(p.getOwner().getId());
        return dto;
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.findAllPets();
        return copyPetsToPetsDTO(pets);
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = petService.findPetByOwner(Long.valueOf(ownerId));
        return copyPetsToPetsDTO(pets);
    }

    private List<PetDTO> copyPetsToPetsDTO(List<Pet> pets) {
        List<PetDTO> dtos = new ArrayList<>();
        pets.forEach(pet -> {
            dtos.add(new PetDTO(
                    pet.getId().longValue(),
                    pet.getType(),
                    pet.getName(),
                    pet.getOwner().getId().longValue(),
                    pet.getBirthDate(),
                    pet.getNotes()));
        });

        return dtos;
    }
}
