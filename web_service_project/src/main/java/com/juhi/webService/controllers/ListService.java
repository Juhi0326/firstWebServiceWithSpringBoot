package com.juhi.webService.controllers;

import java.util.LinkedList;
import java.util.List;
import com.juhi.webService.models.Person;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.juhi.webService.repositories.PersonRepository;

@Service
public class ListService {


	@SuppressWarnings("rawtypes")
	
	private List<Integer> list1 = new LinkedList<>();
    private  List<Integer> list2 = new LinkedList<>();
    private List<Integer> priorityList = new LinkedList<>();
    private List<Integer> levelList = new LinkedList<>();
	@SuppressWarnings("rawtypes")
	private List<Person> people = new LinkedList<>();
	@SuppressWarnings("rawtypes")
	private List<Person> teamList = new LinkedList();
    @SuppressWarnings("rawtypes")
	private List<Person> finalPeople = new LinkedList();
    @SuppressWarnings("rawtypes")
	private List<Person> tempList = new LinkedList();
    
    private int numberOfField;
    private int tempField;
    private int counter = 0;
    private int number;
    /* a "kapcsolo" változó azért lett bevezetve, mert egyszer az egyik 
    oldalra, egyszer a másik oldalra kell pakolnom a kajakosokat*/
    private boolean kapcsolo = false;
    /* a "probalkozas" változó azért lett bevezetve, hogy limitálja a 
    azt a próbálkozást,hogy olyan kajakost keressen a mellette lévő pályára
    , aki más csapathoz tartozik. így kerülhető el egy végtelen ciklus.*/      
    private int probalkozas=0;
    private int teamlist = 0;
    private int sorszam = 1;
    private int fele;
    private boolean paros;
	

	
	@Autowired
	private PersonRepository personRepository;
	

	@SuppressWarnings("rawtypes")
	private List<Person> ListOfPerson(){
		this.people=this.personRepository.findAll();
		return this.orderFields();
	}
	
	@SuppressWarnings("rawtypes")
	public List<Person> getAllPeople() {
		return this.ListOfPerson();	
	}
	
	public long tableNumber() {
		return this.personRepository.count();
	}
	
	public List<Integer> getPrioList(long l) {

        //előszőr megvizsgálom, hogy a kapott szám páros-e
        paros = l % 2 == 0;

        //itt megkapm a felét, és belekényszerítem egy int-be
        fele = (int) l / 2;

        if (paros == false) {
            //ha páratlan a szám, akkor a legkedvezőbb pálya a szám fele+1 lesz,
            //ezt már most beleteszem a priority listába.
            priorityList.add(fele + 1);
        }
        
        //itt két listába teszem a számokat, és ezeket fogom majd a megfelelő
        //módon összerendezni egy listába, és ezzel a listával térek vissza
        
        for (int i = fele; i >= 1; i--) {
            list1.add(i);
        }

        
        //a második lista kezdete attól függ, hogy páratlan volt-e a szám
        //mert ha igen akkor ki kell hagynom egy számot, azaz a fele+1-ről 
        //indul.
        if (paros == false) {
            for (int i = fele + 2; i < l + 1; i++) {
                list2.add(i);
            }
        } else {
            for (int i = fele + 1; i < l + 1; i++) {
                list2.add(i);
            }
        }
        for (int i = 0; i < fele; i++) {
            if (list1.get(i) != null) {
                priorityList.add(list1.get(i));
            }

            if (list2.get(i) != null) {
                priorityList.add(list2.get(i));
            }
        }

        return priorityList;
    }
	
	public List<Integer> getLevel() {
        if (paros == false) {
        	levelList.add(1);
         for (int i = 1; i <((int)priorityList.size()/2)+1; i++) {
        	 levelList.add(i+1);
        	 levelList.add(i+1);
		} 
        }
        else {
                    for (int i = 0; i < (priorityList.size()/2); i++) {
                    	levelList.add(i+1);
                    	levelList.add(i+1);
		}
        }
        return levelList;} 
	
	
	public  List<Person>orderFields() {
		people.sort(Person.priorityComperator);
		priorityList=getPrioList(tableNumber());
		numberOfField=priorityList.size();
		levelList=getLevel();
		
        // innen kezdem a két fő lista szétválogatását (kiemeltek, nem kiemeltek)

        for (Person person : people) {
            //ha a priority 100, akkor nem kiemelt, és megy egy sima listába
            if (person.getPriority() == 100) {
                teamList.add(person);
            } else {
                //különben kiemelt, és megy a kiemeltes listába
                finalPeople.add(person);
            }
        }
		
		return people;
	}
	
	
	
	public void clearLists() {
		
		this.list1.clear();
		this.list2.clear();
		this.priorityList.clear();
		this.levelList.clear();
	}
	

}
