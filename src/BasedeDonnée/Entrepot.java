package BasedeDonnée;


public class Entrepot {
	
	private int Identrepot;
	private int Idsite;
	private int coutfixe;
	private int stock;
	private boolean disponible;
	/**
	 * @param identrepot
	 * @param idsite
	 * @param coutfixe
	 * @param stock
	 */
	public Entrepot(int Identrepot, int Idsite, int coutfixe, int stock, boolean disponible) {
		this.Identrepot = Identrepot;
		this.Idsite = Idsite;
		this.coutfixe = coutfixe;
		this.stock = stock;
		this.disponible = disponible;
	}

	public int getIdentrepot() {
		return Identrepot;
	}
	public void setIdentrepot(int Identrepot) {
		this.Identrepot = Identrepot;
	}
	public int getIdsite() {
		return Idsite;
	}
	public void setIdsite(int Idsite) {
		this.Idsite = Idsite;
	}
	public int getCoutfixe() {
		return coutfixe;
	}
	public void setCoutfixe(int coutfixe) {
		this.coutfixe = coutfixe;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public boolean getDisponible() {
		return disponible;
	}
	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
}