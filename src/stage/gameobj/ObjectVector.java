package stage.gameobj;

public final class ObjectVector {
	private int x, y, z;
	private String name;

	public ObjectVector(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		name = "";
	}

	public ObjectVector(int x, int y, int z, String name) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.name = name;
	}

	public void setVector(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void addVector(int diffX, int diffY, int diffZ) {
		x += diffX;
		y += diffY;
		z += diffZ;
	}

	public void addVector(ObjectVector v) {
		this.addVector(v.getX(), v.getY(), v.getZ());
	}

	public void subtractVector(ObjectVector v) {
		this.addVector(-v.getX(), -v.getY(), -v.getZ());
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		// This equals method does not prevent comparing with subClass

		if (obj == null)
			return false;
		if (!(obj instanceof ObjectVector))
			return false;
		if (obj == this)
			return true;
		ObjectVector objVector = (ObjectVector) obj;

		return x == objVector.getX() && y == objVector.getY() && z == objVector.getZ()
				&& name.equals(objVector.getName());

	}

	@Override
	public int hashCode() {
		// This hashCode Function is a reasonable good implementation (for random pattern of data) proposed
		// by Josh Bloch's Effective Java in item 8.
		int result = 31;
		return 37 * result + (x + y + z + name.hashCode());

	}

}
