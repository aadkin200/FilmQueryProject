package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;
import com.skilldistillery.filmquery.entities.Language;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private String user = "student";
	private String pass = "student";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) throws SQLException {
		Film film = null;
		Language lang = null;
		List<Actor> actorList = findActorsByFilmId(filmId);
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT *\n"
					+ "FROM film\n"
					+ "JOIN language\n"
					+ "ON film.language_id = language.id\n"
					+ "WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int filmIds = rs.getInt("film.id");
				String title = rs.getString("film.title");
				String desc = rs.getString("film.description");
				Integer releaseYear = rs.getInt("film.release_year");
				int langId = rs.getInt("film.language_id");
				int rentDur = rs.getInt("film.rental_duration");
				double rate = rs.getDouble("film.rental_rate");
				int length = rs.getInt("film.length");
				double repCost = rs.getDouble("film.replacement_cost");
				String rating = rs.getString("film.rating");
				String features = rs.getString("film.special_features");
				int languageId = rs.getInt("language.id");
				String name = rs.getString("language.name");
				
				lang = new Language(languageId, name);
				film = new Film(filmIds, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating,
						features, actorList, name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = " select * from actor where id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int actorIds = rs.getInt("actor.id");
				String firstName = rs.getString("actor.first_name");
				String lastName = rs.getString("actor.last_name");
				actor = new Actor(actorIds, firstName, lastName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) throws SQLException {
		List<Actor> actorList = new ArrayList<>();
		Actor actor = null;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT actor.id, actor.first_name, actor.last_name FROM actor "
					   + "JOIN film_actor ON actor.id = film_actor.actor_id JOIN film ON "
					   + "film_actor.film_id = film.id where film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int actorIds = rs.getInt("actor.id");
				String firstName = rs.getString("actor.first_name");
				String lastName = rs.getString("actor.last_name");
				actor = new Actor(actorIds, firstName, lastName);
				actorList.add(actor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn.close();
		return actorList;
	}

	@Override
	public List<Film> findFilmByKeyword(String keyword) {
		Film film = null;
		Actor actor = null;
		List<Film> filmList = new ArrayList<>();
		List<Actor> actorList = null;
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "Select *\n"
					+ "FROM film\n"
					+ "JOIN language\n"
					+ "ON film.language_id = language.id\n"
					+ "join film_actor\n"
					+ "on film.id = film_actor.film_id\n"
					+ "join actor\n"
					+ "on film_actor.actor_id = actor.id\n"
					+ "WHERE film.title like ? OR film.description\n"
					+ "LIKE ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%" + keyword + "%");
			stmt.setString(2, "%" + keyword + "%");
			System.out.println(stmt);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int filmIds = rs.getInt("film.id");
				String title = rs.getString("film.title");
				String desc = rs.getString("film.description");
				Integer releaseYear = rs.getInt("film.release_year");
				int langId = rs.getInt("film.language_id");
				int rentDur = rs.getInt("film.rental_duration");
				double rate = rs.getDouble("film.rental_rate");
				int length = rs.getInt("film.length");
				double repCost = rs.getDouble("film.replacement_cost");
				String rating = rs.getString("film.rating");
				String features = rs.getString("film.special_features");
				String name = rs.getString("language.name");
				int actorId = rs.getInt("Actor.id");
				
				actorList = findActorsByFilmId(filmIds);
				film = new Film(filmIds, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating,
						features, actorList, name);
				filmList.add(film);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return filmList;
	}

}
