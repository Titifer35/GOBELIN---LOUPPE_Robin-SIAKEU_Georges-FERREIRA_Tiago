package Class;


public class Client {

	private String nom;
	private String mail;
	private int Idsite;
	private int demande;
	

	public Client(String nom, String mail, int Idsite, int demande) {
		this.nom = nom;
		this.mail = mail;
		this.Idsite = Idsite;
		this.demande = demande;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public int getIdsite() {
		return Idsite;
	}
	public void setIdsite(int Idsite) {
		this.Idsite = Idsite;
	}
	public int getDemande() {
		return demande;
	}
	public void setDemande(int demande) {
		this.demande = demande;
	}
	

}
