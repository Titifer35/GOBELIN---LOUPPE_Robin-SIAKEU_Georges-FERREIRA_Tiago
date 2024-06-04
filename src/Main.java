import java.io.File;
import java.util.Scanner;
import BasedeDonnée.BasesJDBC;
import BasedeDonnée.CreationJson;
import BasedeDonnée.LectureCSV;

import BasedeDonnée.BasesJDBC;
import BasedeDonnée.CreationJson;
import BasedeDonnée.LectureCSV;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("+------------------------------+");
        System.out.println("|                              |");
        System.out.println("|          GOBLIN              |");
        System.out.println("|                              |");
        System.out.println("+------------------------------+\n");

        Scanner scannerUtilisateur = new Scanner(System.in);
        int choixDeLUtilisateur = 0;
        LectureCSV gestionnaireDeCSV = new LectureCSV();
        String dossierSelectionne = "";
        String nomDuBordereau = "";

        BasesJDBC.main(null);
        
        

        while (choixDeLUtilisateur < 1 || choixDeLUtilisateur > 3) {
            System.out.println("Choisissez le fichier sur lequel vous souhaitez travailler :");
            System.out.println(" 1 : Petite taille");
            System.out.println(" 2 : Taille moyenne");
            System.out.println(" 3 : Un peu plus grand");
            System.out.print("Votre choix (1-3) : ");
            while (!scannerUtilisateur.hasNextInt()) {
                System.out.println("Veuillez entrer un nombre valide !");
                scannerUtilisateur.next();
            }
            choixDeLUtilisateur = scannerUtilisateur.nextInt();

            switch (choixDeLUtilisateur) {
                case 1: dossierSelectionne = "Jeux_de_donnees" + File.separator + "petit"; break;
                case 2: dossierSelectionne = "Jeux_de_donnees" + File.separator + "moyen"; break;
                case 3: dossierSelectionne = "Jeux_de_donnees" + File.separator + "grand"; break;
                default:
                    System.out.println("Choix non valide, veuillez réessayer.");
                    choixDeLUtilisateur = 0; // Réinitialiser le choix
            }
        }

        System.out.print("\nVeuillez saisir le nom du bordereau (ex. init-bordereau-commande-2021-12-25.txt) : ");
        nomDuBordereau = scannerUtilisateur.next();
        String cheminDuBordereau = dossierSelectionne + File.separator + nomDuBordereau;
        File fichierBordereau = new File(cheminDuBordereau);

        while (!fichierBordereau.exists()) {
            System.out.print("Le bordereau saisi n'existe pas. Veuillez réessayer : ");
            nomDuBordereau = scannerUtilisateur.next();
            cheminDuBordereau = dossierSelectionne + File.separator + nomDuBordereau;
            fichierBordereau = new File(cheminDuBordereau);
        }
        while (!fichierBordereau.exists()) {
            System.out.print("Le bordereau saisi n'existe pas. Veuillez réessayer : ");
            nomDuBordereau = scannerUtilisateur.next();
            cheminDuBordereau = dossierSelectionne + File.separator + nomDuBordereau;
            fichierBordereau = new File(cheminDuBordereau);
        }

        String cheminFichierSites = dossierSelectionne + File.separator + "init-sites.csv";
        String cheminFichierClients = dossierSelectionne + File.separator + "init-clients.csv";
        String cheminFichierEntrepots = dossierSelectionne + File.separator + "init-entrepots.csv";
        String cheminFichierRoutes = dossierSelectionne + File.separator + "init-routes.csv";

        try {
            gestionnaireDeCSV.importSitesToDatabase(cheminFichierSites);
            gestionnaireDeCSV.importClientsToDatabase(cheminFichierClients, cheminDuBordereau);
            gestionnaireDeCSV.importEntrepotsToDatabase(cheminFichierEntrepots, cheminDuBordereau);
            gestionnaireDeCSV.importRoutesToDatabase(cheminFichierRoutes);
            System.out.println("\nLes données ont été chargées avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des données : " + e.getMessage());
            scannerUtilisateur.close();
            return;
        }

        CreationJson.main(null); 
        

        System.out.println("\nVoulez-vous valider la solution ?");
        while (true) {
            System.out.print("Entrez 1 pour Oui ou 2 pour Non : ");
            while (!scannerUtilisateur.hasNextInt()) {
                System.out.println("Veuillez entrer un nombre valide !");
                scannerUtilisateur.next();
            }
            choixDeLUtilisateur = scannerUtilisateur.nextInt();
            if (choixDeLUtilisateur == 1) {
                System.out.println("Merci d'avoir validé notre solution.");
                break;
            } else if (choixDeLUtilisateur == 2) {
                System.out.println("Vous n'avez pas validé la solution. Veuillez relancer le programme pour réessayer.");
                break;
            } else {
                System.out.println("Saisie incorrecte, veuillez réessayer.");
            }
        }

        System.out.println("À bientôt.");
        scannerUtilisateur.close();
    }
}
