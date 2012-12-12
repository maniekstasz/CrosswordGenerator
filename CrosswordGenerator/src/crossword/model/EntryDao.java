package crossword.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import crossword.core.ApplicationContext;
import crossword.handler.DBHandler;

public class EntryDao {
	private DBHandler dbHandler = ApplicationContext.dbHandler;

	private final String INSERT = "INSERT INTO entry (word, clue) VALUES (?,?)";
	private final String DELETE = "DELETE FROM entry WHERE word = ?";
	private final String GET = "SELECT * FROM entry WHERE word = ?";
	private final String EXCEPTION_MSG = "Błąd przy pracy z baza danych. Uruchom ponownie program aby sprawdzić czy istnieje połaczenie.\n" +
			"Jeśli tak skontaktuj się z Administratorem:)";
	public void save(List<Entry> entries) throws Exception {
		PreparedStatement statement = null;
		try {
			Connection con = dbHandler.getConnection();
			statement = con.prepareStatement(INSERT);
			int count = 0;
			int batchSize = 1000;
			for (Entry entry : entries) {
				statement.setString(1, entry.getWord());
				statement.setString(2, entry.getClue());
				statement.addBatch();
				if (++count % batchSize == 0) {
					statement.executeBatch();
				}
			}
			statement.executeBatch();
			// ResultSet generatedKeys = statement.getGeneratedKeys();
			// int i = 0;
			// if (generatedKeys.next()) {
			// entries.get(i++).setId(generatedKeys.getLong(1));
			// }
			// generatedKeys.close();
		} catch (SQLException e) {
			throw new Exception("Błąd przy pracy z baza danych. Upewnij się, że wczytywany plik ma poprawną formę.");

		} finally {
			if (statement != null) {
				statement.close();
			}
			statement = null;
		}
	}

	public void remove(String word) throws Exception {
		PreparedStatement statement = null;
		try {
			Connection con = dbHandler.getConnection();
			statement = con.prepareStatement(DELETE);
			statement.setString(1, word);
			statement.execute();
		}catch (SQLException e) {
			throw new Exception(EXCEPTION_MSG);

		} finally {
			if (statement != null) {
				statement.close();
			}
			statement = null;
		}
	}

	public Entry get(String word) throws Exception {
		PreparedStatement statement = null;
		ResultSet result = null;
		Entry entry = null;
		try {
			Connection con = dbHandler.getConnection();
			statement = con.prepareStatement(GET);
			statement.setString(1, word);
			statement.execute();
			result = statement.getResultSet();

			if (result.next()) {
				entry = new Entry(result.getLong("id"),
						result.getString("word"), result.getString("clue"));
			}
		}catch (SQLException e) {
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
}
