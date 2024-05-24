
public class Site {
	
	private int idsite;
	private int x;
	private int y;
	/**
	 * @param idsite
	 * @param x
	 * @param y
	 */
	public Site(int idsite, int x, int y) {
		this.idsite = idsite;
		this.x = x;
		this.y = y;
	}
	public int getIdsite() {
		return idsite;
	}
	public void setIdsite(int idsite) {
		this.idsite = idsite;
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
