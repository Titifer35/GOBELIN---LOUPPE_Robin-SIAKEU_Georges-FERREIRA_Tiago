
public class Client {

	private String nom;
	private String prenom;
	private int idsite;

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public int getIdsite() {
		return idsite;
	}
	public void setIdsite(int idsite) {
		this.idsite = idsite;
	}
	/**
	 * @param nom
	 * @param prenom
	 * @param idsite
	 */
	public Client(String nom, String prenom, int idsite) {
		this.nom = nom;
		this.prenom = prenom;
		this.idsite = idsite;
	}
}
