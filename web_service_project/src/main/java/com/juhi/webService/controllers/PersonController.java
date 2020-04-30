package com.juhi.webService.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.juhi.webService.models.Person;
import com.juhi.webService.repositories.PersonRepository;

@RestController
@RequestMapping("api/v1/people")
@CrossOrigin(origins= "*")
public class PersonController {
	
	@Autowired
	private PersonRepository personRepository;
	
	@GetMapping
	public List<Person> List(){
		return personRepository.findAll();
	}
	
	@PostMapping("/create")
	//@ResponseStatus(HttpStatus.OK)
	public String create(@RequestBody Person person) {
		personRepository.save(person);
		return "The new person is created succesfully.";
	}
	@GetMapping("/{id}")
	public Person get(@PathVariable("id") long id) {
		return personRepository.getOne(id);
	}
	
	@DeleteMapping("/cancel/{id}")
	public List<Person> cancelPerson(@PathVariable int id) {
		personRepository.deleteById((long) id);;
		return personRepository.findAll();
	}
}
