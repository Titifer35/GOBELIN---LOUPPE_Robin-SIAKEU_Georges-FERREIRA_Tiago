package goblin;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.sql.SQLException;

public class LectureCSV {

    // Initialisation des variables
    private List<Route> routes;
    private List<Entrepot> entrepots;
    private List<Client> clients;

    public LectureCSV() {
        this.clients = new LinkedList<>();
        this.routes = new LinkedList<>();
        this.entrepots = new LinkedList<>();
    }

    // Méthode pour lire et insérer les sites dans la BD
    public void insertSitesFromCSV(String dossierSites) throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:file:database" + File.separator + "basic;shutdown=true";
        String login = "sa";
        String password = "";

        String insertQuery = "INSERT INTO SITES(id_site, x, y) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, login, password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
             FileReader reader = new FileReader(dossierSites);
             CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).withSkipLines(1).build()){

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                int idSite = Integer.parseInt(nextLine[0]);
                int x = Integer.parseInt(nextLine[1]);
                int y = Integer.parseInt(nextLine[2]);

                preparedStatement.setInt(1, idSite);
                preparedStatement.setInt(2, x);
                preparedStatement.setInt(3, y);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour lire et insérer les clients dans la BD
    public void insertClientsFromCSV(String dossierClients, String bordereau) throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:file:database" + File.separator + "basic;shutdown=true";
        String login = "sa";
        String password = "";

        Map<String, Integer> clientsMap = new HashMap<>();
        List<String> contenuFichier = new ArrayList<>();

        try (BufferedReader aLire = new BufferedReader(new FileReader(bordereau))) {
            String ligne;
            while ((ligne = aLire.readLine()) != null) {
                contenuFichier.add(ligne);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
        }

        int nbClients = Integer.parseInt(contenuFichier.get(2));
        for (int i = 0; i < nbClients; i++) {
            String[] commande = contenuFichier.get(i + 3).split(" : ");
            String mail = commande[0];
            int demandeC = Integer.parseInt(commande[1]);
            clientsMap.put(mail, demandeC);
        }

        try (Connection connection = DriverManager.getConnection(url, login, password);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CLIENTS(mail, id_site, nom, demande) VALUES (?, ?, ?, ?)");
             FileReader reader = new FileReader(dossierClients);
             CSVReader csvReader = new CSVReaderBuilder(reader).withRowValidator(';').withSkipLines(1).build()) {

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                String nom = nextLine[0];
                String mail = nextLine[1];
                int idSite = Integer.parseInt(nextLine[2]);
                int demande = clientsMap.getOrDefault(mail, 0);

                preparedStatement.setString(1, mail);
                preparedStatement.setInt(2, idSite);
                preparedStatement.setString(3, nom);
                preparedStatement.setInt(4, demande);
                preparedStatement.executeUpdate();

                clients.add(new Client(nom, mail, idSite, demande));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Méthode pour lire et insérer les entrepôts dans la BD
    public void insertEntrepotsFromCSV(String dossierEntrepots, String bordereau) throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:file:database" + File.separator + "basic;shutdown=true";
        String login = "sa";
        String password = "";

        List<Integer> entrepotsouverts = new LinkedList<>();
        List<String> contenuFichier = new ArrayList<>();

        try (BufferedReader aLire = new BufferedReader(new FileReader(bordereau))) {
            String ligne;
            while ((ligne = aLire.readLine()) != null) {
                contenuFichier.add(ligne);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
        }

        String[] dispo = contenuFichier.get(contenuFichier.size() - 1).split(",");
        for (String s : dispo) {
            entrepotsouverts.add(Integer.parseInt(s));
        }

        try (Connection connection = DriverManager.getConnection(url, login, password);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ENTREPOTS(id_entrepot, id_site, cout_utilisation, stock, disponible) VALUES (?, ?, ?, ?, ?)");
             FileReader reader = new FileReader(dossierEntrepots);
             CSVReader csvReader = new CSVReaderBuilder(reader).withRowValidator(';').withSkipLines(1).build()) {

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                int idEntrepot = Integer.parseInt(nextLine[0]);
                int idSite = Integer.parseInt(nextLine[1]);
                int coutUtilisation = Integer.parseInt(nextLine[2]);
                int stock = Integer.parseInt(nextLine[3]);
                boolean disponible = entrepotsouverts.contains(idEntrepot);

                preparedStatement.setInt(1, idEntrepot);
                preparedStatement.setInt(2, idSite);
                preparedStatement.setInt(3, coutUtilisation);
                preparedStatement.setInt(4, stock);
            }
        }
    }
}