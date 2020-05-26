package com.juhi.webService.controllers;
import java.util.LinkedList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.juhi.webService.models.Person;
import com.juhi.webService.repositories.PersonRepository;

@RestController
@RequestMapping("api/v1/people")
@CrossOrigin(origins= "*")
public class PersonController {
		
	@Autowired
	private PersonRepository personRepository;
	
	
	@Autowired
	private ListService ListService;
	
	@SuppressWarnings("rawtypes")
	@GetMapping
	public List<Person> List(@RequestParam(value="priority", defaultValue="0") int priority,
			@RequestParam(value="limit",defaultValue="5000") int limit,
			@RequestParam(value="sort",defaultValue="asc", required=false) String sort){
		
		return ListService.getAllPeople();
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/create")
	//@ResponseStatus(HttpStatus.OK)
	public String create(@RequestBody Person person) {
		

			this.ListService.clearLists();
			personRepository.save(person);
			return "A versenyző sikeresen hozzá lett adva a futamhoz!";
					
		
				
	}
	@SuppressWarnings("rawtypes")
	@GetMapping("/{id}")
	public Person get(@PathVariable("id") long id) {
		return personRepository.getOne(id);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/cancel/{id}")
	public List<Person> cancelPerson(@PathVariable int id) {
		personRepository.deleteById((long) id);;
		return personRepository.findAll();
	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping("/cancel/all")
	public List<Person> cancelPerson() {
		personRepository.deleteAll();
		return personRepository.findAll();
	}
}
