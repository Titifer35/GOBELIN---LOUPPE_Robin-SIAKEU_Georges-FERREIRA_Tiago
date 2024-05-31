import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.Writer;

public class CreationJson {

	public int getNbClients() throws ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbcDriver");
		String url = "jdbc:hsqldb:database" + File.separator + "basic;shutdown=true";
		String login = "sa";
		String password = "";
		String requete = "SELECT COUNT(mail) AS resultat from CLIENTS WHERE demande != 0";
		int NbClients = 0;
		try (Connection connection = DriverManager.getConnection(url, login, password)) {
			try (Statement statement = connection.createStatement()) {
				try (ResultSet resultSet = statement.executeQuery(requete)) {
					resultSet.next();
					NbClients = resultSet.getInt("resultat");
				}
			}
		}
		return NbClients;
	}

	public int getNbEntrepots() throws ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbcDriver");
		String url = "jdbc:hsqldb:database" + File.separator + "basic;shutdown=true";
		String login = "sa";
		String password = "";
		String requete = "SELECT COUNT(Identrepot) AS resultat from ENTREPOTS WHERE disponible = 1";
		int NbEntrepots = 0;
		try (Connection connection = DriverManager.getConnection(url, login, password)) {
			try (Statement statement = connection.createStatement()) {
				try (ResultSet resultSet = statement.executeQuery(requete)) {
					resultSet.next();
					NbEntrepots = resultSet.getInt("resultat");
				}
			}
		}
		return NbEntrepots;
	}

	public int getNbSites() throws ClassNotFoundException, SQLException {
		Class.forName("org.hsqldb.jdbcDriver");
		String url = "jdbc:hsqldb:database" + File.separator + "basic;shutdown=true";
		String login = "sa";
		String password = "";
		String requete = "SELECT COUNT(Idsite) AS resultat from SITES WHERE disponible = 1";
		int NbSites = 0;
		try (Connection connection = DriverManager.getConnection(url, login, password)) {
			try (Statement statement = connection.createStatement()) {
				try (ResultSet resultSet = statement.executeQuery(requete)) {
					resultSet.next();
					NbSites = resultSet.getInt("resultat");
				}
			}
		}
		return NbSites;
	}

	public List<Integer> getCapacites() throws SQLException, ClassNotFoundException {
		List<Integer> resultat = new ArrayList<>();
		String requete = "SELECT stock FROM ENTREPOTS WHERE disponible = 1";
		Class.forName("org.hsqldb.jdbcDriver");
		String url = "jdbc:hsqldb:database" + File.separator + "basic;shutdown=true";
		String login = "sa";
		String password = "";
		try (Connection connection = DriverManager.getConnection(url, login, password)) {
			try (Statement statement = connection.createStatement()) {
				try (ResultSet resultSet = statement.executeQuery(requete)) {
					while (resultSet.next()) {
						resultat.add(resultSet.getInt("stock"));
					}
				}
			}
		}
		return resultat;
	}

	public List<Integer> getCoutFixeEntrepot() throws SQLException, ClassNotFoundException {
		List<Integer> coutFixeEntrepot = new ArrayList<>();
		String requete = "SELECT coutfixe FROM ENTREPOTS WHERE disponible = true;";
		Class.forName("org.hsqldb.jdbcDriver");
		String url = "jdbc:hsqldb:database" + File.separator + "basic;shutdown=true";
		String login = "sa";
		String password = "";
		try (Connection connection = DriverManager.getConnection(url, login, password)) {
			try (Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(requete)) {
				while (resultSet.next()) {
					int cout = resultSet.getInt("coutfixe");
					coutFixeEntrepot.add(cout);
				}
			}
		}

		return coutFixeEntrepot;
	}

	public List<Integer> getDemandes() throws ClassNotFoundException, SQLException {
		List<Integer> resultat = new ArrayList<>();
		String requete = "SELECT demande FROM CLIENTS WHERE demande != 0";
		Class.forName("org.hsqldb.jdbcDriver");
		String url = "jdbc:hsqldb:database" + File.separator + "basic;shutdown=true";
		String login = "sa";
		String password = "";
		try (Connection connection = DriverManager.getConnection(url, login, password)) {
			try (Statement statement = connection.createStatement()) {
				try (ResultSet resultSet = statement.executeQuery(requete)) {
					while (resultSet.next()) {
						resultat.add(resultSet.getInt("demande"));
					}
				}
			}
		}
		return resultat;
	}
	
	public int [][] getCostmatrix() throws ClassNotFoundException, SQLException{
		Class.forName("org.hsqldb.jdbcDriver");
		String url = "jdbc:hsqldb:database" + File.separator + "basic;shutdown=true";
		String login = "sa";
		String password = "";

		try (Connection connection = DriverManager.getConnection(url, login, password)) {		
			//liste de sites
			List<Integer> sites= new ArrayList<>();
			String requete = "SELECT Idsite FROM SITES";
			try (Statement statement = connection.createStatement()) {
				try (ResultSet resultSet = statement.executeQuery(requete)) {
					while (resultSet.next()) {
						sites.add(resultSet.getInt("Idsite"));
					}
				}
			}

			
			List<Integer> entrepots_disponibles = new ArrayList<>();
			requete = "SELECT Idsite FROM ENTREPOTS "
					+ "WHERE DISPONIBLE = 1";
			try (Statement statement = connection.createStatement()) {
				try (ResultSet resultSet = statement.executeQuery(requete)) {
					while (resultSet.next()) {
						entrepots_disponibles.add(resultSet.getInt("Idsite"));
					}
				}
			}

			
			List<Integer> clients_demandants = new ArrayList<>();
			requete = "SELECT Idsite FROM CLIENTS WHERE demande != 0";
			try (Statement statement = connection.createStatement()) {
				try (ResultSet resultSet = statement.executeQuery(requete)) {
					while (resultSet.next()) {
						clients_demandants.add(resultSet.getInt("Idsite"));
					}
				}
			}
			
			
			CheminPlusCourt pcm = new CheminPlusCourt();
			int [][] matrice_totale = pcm.plus_court_chemin(sites);
			int [][] cost_matrix;
			cost_matrix = new int [entrepots_disponibles.size()][clients_demandants.size()];
			for (int i= 0; i<entrepots_disponibles.size();i++) {
				for (int j =0; j<clients_demandants.size();j++) {
					cost_matrix[i][j]= matrice_totale[entrepots_disponibles.get(i)-1][clients_demandants.get(j)-1] ;
				}
			}
			return cost_matrix ;
		}
	}


	public static void main(String[] args) throws Exception {
		String fichier="Donnees.json";
		Gson json0=new GsonBuilder().setPrettyPrinting().create();
		Writer ecriture=new FileWriter(fichier);
		CreationJson crea=new CreationJson();
		Json json= new Json(crea.getCapacites(), crea.getCoutFixeEntrepot(), crea.getDemandes(), crea.getCostmatrix(), crea.getNbEntrepots(), crea.getNbClients());
		json0.toJson(json,ecriture);
		ecriture.flush();
		ecriture.close();

	}

}
