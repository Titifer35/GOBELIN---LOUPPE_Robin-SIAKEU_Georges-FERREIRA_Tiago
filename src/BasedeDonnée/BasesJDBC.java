package BasedeDonnée;

import java.sql.Statement;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BasesJDBC {
    
//----------------------------------------------------------------------------------------------------------------------//
    public static void DELETE(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:database" + File.separator + "basic;shutdown=true";
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
        String url = "jdbc:hsqldb:database" + File.separator + "basic;shutdown=true";
        String login = "sa";
        String password = "";
        try (Connection connection = DriverManager.getConnection(url, login, password)) {
            String requete = "CREATE TABLE SITES (" +
                             "Idsite INT PRIMARY KEY, " +
                             "x INT, " +
                             "y INT);" +
                             
                             "CREATE TABLE CLIENTS (" +
                             "Idsite INT, " +
                             "nom VARCHAR(30), " +
                             "mail VARCHAR(30), " +
                             "demande INT, " +
                             "PRIMARY KEY(Idsite), " +
                             "FOREIGN KEY(Idsite) REFERENCES SITES(Idsite));" +
                             
                             "CREATE TABLE ENTREPOTS (" +
                             "Identrepot INT, " +
                             "Idsite INT, " +
                             "coutfixe INT, " +
                             "disponible boolean,"+
                             "stock INT, " +
                             "PRIMARY KEY (Identrepot), "+
                             "FOREIGN KEY(Idsite) REFERENCES SITES(Idsite));" +
                             
                             "CREATE TABLE ROUTES (" +
                             "origine INT, " +
                             "destination INT, " +
                             "distance int, "+
                             "PRIMARY KEY(origine, destination), " +
                             "FOREIGN KEY(origine) REFERENCES SITES(Idsite), " +
                             "FOREIGN KEY(destination) REFERENCES SITES(Idsite));";
                             
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(requete);
            }
		}
	}

//--------------------------------------------------------------------------------------------------------------------//
	public static void main(String[] args) throws Exception {
		BasesJDBC CREATE = new BasesJDBC();
		CREATE.DELETE(null);
		CREATE.CREATE(null);
		System.out.println("CREATION DE LA BASE DE DONNéEs");
	}
}
