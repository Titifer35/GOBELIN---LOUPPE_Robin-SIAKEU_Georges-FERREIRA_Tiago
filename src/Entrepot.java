
public class Entrepot {
	
	private int identrepot;
	private int idsite;
	private int coutfixe;
	private int stock;
	private boolean disponible;
	/**
	 * @param identrepot
	 * @param idsite
	 * @param coutfixe
	 * @param stock
	 */
	public Entrepot(int identrepot, int idsite, int coutfixe, int stock, boolean disponible) {
		this.identrepot = identrepot;
		this.idsite = idsite;
		this.coutfixe = coutfixe;
		this.stock = stock;
		this.disponible = disponible;
	}

	public int getIdentrepot() {
		return identrepot;
	}
	public void setIdentrepot(int identrepot) {
		this.identrepot = identrepot;
	}
	public int getIdsite() {
		return idsite;
	}
	public void setIdsite(int idsite) {
		this.idsite = idsite;
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