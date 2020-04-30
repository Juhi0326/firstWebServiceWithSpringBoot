package com.juhi.webService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juhi.webService.models.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{

}
