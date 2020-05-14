package com.juhi.webService.models;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Table(name="customers",uniqueConstraints=
@UniqueConstraint(columnNames={"priority"})) 
public class Person<priority> {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name="firstName")
	private String firstName;
	@Column(name="lastName")
	private String lastName;
	@Column(name="priority")
	private int priority;
	@Column(name="team")
	private String team;
	@Column(name="field")
	private int field;
	@Column(name="level")
	private int level;
	private boolean ordered;
	

	@Column(name="startNumber")
	private int startNumber;
	
	public int getStartNumber() {
		return startNumber;
	}
	public void setStartNumber(int startNumber) {
		this.startNumber = startNumber;
	}

	private boolean prio;
	
	public int getField() {
		return field;
	}
	public void setField(int field) {
		this.field = field;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean isPrio() {
		return prio;
	}
	public void setPrio(boolean prio) {
		this.prio = prio;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public boolean isOrdered() {
		return ordered;
	}
	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}
	
	public Person(long id, String firstName, String lastName, int startNumber,int priority, String team) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.priority=priority;
		this.team=team;

	}
	
	public Person() {

	}
	
	 @SuppressWarnings("rawtypes")
	public static Comparator<Person>levelComperator=(Person o1, Person o2) -> {
	        int leve1=o1.getPriority();
	        int leve2=o2.getPriority();
	        return Integer.compare(leve1, leve2);
	    };

	   @SuppressWarnings("rawtypes")
	public static Comparator<Person>TeamComperator=(Person o1, Person o2) -> {
	       String TeamName1=o1.getTeam();
	       String TeamName2=o2.getTeam();
	       return TeamName1.compareTo(TeamName2);
	    };
	    @SuppressWarnings("rawtypes")
		public static Comparator<Person>priorityComperator=(Person o1, Person o2) -> {
	        int priority1=o1.getPriority();
	        int priority2=o2.getPriority();
	        return Integer.compare(priority1, priority2);
	    };
	    

	     @SuppressWarnings("rawtypes")
		public static Comparator<Person>fieldComperator1=(Person o1, Person o2) -> {
	         int field1=o1.getField();
	         int field2=o2.getField();
	         return Integer.compare(field1,field2);
	    };


}
