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
	private List<Integer> list2 = new LinkedList<>();
	private List<Integer> priorityList = new LinkedList<>();
	private List<Integer> levelList = new LinkedList<>();
	@SuppressWarnings("rawtypes")
	private List<Person> people = new LinkedList<>();
	@SuppressWarnings("rawtypes")
	private List<Person> teamList = new LinkedList();
	@SuppressWarnings("rawtypes")
	private List<Person> finalPeople = new LinkedList();

	private int numberOfField;
	private int tempField;
	private int counter = 0;
	private int number;
	/*
	 * a "kapcsolo" változó azért lett bevezetve, mert egyszer az egyik oldalra,
	 * egyszer a másik oldalra kell pakolnom a kajakosokat
	 */
	private boolean kapcsolo = false;
	/*
	 * a "probalkozas" változó azért lett bevezetve, hogy limitálja a azt a
	 * próbálkozást,hogy olyan kajakost keressen a mellette lévő pályára , aki más
	 * csapathoz tartozik. így kerülhető el egy végtelen ciklus.
	 */
	private int probalkozas = 0;
	private int teamlist = 0;
	private int sorszam = 1;
	private int fele;
	private boolean paros;

	@Autowired
	private PersonRepository personRepository;

	@SuppressWarnings("rawtypes")
	private List<Person> ListOfPerson() {
		clearLists();
		this.people = this.personRepository.findAll();
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

		// előszőr megvizsgálom, hogy a kapott szám páros-e
		paros = l % 2 == 0;

		// itt megkapm a felét, és belekényszerítem egy int-be
		fele = (int) l / 2;

		if (paros == false) {
			// ha páratlan a szám, akkor a legkedvezőbb pálya a szám fele+1 lesz,
			// ezt már most beleteszem a priority listába.
			priorityList.add(fele + 1);
		}
		// itt két listába teszem a számokat, és ezeket fogom majd a megfelelő
		// módon összerendezni egy listába, és ezzel a listával térek vissza

		for (int i = fele; i >= 1; i--) {
			list1.add(i);
		}

		// a második lista kezdete attól függ, hogy páratlan volt-e a szám
		// mert ha igen akkor ki kell hagynom egy számot, azaz a fele+1-ről
		// indul.
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
			for (int i = 1; i < ((int) priorityList.size() / 2) + 1; i++) {
				levelList.add(i + 1);
				levelList.add(i + 1);
			}
		} else {
			for (int i = 0; i < (priorityList.size() / 2); i++) {
				levelList.add(i + 1);
				levelList.add(i + 1);
			}
		}
		return levelList;
	}

	@SuppressWarnings("rawtypes")
	public List<Person> orderFields() {
		people.sort(Person.priorityComperator);

		priorityList = getPrioList(tableNumber());
		numberOfField = priorityList.size();
		levelList = getLevel();

		// innen kezdem a két fő lista szétválogatását (kiemeltek, nem kiemeltek)

		for (@SuppressWarnings("rawtypes")
		Person person : people) {
			// ha a priority 100, akkor nem kiemelt, és megy egy sima listába
			if (person.getPriority() == 100) {
				teamList.add(person);
			} else {
				// különben kiemelt, és megy a kiemeltes listába
				finalPeople.add(person);
			}
		}

		// ##########################################################################
		// Innen kezdem a kiemeltes lista rendezését
		// ##########################################################################

		for (int i = 0; i < finalPeople.size(); i++) {
			finalPeople.get(i).setField(priorityList.get(0));
			finalPeople.get(i).setLevel(levelList.get(0));

			priorityList.remove(0);
			levelList.remove(0);
		}

		finalPeople.sort(Person.fieldComperator1);

		try {
			for (int i = 0; i < finalPeople.size() - 1; i++) {
				// itt ellenőrzöm, hogy két kajakos egymás melletti pályán van-e
				// , és ha igen, akkor már rendeztem, vagy sem.
				if ((finalPeople.get(i).getTeam().equals(finalPeople.get(i + 1).getTeam())
						&& finalPeople.get(i).isOrdered() == false) && finalPeople.size() > 1) {
					// a megkapott értéket változóba mentem, hogy jobban tudjam debug-olni
					final int r = excactLevel(finalPeople, finalPeople.get(i + 1).getLevel(),
							finalPeople.get(i + 1).getField());
					if (r == 0) {
						// csak akkor avatkozom be, ha nem 0 a visszatérési érték
					} else {
						// a megkapott értéket változóba mentem, hogy jobban tudjam debug-olni
						Person p = excactPerson(finalPeople, finalPeople.get(i + 1).getLevel(),
								finalPeople.get(i + 1).getField());
						// csak akkor avatkozom be, ha nem null a visszatérési érték
						if (p != null) {

							tempField = p.getField();
							// a debug miatt mentettem változóba
							int v = finalPeople.get(i + 1).getField();
							p.setField(v);

							finalPeople.get(i + 1).setField(tempField);

							// finalPeople.get(i+1).setField(tempField);
						}
					}
				}
				finalPeople.get(i).setOrdered(true);
			}

			finalPeople.sort(Person.fieldComperator1);

		} catch (Exception e) {
			System.out.println(e);
		}

		// ##########################################################################
		// Innen kezdem a sima lista rendezését
		// ##########################################################################
		// sorbaradezem csapat szernit
		teamList.sort(Person.TeamComperator);

		teamList = SorszamotAd(teamList, sorszam);

		teamList.sort(Person.priorityComperator2);

		finalPeople.sort(Person.levelComperator);
		
        if (finalPeople.size() % 2 == 0 && !finalPeople.isEmpty()) {
            kapcsolo=true;
        } else {
            kapcsolo=false;
        }

		teamlist = numberOfTeamsInList(teamList);

		while (teamlist > 1 && !teamList.isEmpty()) {
			if (!finalPeople.isEmpty()) {
				Person utolso = getTeamOfLastPrioPerson(finalPeople, kapcsolo);

				for (int i = 0; i <= teamList.size() - 1; i++) {
					if (!teamList.get(i).getTeam().equals(utolso.getTeam()) && probalkozas < teamList.size()) {

						teamList.get(i).setField(priorityList.get(0));

						teamList.get(i).setLevel(levelList.get(0));
						teamList.get(i).setPriority(100);
						priorityList.remove(0);
						levelList.remove(0);
						finalPeople.add(teamList.get(i));
						teamList.remove(i);
						if (kapcsolo) {
							kapcsolo = false;
						} else {
							kapcsolo = true;
						}
						probalkozas = 0;
						break;
					} else {
						probalkozas++;
					}

				}

			} else {
				// ha nincs a kiemelteslistában senki, akkor ez fut le!

				teamList.get(0).setLevel(levelList.get(0));
				teamList.get(0).setField(priorityList.get(0));
				teamList.get(0).setPriority(100);
				priorityList.remove(0);
				levelList.remove(0);
				finalPeople.add(teamList.get(0));
				teamList.remove(0);

			}
			if (!teamList.isEmpty()) {
				teamlist = numberOfTeamsInList(teamList);
			}
		}

		while (!teamList.isEmpty()) {
			for (Person person : teamList) {

				person.setField(priorityList.get(0));

				person.setLevel(levelList.get(0));
				person.setPriority(100);
				priorityList.remove(0);
				levelList.remove(0);
				finalPeople.add(person);
				teamList.remove(person);
				if (kapcsolo) {
					kapcsolo = false;
				} else {
					kapcsolo = true;
				}
				break;
			}
		}

		return finalPeople;
	}

	/*
	 * itt megkeresem annak a kajakosnak a pályáját, akinek ugyanolyan a pálya
	 * szintje, mint a cserélendőnek, ha van ilyen, akkor visszatérek a pályával, ha
	 * nem akkor egy 0-át adok vissza, amit mejd ellenőrzök.
	 */
	private int excactLevel(@SuppressWarnings("rawtypes") List<Person> lista, int level, int field) {
		for (@SuppressWarnings("rawtypes")
		Person person : lista) {
			if (person.getLevel() == level && person.getField() != field) {
				return person.getField();
			}
		}
		return 0;
	}

	// Itt visszatérek azzal a kajakossal, akinek a pályáját majd el kell cserélni.

	@SuppressWarnings("rawtypes")
	private Person excactPerson(List<Person> lista, int level, int field) {

		for (int i = 0; i < lista.size(); i++) {

			if (lista.get(i).getLevel() == level && lista.get(i).getField() != field) {
				return lista.get(i);
			}
		}
		return null;
	}

	/*
	 * erre a két függvényre azért van szükségem,mert amikor a kiemelt listához
	 * hozzá akarom adni a nem kiemelteket, akkor két irányból, középről kifelé kell
	 * hozzáadogatnom őket úgy, hogy csapattagok ne kerüljenek egymás mellé. ezért
	 * szükségem van a level szinten a két utolsó kiemeltre (ha van egyáltalán
	 * kettő).
	 */
	@SuppressWarnings("rawtypes")
	private Person getTeamOfLastPrioPerson(List<Person> prioLista, boolean kapcsolo) {
		if (kapcsolo == false) {
			return prioLista.get(prioLista.size() - 1);
		} else {
			return prioLista.get(prioLista.size() - 2);
		}

	}

	/*
	 * ez a függvény visszaadja, hogy a nem kiemeltes listában hányféle csapat van.
	 */
	@SuppressWarnings("rawtypes")
	private int numberOfTeamsInList(List<Person> TeamLista) {
		if (TeamLista.isEmpty()) {
			return 0;
		} else {
			int sumOfTeamInList = 1;
			for (int i = 0; i < TeamLista.size() - 1; i++) {
				if (TeamLista.get(i).getTeam().equals(TeamLista.get(i + 1).getTeam())) {

				} else {
					sumOfTeamInList++;
				}
			}
			return sumOfTeamInList;
		}

	}

	/*
	 * Ebben a függvényben egy sorszámot adok a listához, és ehhez újra felhasználom
	 * a priority változót-et, amit korábban másra készítettem. a cél az, hogy
	 * később ez alapján tudjam rendezni a listát.
	 */

	@SuppressWarnings("rawtypes")
	private List<Person> SorszamotAd(List<Person> ujlista, int sorszam) {
		if (ujlista.isEmpty()) {
			return ujlista;
		} else {
			ujlista.get(0).setPriority(sorszam);
			for (int i = 1; i < ujlista.size(); i++) {
				if (!ujlista.get(i).getTeam().equals(ujlista.get(i - 1).getTeam())) {
					ujlista.get(i).setPriority(sorszam + 1);
					sorszam++;
				} else {
					ujlista.get(i).setPriority(sorszam);
				}
			}
			return ujlista;
		}

	}

	public void clearLists() {

		this.list1.clear();
		this.kapcsolo=false;
		this.list2.clear();
		this.priorityList.clear();
		this.levelList.clear();
		this.teamList.clear();
		this.people.clear();
		this.finalPeople.clear();
	}

}
