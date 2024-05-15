import java.beans.Statement;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class BasesJDBC {

	public static void DELETE(String[] args) throws Exception {
		Class.forName( "org.hsqldb.jdbcDriver" );
		String url = "jdbc:hsqldb:file:database"+File.separator+"basic;shutdown=true";
		String login = "sa";
		String password = "";
		try (Connection connection = DriverManager.getConnection( url, login, password )){
			String requete = "DROP TABLE ROUTES IF EXISTS;"
					+ "DROP TABLE CLIENTS IF EXISTS;"
					+ "DROP TABLE ENTREPOTS IF EXISTS;"
					+ "DROP TABLE SITES IF EXISTS;";

			try ( Statement statement = connection.createStatement() ) {
				statement.executeUpdate( requete );
			}

			requete = "CREATE TABLE ROUTES ("
					+"origine int,"
					+"destination int,"
					+"PRIMARY KEY(orignine))";
			try ( Statement statement = connection.createStatement() ) {
				statement.executeUpdate( requete );
			}

			requete = "INSERT INTO citation (annee, citation, auteur) VALUES"
					+"(1947, 'La simplicite est la reussite absolue', 'Frederic Chopin'),"
					+"(1979, 'Le bonheur, tu sauras que c''est la simplicite', 'Jacques Brillant'),"
					+"(1986, 'La simplicite est la cle de la reussite','Andre Rochette')";
			try ( Statement statement = connection.createStatement() ) {
				statement.executeUpdate( requete );
			}
		}
	}

}

