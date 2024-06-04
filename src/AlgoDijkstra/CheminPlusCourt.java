package AlgoDijkstra;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CheminPlusCourt {

    // Méthode pour calculer la matrice des chemins les plus courts
    public int[][] calculerMatriceDesCheminsLesPlusCourts(List<Integer> sites) throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:database" + File.separator + "basic;shutdown=true";
        String utilisateur = "sa";
        String motDePasse = "";
        int[][] matriceDesCouts = new int[sites.size()][sites.size()];

        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse)) {
            initialiserDistances(connexion, matriceDesCouts, sites);
            appliquerFloydWarshall(matriceDesCouts, sites.size());
        }

        return matriceDesCouts;
    }

    // Initialiser la matrice de distance avec les distances directes de la base de données
    private void initialiserDistances(Connection connexion, int[][] matriceDesCouts, List<Integer> sites) throws SQLException {
        for (int i = 0; i < sites.size(); i++) {
            for (int j = 0; j < sites.size(); j++) {
                if (i == j) {
                    matriceDesCouts[i][j] = 0;
                } else {
                    String requete = String.format("SELECT distance FROM ROUTES WHERE origine = %d AND destination = %d;", sites.get(i), sites.get(j));
                    try (Statement instruction = connexion.createStatement();
                         ResultSet resultat = instruction.executeQuery(requete)) {
                        if (resultat.next()) {
                            matriceDesCouts[i][j] = resultat.getInt("distance");
                        } else {
                            matriceDesCouts[i][j] = Integer.MAX_VALUE / 2; // Utiliser un grand nombre pour représenter l'absence de chemin direct
                        }
                    }
                }
            }
        }
    }

    // Appliquer l'algorithme de Floyd-Warshall pour calculer les chemins les plus courts entre toutes les paires de noeuds
    private void appliquerFloydWarshall(int[][] matriceDesCouts, int taille) {
        for (int k = 0; k < taille; k++) {
            for (int i = 0; i < taille; i++) {
                for (int j = 0; j < taille; j++) {
                    if (matriceDesCouts[i][k] + matriceDesCouts[k][j] < matriceDesCouts[i][j]) {
                        matriceDesCouts[i][j] = matriceDesCouts[i][k] + matriceDesCouts[k][j];
                    }
                }
            }
        }
    }
}
