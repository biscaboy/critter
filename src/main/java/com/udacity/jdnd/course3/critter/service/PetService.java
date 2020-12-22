package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exceptions.EmployeeNotFoundException;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.exceptions.PetNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    public Optional<Pet> findPet(Long id) {
        return petRepository.findById(id);
    }

    public Pet save(Pet p) {
        return petRepository.save(p);
    }

    public List<Pet> findPetByOwner(Long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }

    public List<Pet> findPets(List<Long> petIds) {
        List<Pet> pets = petRepository.findAllById(petIds);

        // TODO Test this exception situation
        if (petIds.size() != pets.size()) {
            List<Long> found = pets.stream().map(p -> p.getId()).collect(Collectors.toList());
            String missing = petIds.stream().map(id -> {
                return (found.contains(id)) ? "" : id;
            }).collect(Collectors.toList()).toString();
            throw new PetNotFoundException("Could not find pet(s) with id(s): " + missing);
        }
        return pets;
    }

    public List<Pet> findAllPets() {
        return petRepository.findAll();
    }
}
