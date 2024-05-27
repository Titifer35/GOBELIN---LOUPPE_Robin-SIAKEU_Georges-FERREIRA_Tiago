import java.io.FileWriter;
import java.io.Writer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        // Print banner
        System.out.println("+------------------------------+");
        System.out.println("|           GOBLIN             |");
        System.out.println("+------------------------------+");

        // Initialize the database and JDBC
        BasesJDBC.DELETE(null);
        BasesJDBC.CREATE(null);
        populateDatabase();  // Populate the database with sample data

        // Create a creationJSON instance to use its data fetching methods
        CreationJson creation = new CreationJson();

        // Fetch data from the database
        List<Integer> capacites = creation.getCapacites();
        List<Integer> coutFixeEntrepot = creation.getCoutFixeEntrepot();
        List<Integer> demandes = creation.getDemandes();
        int[][] costMatrix = creation.getCostmatrix();
        int nbEntrepots = creation.getNbEntrepots();
        int nbClients = creation.getNbClients();

        // Create a Json object using the fetched data
        Json jsonData = new Json(capacites, coutFixeEntrepot, demandes, costMatrix, nbEntrepots, nbClients);

        // Serialize the Json object to a JSON file
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter("Donnees.json")) {
            gson.toJson(jsonData, writer);
        }

        // Print confirmation
        System.out.println("Data fetched and written to Donnees.json successfully.");
    }

    private static void populateDatabase() throws Exception {
        // Assuming BasesJDBC class has been modified to contain this method
        // This should contain insert statements to populate each table
    }
}
