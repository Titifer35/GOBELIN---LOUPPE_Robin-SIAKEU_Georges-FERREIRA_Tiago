import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileWriter;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CreationJson {

    private static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";
    private static final String DB_URL = "jdbc:hsqldb:file:database" + File.separator + "basic;shutdown=true";
    private static final String USER = "sa";
    private static final String PASS = "";

    public CreationJson() throws ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
    }

    public static void populateDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO SITES (Idsite, x, y) VALUES (1, 100, 200);");
            stmt.execute("INSERT INTO CLIENTS (Idsite, nom, mail) VALUES (1, 'Alice', 'alice@example.com');");
            stmt.execute("INSERT INTO ENTREPOTS (Identrepot, Idsite, coutfixe, stock) VALUES (1, 1, 500, 1000);");
            stmt.execute("INSERT INTO ROUTES (origine, destination) VALUES (1, 1);");
        }
    }

    public static void main(String[] args) throws Exception {
        BasesJDBC.DELETE(null);
        BasesJDBC.CREATE(null);
        populateDatabase();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter("Donnees.json")) {
            Json json = new Json(null, null, null, null, 0, 0);
            gson.toJson(json, writer);
        }

        System.out.println("Database created, data populated, and JSON file generated.");
    }
}
