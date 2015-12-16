package stage.gameobj;

import core.geom.Vector3;

/**
 * This class represents the object position in x y z dimension. It is used by {@link ObjectMap} as the key of the
 * HashMap.
 * 
 * @author Thanat
 *
 */
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

	/**
	 * set the objectVector of particular object
	 * 
	 * @param x
	 *            x position
	 * @param y
	 *            y position
	 * @param z
	 *            z position
	 */
	public void setVector(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Add the vector with new Vector
	 * 
	 * @param diffX
	 *            vector that need to be added in x direction
	 * @param diffY
	 *            vector that need to be added in y direction
	 * @param diffZ
	 *            vector that need to be added in z direction
	 */
	public void addVector(int diffX, int diffY, int diffZ) {
		x += diffX;
		y += diffY;
		z += diffZ;
	}

	/**
	 * add the vector by vector in Vector manner
	 * 
	 * @param v
	 */
	public void addVector(ObjectVector v) {
		this.addVector(v.getX(), v.getY(), v.getZ());
	}

	/**
	 * substract vector in the Vector manner with input vector
	 * 
	 * @param v
	 */
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

	/**
	 * The equals method must be override because this class will be used as the key of the hashmap.Therefore it must be
	 * modified in order that objectVector with the same x y z and name is counted as the same object
	 */
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

	/**
	 * This method is used to convert x y z position into Vector3D type
	 * 
	 * @return vector3 of the object
	 */
	public Vector3 toVector3() {
		return new Vector3(x, y, z);
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}

	/**
	 * hasCode is overrided together with equals method in order to make this method and equals method consistent. This
	 * hashCode Function is a reasonable good implementation (for random pattern of data) proposed by Josh Bloch's
	 * Effective Java in item 8.
	 */
	@Override
	public int hashCode() {
		// This hashCode Function is a reasonable good implementation (for random pattern of data) proposed
		// by Josh Bloch's Effective Java in item 8.
		int result = 31;
		return 37 * result + (x + y + z + name.hashCode());

	}

}
