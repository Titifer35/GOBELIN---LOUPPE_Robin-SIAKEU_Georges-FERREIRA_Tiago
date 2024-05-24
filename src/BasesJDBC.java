import java.sql.Statement;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BasesJDBC {
    
    //----------------------------------------------------------------------------------------------------------------------//
    public static void DELETE(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:file:database" + File.separator + "basic;shutdown=true";
        String login = "sa";
        String password = "";
        try (Connection connection = DriverManager.getConnection(url, login, password)) {
            String requete = "DROP TABLE ROUTES IF EXISTS; " +
                             "DROP TABLE CLIENTS IF EXISTS; " +
                             "DROP TABLE ENTREPOTS IF EXISTS; " +
                             "DROP TABLE SITES IF EXISTS;";

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(requete);
            }
        }
    }

    //----------------------------------------------------------------------------------------------------------------------//
    public static void CREATE(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:file:database" + File.separator + "basic;shutdown=true";
        String login = "sa";
        String password = "";
        try (Connection connection = DriverManager.getConnection(url, login, password)) {
            String requete = "CREATE TABLE SITES (" +
                             "Id_site INT PRIMARY KEY, " +
                             "x INT, " +
                             "y INT);" +
                             
                             "CREATE TABLE CLIENTS (" +
                             "Id_site INT, " +
                             "nom VARCHAR(30), " +
                             "prenom VARCHAR(30), " +
                             "PRIMARY KEY(Id_site), " +
                             "FOREIGN KEY(Id_site) REFERENCES SITES(Id_site));" +
                             
                             "CREATE TABLE ENTREPOTS (" +
                             "Id_entrepot INT PRIMARY KEY, " +
                             "Id_site INT, " +
                             "cout_fixe INT, " +
                             "stock INT, " +
                             "FOREIGN KEY(Id_site) REFERENCES SITES(Id_site));" +
                             
                             "CREATE TABLE ROUTES (" +
                             "origine INT, " +
                             "destination INT, " +
                             "PRIMARY KEY(origine, destination), " +
                             "FOREIGN KEY(origine) REFERENCES SITES(Id_site), " +
                             "FOREIGN KEY(destination) REFERENCES SITES(Id_site));";
                             
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(requete);
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
