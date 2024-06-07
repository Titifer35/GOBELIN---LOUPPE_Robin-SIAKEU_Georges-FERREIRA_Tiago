package BasedeDonnée;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import Class.Client;
import Class.Entrepot;
import Class.Route;
import java.sql.SQLException;

public class LectureCSV {

	private static final String PILOTE = "org.hsqldb.jdbcDriver";
	private static final String URL = "jdbc:hsqldb:database" + File.separator + "basic;shutdown=true";
	private static final String LOGIN = "sa";
	private static final String PASSWORD = "";
	private List<Route> routes;
	private List<Entrepot> entrepots;
	private List<Client> clients;

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------//

	public LectureCSV() {
		this.clients = new LinkedList<>();
		this.routes = new LinkedList<>();
		this.entrepots = new LinkedList<>();
	}

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------//

	public void importSitesToDatabase(String siteFolder) throws ClassNotFoundException, SQLException, CsvValidationException, NumberFormatException, IOException {
		
		Class.forName(PILOTE);
		
		String requete = "";
		
		try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
			CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
			FileReader reader = new FileReader(siteFolder);
			CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).withSkipLines(1).build();
			String[] nextLine;

			while ((nextLine = csvReader.readNext()) != null) {
				int idsite = Integer.parseInt(nextLine[0]);
				int x = Integer.parseInt(nextLine[1]);
				int y = Integer.parseInt(nextLine[2]);
				requete = "INSERT INTO SITES(Idsite, x, y) VALUES (" + idsite + "," + x + "," + y + ");";

				try (Statement statement = connection.createStatement()) {
					statement.executeUpdate(requete);
				}
			}
		}
	}

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------//

	public void importClientsToDatabase(String clientFolder, String bordereau) throws ClassNotFoundException, SQLException {
		
		Class.forName(PILOTE);
		
		String requete = "";
		
		Map<String, Integer> clientOrders = new HashMap<>();
		List<String> fileContents = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(bordereau));
			String line;
			while ((line = reader.readLine()) != null) {
				fileContents.add(line);
			}
			reader.close();
		} catch (Exception exep) {
			exep.printStackTrace();
		}
		int clientCount = Integer.parseInt(fileContents.get(2));
		for (int i = 0; i < clientCount; i++) {
			String[] order = fileContents.get(i + 3).split(" : ");
			String email = order[0];
			int demand = Integer.parseInt(order[1]);
			clientOrders.put(email, demand);
		}
		try {
			CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
			FileReader reader = new FileReader(clientFolder);
			CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).withSkipLines(1).build();
			String[] nextLine;
			while ((nextLine = csvReader.readNext()) != null) {
				String nom = nextLine[0];
				String mail = nextLine[1];
				int idsite = Integer.parseInt(nextLine[2]);
				int demande = clientOrders.getOrDefault(mail, 0);
				this.clients.add(new Client(nom, mail, idsite, demande));
			}
		} catch (Exception exep) {
			exep.printStackTrace();
		}
		try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
			for (Client client : this.clients) {
				requete = "INSERT INTO CLIENTS(mail, Idsite, nom, demande) VALUES ('" + client.getMail() + "'," + client.getIdsite() + ",'" + client.getNom() + "'," + client.getDemande() + ");";
				try (Statement statement = connection.createStatement()) {
					statement.executeUpdate(requete);
				}
			}
		}
	}

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------//

	public void importEntrepotsToDatabase(String entrepotFilePath, String bordereauFilePath) throws SQLException, ClassNotFoundException {
		
		Class.forName(PILOTE);
		
		String requete = "";
		
		List<Integer> openEntrepots = new LinkedList<>(); 
		String line;
		boolean isAvailable; 
		List<String> fileContent = new ArrayList<>();

		// Lecture du fichier bordereau pour obtenir la liste des entrepôts ouverts
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(bordereauFilePath));
			while ((line = bufferedReader.readLine()) != null) {
				fileContent.add(line);
			}
			bufferedReader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// Conversion des identifiants des entrepôts ouverts en entiers
		String[] availability = fileContent.get(fileContent.size() - 1).split(",");
		for (String s : availability) {
			openEntrepots.add(Integer.parseInt(s));
		}

		// Lecture du fichier CSV des entrepôts et création des objets Entrepot
		try {
			CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
			FileReader fileReader = new FileReader(entrepotFilePath);
			CSVReader csvReader = new CSVReaderBuilder(fileReader).withCSVParser(parser).withSkipLines(1).build();
			String[] nextLine;
			while ((nextLine = csvReader.readNext()) != null) {
				int entrepotId = Integer.parseInt(nextLine[0]);
				int siteId = Integer.parseInt(nextLine[1]);
				int fixedCost = Integer.parseInt(nextLine[2]);
				int stock = Integer.parseInt(nextLine[3]);
				isAvailable = openEntrepots.contains(entrepotId);
				this.entrepots.add(new Entrepot(entrepotId, siteId, fixedCost, stock, isAvailable));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Insertion des entrepôts dans la base de données
		try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
			for (Entrepot entrepot : this.entrepots) {
				int entrepotId = entrepot.getIdentrepot();
				int siteId = entrepot.getIdsite();
				int fixedCost = entrepot.getCoutfixe();
				int stock = entrepot.getStock();
				isAvailable = entrepot.getDisponible();
				requete = "INSERT INTO ENTREPOTS (Identrepot, Idsite, stock, coutfixe, disponible) VALUES (" 
						+ entrepotId + "," + siteId + "," + stock + "," + fixedCost + "," + (isAvailable ? 1 : 0) + ");";
				try (Statement statement = connection.createStatement()) {
					statement.executeUpdate(requete);
				}
			}
		}
	}

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------//

	public void importRoutesToDatabase(String routesFile) throws SQLException, ClassNotFoundException {
		
		Class.forName(PILOTE);
		
		String requete = "";
		
		try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
			CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
			FileReader reader = new FileReader(routesFile);
			CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).withSkipLines(1).build();
			String[] nextLine;
			int originX;
			int originY;
			int destinationX;
			int destinationY;
			while ((nextLine = csvReader.readNext()) != null) {
				int origin = Integer.parseInt(nextLine[0]);
				int destination = Integer.parseInt(nextLine[1]);
				String query = "SELECT x, y FROM SITES WHERE Idsite = " + origin + ";";
				try (Statement statement = connection.createStatement()) {
					try (ResultSet resultSet = statement.executeQuery(query)) {
						resultSet.next();
						originX = resultSet.getInt("x");
						originY = resultSet.getInt("y");
					}
				}
				query = "SELECT x, y FROM SITES WHERE Idsite = " + destination + ";";
				try (Statement statement = connection.createStatement()) {
					try (ResultSet resultSet = statement.executeQuery(query)) {
						resultSet.next();
						destinationX = resultSet.getInt("x");
						destinationY = resultSet.getInt("y");
					}
				}
				this.routes.add(new Route(origin, destination, originX, originY, destinationX, destinationY));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Route route : this.routes) {
			int origine = route.getOrigine();
			int destination = route.getDestination();
			double distance = route.getDistance();
			requete = "INSERT INTO ROUTES(origine, destination, distance) VALUES (" + origine + "," + destination + "," + distance + ");";
			try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
				try (Statement statement = connection.createStatement()) {
					statement.executeUpdate(requete);
				}
			}
		}
	}
}
