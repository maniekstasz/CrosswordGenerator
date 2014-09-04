package crossword.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import crossword.core.ApplicationContext;
import crossword.handler.DBHandler;

public class InteliDao {
	private DBHandler dbHandler = ApplicationContext.dbHandler;

	private final String RANDOM = " ORDER BY RANDOM() LIMIT ?";
	private final String SELECT = "SELECT * FROM entry";
	private final String BY_PATTERN = " WHERE word ~* ?";
	
	private final String EXCEPTION_MSG = "Błąd przy pracy z baza danych. Uruchom ponownie program aby sprawdzić czy istnieje połaczenie.\n" +
			"Jeśli tak skontaktuj się z Administratorem:)";
	public List<Entry> findAll(String pattern) throws Exception {
		PreparedStatement statement = null;
		ResultSet result = null;
		List<Entry> list = new ArrayList<Entry>();
		try {
			Connection con = dbHandler.getConnection();
			statement = con.prepareStatement(SELECT + BY_PATTERN);
			statement.setString(1, pattern);
			statement.execute();
			result = statement.getResultSet();

			while (result.next()) {
				list.add(new Entry(result.getLong("id"), result
						.getString("word"), result.getString("clue")));
			}
		} catch (SQLException e) {
			throw new Exception(EXCEPTION_MSG);

		} finally {
			if (result != null) {
				result.close();
			}
			result = null;
			if (statement != null) {
				statement.close();
			}
			statement = null;
		}
		return list;
	}

	public Entry getRandom() throws Exception {

		PreparedStatement statement = null;
		ResultSet result = null;
		Entry entry = null;
		try {
			Connection con = dbHandler.getConnection();
			statement = con.prepareStatement(SELECT + RANDOM);
			statement.setInt(1, 1);
			statement.execute();
			result = statement.getResultSet();
			if (result.next()) {
				entry = new Entry(result.getLong("id"),
						result.getString("word"), result.getString("clue"));
			}
		} catch (SQLException e) {
			throw new Exception(EXCEPTION_MSG);

		} finally {
			if (result != null) {
				result.close();
			}
			result = null;
			if (statement != null) {
				statement.close();
			}
			statement = null;
		}
		return entry;
	}

	public Entry getRandom(String pattern) throws Exception {
		PreparedStatement statement = null;
		ResultSet result = null;
		Entry entry = null;
		try {
			Connection con = dbHandler.getConnection();
			statement = con.prepareStatement(SELECT + BY_PATTERN + RANDOM);
			statement.setString(1, pattern);
			statement.setInt(2, 1);
			statement.execute();
			result = statement.getResultSet();
			if (result.next()) {
				entry = new Entry(result.getLong("id"),
						result.getString("word"), result.getString("clue"));
			}
		} catch (SQLException e) {
			throw new Exception(EXCEPTION_MSG);

		} finally {
			if (result != null) {
				result.close();
			}
			result = null;
			if (statement != null) {
				statement.close();
			}
			statement = null;
		}
		return entry;
	}

	public boolean checkIfEmpty() throws Exception {
		PreparedStatement statement = null;
		ResultSet result = null;
		boolean empty = true;
		try {
			Connection con = dbHandler.getConnection();
			statement = con.prepareStatement("SELECT id FROM entry LIMIT 1");
			statement.execute();
			result = statement.getResultSet();
			empty = !result.next();
		} catch (SQLException e) {
			throw new Exception(EXCEPTION_MSG);

		} finally {
			if (result != null) {
				result.close();
			}
			result = null;
			if (statement != null) {
				statement.close();
			}
			statement = null;
		}
		return empty;
	}
}
