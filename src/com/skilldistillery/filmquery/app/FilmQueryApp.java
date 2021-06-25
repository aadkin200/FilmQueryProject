package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {
	Scanner input = new Scanner(System.in);

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();
//		app.test();
		app.launch();
	}

	private void test() throws SQLException {
		Film film = db.findFilmById(1);
		Actor actor = db.findActorById(1);
		List<Actor> actorList = db.findActorsByFilmId(6);
		List<Film> filmList = db.findFilmByKeyword("feminist");
//    System.out.println(film);
//    System.out.println(actor);
//    System.out.println(actorList);
		System.out.println(filmList);
	}

	private void launch() throws SQLException {
		int choice;
		do {
			startUserInterface();
			choice = userChoice();
			switch (choice) {
			case 1:
				filmById();
				break;
			case 2:
				filmBySearch();
				break;
			case 3:
				exit();
				break;
			default:
				System.out.println("Please select a valid choice!");
			}
		} while (choice != 0);

		input.close();
	}

	private void startUserInterface() {
		System.out.println("-------------MENU-------------");
		System.out.println("1. Look up a film by its id");
		System.out.println("2. Look up a film by a search keyword");
		System.out.println("3. Exit");
		System.out.println("------------------------------");
		System.out.println("Enter your choice: ");
	}

	private void filmById() throws SQLException {
		System.out.println("Please enter a film ID: ");
		int choice = userChoice();
		Film film = db.findFilmById(choice);
		if (!film.equals(null)) {
			printFilm(film);
		} else {
			System.out.println("No film found!");
		}
	}

	private void filmBySearch() {
		System.out.println("Please enter a search word: ");
		String choice = input.nextLine();
		System.out.println(choice);
		List<Film> filmList = db.findFilmByKeyword(choice);
		if (!filmList.equals(null)) {
			for (Film film : filmList) {
				printFilm(film);
			}
		} else {
			System.out.println("No films found!");
		}

	}

	private void exit() {
		System.exit(0);
	}

	private void printFilm(Film film) {
		System.out.println("------------------------------FILM------------------------------");
		System.out.println(film.getTitle());
		System.out.println(film.getYear());
		System.out.println(film.getRating());
		System.out.println(film.getDescription());
		System.out.println(film.getLang());
		System.out.println("-----Actors-----");
		for (Actor actor : film.getActorList()) {
			System.out.println(actor.getFirstName() + " " + actor.getLastName());
		}
		System.out.println("----------------");
		System.out.println("----------------------------------------------------------------");
	}

	private int userChoice() {
		int choice = input.nextInt();
		input.nextLine();
		return choice;
	}

}
