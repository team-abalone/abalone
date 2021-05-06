public class Hexagon {
	private Marble marble;
	private HexCoordinate position;
	private int id;

	public Hexagon(HexCoordinate coordinate, int id) {
		this.position = coordinate;
		this.id = id;
	}

	public Marble getMarble() {
		return marble;
	}

	public void setMarble(Marble marble) {
		this.marble = marble;
	}

	public HexCoordinate getPosition() {
		return position;
	}

	public int getId() {
		return id;
	}
}
