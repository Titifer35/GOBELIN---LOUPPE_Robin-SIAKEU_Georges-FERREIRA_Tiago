package BasedeDonnée;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import AlgoDijkstra.CheminPlusCourt;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.Writer;

public class CreationJson {

    private static final String URL = "jdbc:hsqldb:database" + File.separator + "basic;shutdown=true";
    private static final String LOGIN = "sa";
    private static final String PASSWORD = "";
    
    
//--------------------------------------------------------------------------------------------------------------------//

    public int getNombreClients() throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        String requete = "SELECT COUNT(mail) AS resultat FROM CLIENTS WHERE demande != 0";
        int nombreClients = 0;
        try (Connection connexion = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             Statement declaration = connexion.createStatement();
             ResultSet resultat = declaration.executeQuery(requete)) {
            if (resultat.next()) {
                nombreClients = resultat.getInt("resultat");
            }
        }
        return nombreClients;
    }

//--------------------------------------------------------------------------------------------------------------------//

    public int getNombreEntrepots() throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        String requete = "SELECT COUNT(Identrepot) AS resultat FROM ENTREPOTS WHERE disponible = 1";
        int nombreEntrepots = 0;
        try (Connection connexion = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             Statement declaration = connexion.createStatement();
             ResultSet resultat = declaration.executeQuery(requete)) {
            if (resultat.next()) {
                nombreEntrepots = resultat.getInt("resultat");
            }
        }
        return nombreEntrepots;
    }
    
//--------------------------------------------------------------------------------------------------------------------//

    public int getNombreSites() throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        String requete = "SELECT COUNT(Idsite) AS resultat FROM SITES WHERE disponible = 1";
        int nombreSites = 0;
        try (Connection connexion = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             Statement declaration = connexion.createStatement();
             ResultSet resultat = declaration.executeQuery(requete)) {
            if (resultat.next()) {
                nombreSites = resultat.getInt("resultat");
            }
        }
        return nombreSites;
    }

//--------------------------------------------------------------------------------------------------------------------//

    public List<Integer> getCapacites() throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        String requete = "SELECT stock FROM ENTREPOTS WHERE disponible = 1";
        List<Integer> resultats = new ArrayList<>();
        try (Connection connexion = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             Statement declaration = connexion.createStatement();
             ResultSet resultat = declaration.executeQuery(requete)) {
            while (resultat.next()) {
                resultats.add(resultat.getInt("stock"));
            }
        }
        return resultats;
    }
    
//--------------------------------------------------------------------------------------------------------------------//

    public List<Integer> getCoutFixeEntrepot() throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        String requete = "SELECT coutfixe FROM ENTREPOTS WHERE disponible = true;";
        List<Integer> coutFixeEntrepot = new ArrayList<>();
        try (Connection connexion = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             Statement declaration = connexion.createStatement();
             ResultSet resultat = declaration.executeQuery(requete)) {
            while (resultat.next()) {
                coutFixeEntrepot.add(resultat.getInt("coutfixe"));
            }
        }
        return coutFixeEntrepot;
    }
    
//--------------------------------------------------------------------------------------------------------------------//

    public List<Integer> getDemandes() throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        String requete = "SELECT demande FROM CLIENTS WHERE demande != 0";
        List<Integer> resultats = new ArrayList<>();
        try (Connection connexion = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             Statement declaration = connexion.createStatement();
             ResultSet resultat = declaration.executeQuery(requete)) {
            while (resultat.next()) {
                resultats.add(resultat.getInt("demande"));
            }
        }
        return resultats;
    }
    
//--------------------------------------------------------------------------------------------------------------------//

    public int[][] getMatriceCouts() throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        try (Connection connexion = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            List<Integer> sites = new ArrayList<>();
            try (Statement declaration = connexion.createStatement();
                 ResultSet resultat = declaration.executeQuery("SELECT Idsite FROM SITES")) {
                while (resultat.next()) {
                    sites.add(resultat.getInt("Idsite"));
                }
            }
            List<Integer> entrepotsDisponibles = new ArrayList<>();
            try (Statement declaration = connexion.createStatement();
                 ResultSet resultat = declaration.executeQuery("SELECT Idsite FROM ENTREPOTS WHERE disponible = 1")) {
                while (resultat.next()) {
                    entrepotsDisponibles.add(resultat.getInt("Idsite"));
                }
            }
            List<Integer> clientsDemandants = new ArrayList<>();
            try (Statement declaration = connexion.createStatement();
                 ResultSet resultat = declaration.executeQuery("SELECT Idsite FROM CLIENTS WHERE demande != 0")) {
                while (resultat.next()) {
                    clientsDemandants.add(resultat.getInt("Idsite"));
                }
            }
            CheminPlusCourt cheminPlusCourt = new CheminPlusCourt();
            int[][] matriceTotale = cheminPlusCourt.plus_court_chemin(sites);
            int[][] matriceCouts = new int[entrepotsDisponibles.size()][clientsDemandants.size()];
            for (int i = 0; i < entrepotsDisponibles.size(); i++) {
                for (int j = 0; j < clientsDemandants.size(); j++) {
                    matriceCouts[i][j] = matriceTotale[entrepotsDisponibles.get(i) - 1][clientsDemandants.get(j) - 1];
                }
            }
            return matriceCouts;
        }
    }

//--------------------------------------------------------------------------------------------------------------------//

    public static void main(String[] args) throws Exception {
        String fichier = "Donnees.json";
        Gson json = new GsonBuilder().setPrettyPrinting().create();
        Writer ecriture = new FileWriter(fichier);
        CreationJson creationJson = new CreationJson();
        Json jsonDonnees = new Json(
            creationJson.getCapacites(),
            creationJson.getCoutFixeEntrepot(),
            creationJson.getDemandes(),
            creationJson.getMatriceCouts(),
            creationJson.getNombreEntrepots(),
            creationJson.getNombreClients()
        );
        json.toJson(jsonDonnees, ecriture);
        ecriture.flush();
        ecriture.close();
    }
}
