import java.util.LinkedList;

public class LectureCSV {
	
	private LinkedList<Route> Routes;
	private LinkedList<Site> Sites;
	private LinkedList<Entrepot> Entrepots;
	private LinkedList<Client> Clients;
	
	
	public LectureCSV(LinkedList<Route> routes, LinkedList<Site> sites, LinkedList<Entrepot> entrepots,
			LinkedList<Client> clients) {
		Routes = routes;
		Sites = sites;
		Entrepots = entrepots;
		Clients = clients;
	}
}
