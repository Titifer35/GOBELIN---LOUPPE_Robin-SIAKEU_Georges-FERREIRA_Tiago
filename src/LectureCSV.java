import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.*;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVReader;

public class LectureCSV {

    private List<Route> routes;
    private List<Entrepot> entrepots;
    private List<Client> clients;

    public LectureCSV() {
        this.clients = new LinkedList<>();
        this.routes = new LinkedList<>();
        this.entrepots = new LinkedList<>();
    }

    public void importSitesFromCSV(String filePath) throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:file:database" + File.separator + "basic;shutdown=true";
        String login = "sa";
        String password = "";
        
        try (Connection connection = DriverManager.getConnection(url, login, password);
             CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath))
                                     .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                                     .withSkipLines(1).build()) {

            String insertQuery = "INSERT INTO SITES(id_site, x, y) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            String[] nextLine;

            while ((nextLine = csvReader.readNext()) != null) {
                statement.setInt(1, Integer.parseInt(nextLine[0]));
                statement.setInt(2, Integer.parseInt(nextLine[1]));
                statement.setInt(3, Integer.parseInt(nextLine[2]));
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importClientsFromCSV(String clientFilePath, String orderFilePath) throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:file:database" + File.separator + "basic;shutdown=true";
        String login = "sa";
        String password = "";
        Map<String, Integer> clientOrders = new HashMap<>();

        // Load orders from the bordereau file
        try (BufferedReader reader = new BufferedReader(new FileReader(orderFilePath))) {
            List<String> content = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                content.add(line);
            }
            int numberOfClients = Integer.parseInt(content.get(2));
            for (int i = 0; i < numberOfClients; i++) {
                String[] order = content.get(i + 3).split(" : ");
                clientOrders.put(order[0], Integer.parseInt(order[1]));
            }
        }

        try (Connection connection = DriverManager.getConnection(url, login, password);
             CSVReader csvReader = new CSVReaderBuilder(new FileReader(clientFilePath))
                                     .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                                     .withSkipLines(1).build()) {

            String insertQuery = "INSERT INTO CLIENTS(mail, id_site, nom, demande) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            String[] nextLine;

            while ((nextLine = csvReader.readNext()) != null) {
                String mail = nextLine[1];
                statement.setString(1, mail);
                statement.setInt(2, Integer.parseInt(nextLine[2]));
                statement.setString(3, nextLine[0]);
                statement.setInt(4, clientOrders.getOrDefault(mail, 0));
                statement.executeUpdate();

                clients.add(new Client(nextLine[0], mail, Integer.parseInt(nextLine[2]), clientOrders.getOrDefault(mail, 0)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importEntrepotsFromCSV(String filePath, String bordereau) throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:file:database" + File.separator + "basic;shutdown=true";
        String login = "sa";
        String password = "";
        List<Integer> openEntrepots = new LinkedList<>();

        // Load warehouse availability from the bordereau
        try (BufferedReader reader = new BufferedReader(new FileReader(bordereau))) {
            String lastLine = null, line;
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }
            Arrays.stream(lastLine.split(",")).forEach(id -> openEntrepots.add(Integer.parseInt(id)));
        }

        try (Connection connection = DriverManager.getConnection(url, login, password);
             CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath))
                                     .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                                     .withSkipLines(1).build()) {

            String insertQuery = "INSERT INTO ENTREPOTS(id_entrepot, id_site, cout_utilisation, stock, disponible) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            String[] nextLine;

            while ((nextLine = csvReader.readNext()) != null) {
                int idEntrepot = Integer.parseInt(nextLine[0]);
                boolean isAvailable = openEntrepots.contains(idEntrepot);
                statement.setInt(1, idEntrepot);
                statement.setInt(2, Integer.parseInt(nextLine[1]));
                statement.setInt(3, Integer.parseInt(nextLine[2]));
                statement.setInt(4, Integer.parseInt(nextLine[3]));
                statement.setBoolean(5, isAvailable);
                statement.executeUpdate();

                entrepots.add(new Entrepot(idEntrepot, Integer.parseInt(nextLine[1]), Integer.parseInt(nextLine[2]), Integer.parseInt(nextLine[3]), isAvailable));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importRoutesFromCSV(String filePath) throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:file:database" + File.separator + "basic;shutdown=true";
        String login = "sa";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, login, password);
             CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath))
                                     .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                                     .withSkipLines(1).build()) {

            String insertQuery = "INSERT INTO ROUTES(origine, destination, distance) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            String[] nextLine;

            while ((nextLine = csvReader.readNext()) != null) {
                int origin = Integer.parseInt(nextLine[0]);
                int destination = Integer.parseInt(nextLine[1]);
                int distance = calculateDistance(connection, origin, destination);

                statement.setInt(1, origin);
                statement.setInt(2, destination);
                statement.setInt(3, distance);
                statement.executeUpdate();

                routes.add(new Route(origin, destination, 0, 0, 0, 0));  // Coords not used here
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int calculateDistance(Connection connection, int origin, int destination) throws SQLException {
        int xo = 0, yo = 0, xd = 0, yd = 0;

        String query = "SELECT x, y FROM SITES WHERE id_site = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        
        statement.setInt(1, origin);
        try (ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                xo = rs.getInt("x");
                yo = rs.getInt("y");
            }
        }

        statement.setInt(1, destination);
        try (ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                xd = rs.getInt("x");
                yd = rs.getInt("y");
            }
        }

        return (int) Math.sqrt(Math.pow(xd - xo, 2) + Math.pow(yd - yo, 2));
    }
}
