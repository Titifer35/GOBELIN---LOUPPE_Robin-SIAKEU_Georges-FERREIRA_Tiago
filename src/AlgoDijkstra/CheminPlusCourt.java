package AlgoDijkstra;
import java.util.ArrayList;
import java.util.List;
import BasedeDonnée.Client;
import BasedeDonnée.Entrepot;

public class CheminPlusCourt {
	
	public static class Resultat {
		private int coutTotal;
		
		public Resultat(Client client, Entrepot entrepot, int coutTotal) {
	        this.coutTotal = coutTotal;
	    }
		
	}

	public List<Resultat> plusCourtChemin(List<Entrepot> entrepotsDisponibles, List<Client> clientsDemandants, int[][] coutTransport) {
        List<Resultat> resultats = new ArrayList<>();

        for (Client client : clientsDemandants) {
            int meilleurCout = Integer.MAX_VALUE;
            Entrepot meilleurEntrepot = null;

            for (Entrepot entrepot : entrepotsDisponibles) {
                if (entrepot.getDisponible() && entrepot.getStock() >= client.getDemande()) {
                    int coutLivraison = coutTransport[entrepot.getIdsite()][client.getIdsite()];
                    int coutTotal = entrepot.getCoutfixe() + coutLivraison;

                    if (coutTotal < meilleurCout) {
                        meilleurCout = coutTotal;
                        meilleurEntrepot = entrepot;
                    }
                }
            }

            if (meilleurEntrepot != null) {
                resultats.add(new Resultat(client, meilleurEntrepot, meilleurCout));
                meilleurEntrepot.setStock(meilleurEntrepot.getStock() - client.getDemande());
            }
        }

        return resultats;
	}

}
