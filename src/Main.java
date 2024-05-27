import java.io.FileWriter;
import java.io.Writer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import java.io.File;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {
		// Print banner
		System.out.println("+------------------------------+");
		System.out.println("|           GOBLIN             |");
		System.out.println("+------------------------------+");

		Scanner scanner = new Scanner(System.in);
		int choixUtilisateur = 0;
		LectureCSV gestionnaireCSV = new LectureCSV();
		String dossierChoisi = "";
		String nomBordereau = "";

		BasesJDBC.main(null); // Initialisation de la base de données

		while (choixUtilisateur != 1 && choixUtilisateur != 2 && choixUtilisateur != 3) {
			System.out.println("Sur quel fichier souhaitez-vous travailler ?");
			System.out.println(" 1 : Petite taille");
			System.out.println(" 2 : Taille moyenne");
			System.out.println(" 3 : Un peu plus grand");
			choixUtilisateur = scanner.nextInt();

			switch (choixUtilisateur) {
			case 1:
				dossierChoisi = "Petite taille";
				break;
			case 2:
				dossierChoisi = "Taille moyenne";
				break;
			case 3:
				dossierChoisi = "Un peu plus grand";
				break;
			default:
				System.out.println("Choix non valide, veuillez réessayer.");
				break;
			}
		}

		System.out.println("Veuillez saisir le nom du bordereau :");
		nomBordereau = scanner.next();
		String cheminBordereau = dossierChoisi + File.separator + nomBordereau;
		File fichierBordereau = new File(cheminBordereau);

		while (!fichierBordereau.exists()) {
			System.out.println("Le bordereau saisi n'existe pas, veuillez réessayer :");
			nomBordereau = scanner.next();
			cheminBordereau = dossierChoisi + File.separator + nomBordereau;
			fichierBordereau = new File(cheminBordereau);
		}

		String fichierSites = dossierChoisi + File.separator + "init-sites.csv";
		String fichierClients = dossierChoisi + File.separator + "init-clients.csv";
		String fichierEntrepots = dossierChoisi + File.separator + "init-entrepots.csv";
		String fichierRoutes = dossierChoisi + File.separator + "init-routes.csv";

		try {
			gestionnaireCSV.importSitesToDatabase(fichierSites);
			gestionnaireCSV.importClientsToDatabase(fichierClients, cheminBordereau);
			gestionnaireCSV.importEntrepotsToDatabase(fichierEntrepots, cheminBordereau);
			gestionnaireCSV.importRoutesToDatabase(fichierRoutes);
		} catch (Exception e) {
			System.out.println("Erreur lors du chargement des données : " + e.getMessage());
			return;
		}

		CreationJson.main(null);
		//TrouverSolution.main(null);

		choixUtilisateur = 0;
		while (choixUtilisateur != 1 && choixUtilisateur != 2) {
			System.out.println("Voulez-vous valider la solution ? 1 : Oui / 2 : Non");
			choixUtilisateur = scanner.nextInt();
			if (choixUtilisateur == 1) {
				System.out.println("Merci d'avoir validé notre solution.");
				break;
			} else if (choixUtilisateur == 2) {
				System.out.println("Vous n'avez pas validé la solution, veuillez relancer le programme pour réessayer.");
				break;
			} else {
				System.out.println("Saisie incorrecte, veuillez réessayer.");
			}
		}

		System.out.println("À bientôt.");
		scanner.close();
	}
}
