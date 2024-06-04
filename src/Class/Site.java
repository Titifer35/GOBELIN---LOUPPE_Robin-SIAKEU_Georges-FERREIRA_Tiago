package Class;


public class Site {
	
	private int Idsite;
	private int x;
	private int y;
	/**
	 * @param idsite
	 * @param x
	 * @param y
	 */
	public Site(int Idsite, int x, int y) {
		this.Idsite = Idsite;
		this.x = x;
		this.y = y;
	}
	public int getIdsite() {
		return Idsite;
	}
	public void setIdsite(int Idsite) {
		this.Idsite = Idsite;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}
