package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.exceptions.PetNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    public Pet findPet(Long id) {
        return petRepository.findById(id).orElseThrow(PetNotFoundException::new);
    }

    public Pet save(Pet p) {
        return petRepository.save(p);
    }

    public List<Pet> findPetByOwner(Long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }
}
