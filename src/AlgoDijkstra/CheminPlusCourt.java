package AlgoDijkstra;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CheminPlusCourt {

	private static final String PILOTE = "org.hsqldb.jdbcDriver";
	private static final String URL = "jdbc:hsqldb:database" + File.separator + "basic;shutdown=true";
	private static final String LOGIN = "sa";
	private static final String PASSWORD = "";
	
	public int[][] Chemin_Plus_Court(List<Integer> sites) throws SQLException, ClassNotFoundException {
	    
		Class.forName(PILOTE);
	
	    int[][] matrice_cout = new int[sites.size()][sites.size()];

	    try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
	        String requete;
	        int cout;

	        // Initialisation de la matrice des coûts
	        for (int i = 0; i < sites.size(); i++) {
	            for (int j = 0; j < sites.size(); j++) {
	                if (i == j) {
	                    matrice_cout[i][j] = 0;
	                } else {
	                    requete = "SELECT distance FROM ROUTES WHERE destination = " + sites.get(j) + " AND origine = " + sites.get(i) + ";";
	                    try (Statement statement = connection.createStatement()) {
	                        ResultSet resultat = statement.executeQuery(requete);
	                        if (resultat.next()) {
	                            cout = resultat.getInt("distance");
	                        } else {
	                            cout = Integer.MAX_VALUE / 2;
	                        }
	                    }

	                    if (cout != 0 && matrice_cout[i][j] == 0) {
	                        matrice_cout[i][j] = cout;
	                        matrice_cout[j][i] = cout;
	                    }
	                }
	            }
	        }
	    }

	    // Calcul des plus courts chemins (algorithme de Floyd-Warshall)
	    int numSites = sites.size();
	    for (int n = 0; n < numSites; n++) {
	        for (int i = 0; i < numSites; i++) {
	            for (int j = 0; j < numSites; j++) {
	                if (matrice_cout[i][n] + matrice_cout[n][j] < matrice_cout[i][j]) {
	                    matrice_cout[i][j] = matrice_cout[i][n] + matrice_cout[n][j];
	                }
	            }
	        }
	    }

	    return matrice_cout;
	}

}
