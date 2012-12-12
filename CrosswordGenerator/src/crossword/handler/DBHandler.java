package crossword.handler;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import crossword.core.ApplicationContext;

public class DBHandler {
	private ExceptionHandler exceptionHandler = ApplicationContext.exceptionHandler;

	private Connection connection;

	private final String dbPath = "jdbc:postgresql://localhost:5432/crossword";
	private final String user = "postgres";
	private final String password = "sa";

	public void init() {
		try {
			connect();
			createTable();
		} catch (Exception e) {
			exceptionHandler
					.handleCriticalException(new Exception(
							"Nie udało się połaczyć z bazą danych.\n Program nie może działać bez tego połączenia"));
		}
	}

	public void connect() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(dbPath, user, password);
	}

	public void disconnect() throws SQLException {
		connection.close();
	}

	public Connection getConnection() {
		return connection;
	}

	private void createTable() throws Exception {
		String table = "CREATE TABLE IF NOT EXISTS entry "
				+ "("
				+ " word character varying(50) COLLATE pg_catalog.\"POSIX\" NOT NULL,"
				+ " clue character varying(500)," + " id serial NOT NULL,"
				+ " CONSTRAINT entry_pkey PRIMARY KEY (id)"
				+ ")"
				+ " WITH (" + " OIDS=FALSE" + ");";
		Statement statement;
		statement = connection.createStatement();
		try {
			statement.executeUpdate(table);
		} catch (Exception ignore) {
			// TODO ask if it is ok
		}
	}
}
