
public class Main {
    public static void main(String[] args) {
        System.out.println("+------------------------------+");
        System.out.println("|           GOBLIN             |");
        System.out.println("+------------------------------+");

        LectureCSV csvImporter = new LectureCSV();
        try {
            System.out.println("Importing sites...");
            csvImporter.importSitesFromCSV("init-sites-30-Carre.csv");
            System.out.println("Sites imported successfully.");

            System.out.println("Importing clients and orders...");
            csvImporter.importClientsFromCSV("/path/to/clients.csv", "/path/to/orders.csv");
            System.out.println("Clients and orders imported successfully.");

            System.out.println("Importing entrepots...");
            csvImporter.importEntrepotsFromCSV("/path/to/entrepots.csv", "/path/to/availability.csv");
            System.out.println("Entrepots imported successfully.");

            System.out.println("Importing routes...");
            csvImporter.importRoutesFromCSV("/path/to/routes.csv");
            System.out.println("Routes imported successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to import data due to an error.");
        }
    }
}
