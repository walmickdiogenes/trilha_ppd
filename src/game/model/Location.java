package game.model;

/*
 * @author Walmick Diogenes
 */
public class Location {

	private final int x;
	private final int y;

	/*
	 * Construtor para um objeto de localização que define a localização na forma (x, y)
	 * 
	 * @param x
	 *           posição na direção x
	 * @param y
	 *           posição na direção y
	 */
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return (x + 1) * (y + 1);
	}

	@Override
	public String toString() {
		return "Location{" + "x=" + x + ", y=" + y + "}";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Location) {
			Location other = (Location) obj;
			return this.x == other.x && this.y == other.y;
		}
		return false;
	}

}
