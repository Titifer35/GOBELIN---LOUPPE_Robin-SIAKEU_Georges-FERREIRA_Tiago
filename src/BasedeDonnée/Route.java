package BasedeDonnée;


public class Route {

	private int origine;
	private int destination;
	private int xo, yo, xd, yd;
	/**
	 * @param origine
	 * @param destination
	 */
	public Route(int origine, int destination, int xo, int yo, int xd, int yd) {
		this.origine = origine;
		this.destination = destination;
		this.xo = xo;
		this.yo = yo;
		this.xd = xd;
		this.yd = yd;
	}

	// Getters and setters
	public int getOrigine() {
		return this.origine;
	}
	public void setOrigine(int origine) {
		this.origine = origine;
	}
	public int getDestination() {
		return this.destination;
	}
	public void setDestination(int destination) {
		this.destination = destination;
	}

	public int getXo() { 
		return this.xo; 
	}
	
	public int getYo() { 
		return this.yo; 
	}
	
	public int getXd() { 
		return this.xd; 
	}
	
	public int getYd() { 
		return this.yd; 
	}

	public int getDistance() {
		// Calculate distance (Euclidean distance)
		return (int) Math.sqrt(Math.pow((xd - xo), 2) + Math.pow((yd - yo), 2));
	}
	
}
