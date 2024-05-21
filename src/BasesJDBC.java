import java.sql.Statement;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class BasesJDBC {
	
//----------------------------------------------------------------------------------------------------------------------//
	public static void DELETE(String[] args) throws Exception {
		Class.forName( "org.hsqldb.jdbcDriver" );
		String url = "jdbc:hsqldb:file:database"+File.separator+"basic;shutdown=true";
		String login = "sa";
		String password = "";
		try (Connection connection = DriverManager.getConnection( url, login, password )){
			String requete="";
			
			requete = "DROP TABLE ROUTES IF EXISTS;"
					+ "DROP TABLE CLIENTS IF EXISTS;"
					+ "DROP TABLE ENTREPOTS IF EXISTS;"
					+ "DROP TABLE SITES IF EXISTS;";

			try ( Statement statement = connection.createStatement() ) {
				statement.executeUpdate( requete );
			}
		}
	}

//----------------------------------------------------------------------------------------------------------------------//
	public static void CREATE(String[] args) throws Exception {
		Class.forName( "org.hsqldb.jdbcDriver" );
		String url = "jdbc:hsqldb:file:database"+File.separator+"basic;shutdown=true";
		String login = "sa";
		String password = "";
		try (Connection connection = DriverManager.getConnection( url, login, password )){	
			String requete ="";
			requete = "CREATE TABLE ROUTES ("
					+"origine int,"
					+"destination int,"
					+"PRIMARY KEY(orignine))"

			+"CREATE TABLE CLIENTS ("
			+"nom varchar(30),"
			+"prenom varchar(30),"
			+"Id_site int"
			+"PRIMARY KEY(Id_site))"

			+"CREATE TABLE ENTREPROTS ("
			+"Id_entreprot int,"
			+"Id_site int,"
			+"cout_fixe int,"
			+"stock int,"
			+"PRIMARY KEY(Id_entreprot),"
			+"FOREIGN KEY(Id_site))"

			+"CREATE TABLE SITES ("
			+"Id_site int,"
			+"x int,"
			+"y int,"
			+"PRIMARY KEY(Id_site))"
			+"FOREIGN KEY(Id_site))";

			try ( Statement statement = connection.createStatement() ) {
				statement.executeUpdate( requete );
			}
		}
	}

//--------------------------------------------------------------------------------------------------------------------//
	public static void main(String[] args) throws Exception {
		BasesJDBC CREATE = new BasesJDBC();
		CREATE.DELETE(args);
		CREATE.CREATE(args);
		System.out.println("CREATION DE LA BASE DE DONNéEs");
	}
}

