package BasedeDonnée;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
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

public class CreationJson {

	private static final String PILOTE = "org.hsqldb.jdbcDriver";
	private static final String URL = "jdbc:hsqldb:database" + File.separator + "basic;shutdown=true";
	private static final String LOGIN = "sa";
	private static final String PASSWORD = "";

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------//

	public int getNombreClients() throws ClassNotFoundException, SQLException {
		
		Class.forName(PILOTE);
		
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

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------//

	public int getNombreEntrepots() throws ClassNotFoundException, SQLException {
		
		Class.forName(PILOTE);
		
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

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------//

	public int getNombreSites() throws ClassNotFoundException, SQLException {
		Class.forName(PILOTE);
		
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

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------//

	public List<Integer> getCapacites() throws SQLException, ClassNotFoundException {
		
		Class.forName(PILOTE);
		
		String requete = "SELECT stock FROM ENTREPOTS WHERE disponible = 1";
		List<Integer> capacite = new ArrayList<>();
		
		try (Connection connexion = DriverManager.getConnection(URL, LOGIN, PASSWORD);
				Statement declaration = connexion.createStatement();
				ResultSet resultat = declaration.executeQuery(requete)) {
			while (resultat.next()) {
				int stock = resultat.getInt("stock");
				capacite.add(stock);
			}
		}
		return capacite;
	}

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------//

	public List<Integer> getCoutFixeEntrepot() throws SQLException, ClassNotFoundException {
		
		Class.forName(PILOTE);
		
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

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------//

	public List<Integer> getDemandes() throws ClassNotFoundException, SQLException {
		
		Class.forName(PILOTE);
		
		String requete = "SELECT demande FROM CLIENTS WHERE demande != 0";
		List<Integer> demande = new ArrayList<>();
		
		try (Connection connexion = DriverManager.getConnection(URL, LOGIN, PASSWORD);
				Statement declaration = connexion.createStatement();
				ResultSet resultat = declaration.executeQuery(requete)) {
			while (resultat.next()) {
				demande.add(resultat.getInt("demande"));
			}	
		}
		return demande;
	}

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------//

	public int[][] getMatriceCouts() throws ClassNotFoundException, SQLException {
		
		Class.forName(PILOTE);

		try (Connection connexion = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
			List<Integer> sites = recupererIdSites(connexion, "SELECT Idsite FROM SITES");
			List<Integer> entrepotsDisponibles = recupererIdSites(connexion, "SELECT Idsite FROM ENTREPOTS WHERE disponible = 1");
			List<Integer> clientsDemandants = recupererIdSites(connexion, "SELECT Idsite FROM CLIENTS WHERE demande != 0");

			CheminPlusCourt cheminPlusCourt = new CheminPlusCourt();
			int[][] matriceTotale = cheminPlusCourt.Chemin_Plus_Court(sites);

			return construireMatriceCouts(entrepotsDisponibles, clientsDemandants, matriceTotale);
		}
	}

	// Méthode pour récupérer les ID des sites d'une requête SQL donnée
	private List<Integer> recupererIdSites(Connection connexion, String requete) throws SQLException {
		List<Integer> Idsite = new ArrayList<>();
		try (Statement declaration = connexion.createStatement();
				ResultSet resultat = declaration.executeQuery(requete)) {
			while (resultat.next()) {
				Idsite.add(resultat.getInt("Idsite"));
			}
		}
		return Idsite;
	}

	// Construire la matrice des coûts spécifique entre entrepôts et clients
	private int[][] construireMatriceCouts(List<Integer> entrepots, List<Integer> clients, int[][] matriceTotale) {
		int[][] matriceCouts = new int[entrepots.size()][clients.size()];
		for (int i = 0; i < entrepots.size(); i++) {
			for (int j = 0; j < clients.size(); j++) {
				// Attention à l'indexation des listes pour correspondre aux indices de la matrice
				matriceCouts[i][j] = matriceTotale[entrepots.get(i)][clients.get(j)];
			}
		}
		return matriceCouts;
	}

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------//

	public static void main(String[] args) throws Exception {
		String fichier = "Donnees.json";
		Gson json = new GsonBuilder().setPrettyPrinting().create();
		Writer ecriture = new FileWriter(fichier);
		CreationJson creationJson = new CreationJson();

		List<Integer> capacites = creationJson.getCapacites();

		Json jsonDonnees = new Json(
				capacites,
				creationJson.getCoutFixeEntrepot(),
				creationJson.getDemandes(),
				creationJson.getMatriceCouts(),
				creationJson.getNombreClients(),
				creationJson.getNombreEntrepots()
				
				);
		json.toJson(jsonDonnees, ecriture);
		ecriture.flush();
		ecriture.close();
	}
}

