package com.service.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.service.model.Pet;

@Repository
public class PetDAO {
	private static HashMap<String, Pet> petMap = new HashMap<>();
	
	static {
		initPet();
	}
	private static void initPet() {
		Pet pet1 = new Pet("kiwiwi", "cat", 1);
		Pet pet2 = new Pet("alaska", "dog", 2);
		Pet pet3 = new Pet("pulldog", "dog", 1);
		petMap.clear();
		petMap.put(pet1.getName(), pet1);
		petMap.put(pet2.getName(), pet2);
		petMap.put(pet3.getName(), pet3);
		
	}
	public Pet getPet(String empNo) {
        return petMap.get(empNo);
    }
 
    public Pet addPet(Pet pet) {
    	petMap.put(pet.getName(), pet);
        return pet;
    }
 
    public Pet updatePet(Pet pet) {
    	petMap.put(pet.getName(), pet);
        return pet;
    }
 
    public void deletePet(String petName) {
    	petMap.remove(petName);
    }
    public List<Pet> getAllPet(){
    	Collection<Pet> c = petMap.values();
        List<Pet> list = new ArrayList<Pet>();
        list.addAll(c);
        return list;
    }
}
