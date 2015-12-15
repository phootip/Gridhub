package core.geom;

/**
 * The 3-dimensional vector (or 3-tuple) for easier geometry operation. This class support method chaining.
 * 
 * @author Kasidit Iamthong
 *
 */
public final class Vector3 {

	/**
	 * A zero vector (a vector that has all of its component value equals to zero).
	 */
	public static final Vector3 ZERO = new Vector3(0, 0, 0);

	private float x;
	private float y;
	private float z;

	/**
	 * Get the value of x of this vector.
	 * 
	 * @return The value of x.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Set the value of x of this vector.
	 * 
	 * @param x
	 *            the value to set.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Get the value of y of this vector.
	 * 
	 * @return The value of y.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Set the value of y of this vector.
	 * 
	 * @param y
	 *            the value to set.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Get the value of z of this vector.
	 * 
	 * @return The value of z.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Set the value of z of this vector.
	 * 
	 * @param z
	 *            the value to set.
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Create a 3-dimensional vector object with value ({@code x}, {@code y}, {@code z}).
	 * 
	 * @param x
	 *            the value of x.
	 * @param y
	 *            the value of y.
	 * @param z
	 *            the value of z.
	 */
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Do a copy of existing vector.
	 * 
	 * @param v
	 *            the vector to be copied.
	 */
	public Vector3(Vector3 v) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
	}

	/**
	 * Add this vector with specified value.
	 * 
	 * @param x
	 *            the value of x
	 * @param y
	 *            the value of y
	 * @param z
	 *            the value of z
	 * @return This object itself for method chaining.
	 */
	public Vector3 add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;

		return this;
	}

	/**
	 * Add this vector with another vector.
	 * 
	 * @param v
	 *            another vector that its value will be used for addition.
	 * @return This object itself for method chaining.
	 */
	public Vector3 add(Vector3 v) {
		return this.add(v.getX(), v.getY(), v.getZ());
	}

	/**
	 * Subtract this vector with specified value. This is equivalent to adding by negation of these values.
	 * 
	 * @param x
	 *            the value of x
	 * @param y
	 *            the value of y
	 * @param z
	 *            the value of z
	 * @return This object itself for method chaining.
	 */
	public Vector3 subtract(float x, float y, float z) {
		return this.add(-x, -y, -z);
	}

	/**
	 * Subtract this vector with specified value. This is equivalent to adding by negation of these values.
	 * 
	 * @param v
	 *            another vector that its value will be used for subtraction.
	 * @return This object itself for method chaining.
	 */
	public Vector3 subtract(Vector3 v) {
		return this.subtract(v.getX(), v.getY(), v.getZ());
	}

	/**
	 * Multiply this vector by a constant factor.
	 * 
	 * @param factor
	 *            the factor value
	 * @return This object itself for method chaining.
	 */
	public Vector3 multiply(float factor) {
		this.x *= factor;
		this.y *= factor;
		this.z *= factor;
		return this;
	}

	/**
	 * Multiply each component of this vector by a constant factor.
	 * 
	 * @param factorX
	 *            the factor value to be multiplied with x value
	 * @param factorY
	 *            the factor value to be multiplied with y value
	 * @param factorZ
	 *            the factor value to be multiplied with z value
	 * @return This object itself for method chaining.
	 */
	public Vector3 multiply(float factorX, float factorY, float factorZ) {
		this.x *= factorX;
		this.y *= factorY;
		this.z *= factorZ;
		return this;
	}

	/**
	 * Negate the value of this vector. This is equivalent to multiplying vector by -1.
	 * 
	 * @return This object itself for method chaining.
	 */
	public Vector3 negate() {
		this.x *= -1;
		this.y *= -1;
		this.z *= -1;

		return this;
	}

	/**
	 * Rotate this vector in plane X-Y plane with the specified degree, according to the right-hand rule.
	 * 
	 * @param angle
	 *            the specified angle in radian.
	 * @return This object itself for method chaining.
	 * @see Vector3#rotateYZ
	 * @see Vector3#rotateXZ
	 */
	public Vector3 rotateXY(double angle) {
		float temp = this.x;
		this.x = (float) (Math.cos(angle) * temp - Math.sin(angle) * this.y);
		this.y = (float) (Math.sin(angle) * temp + Math.cos(angle) * this.y);

		return this;
	}

	/**
	 * Rotate this vector in plane Y-Z plane with the specified degree, according to the right-hand rule.
	 * 
	 * @param angle
	 *            the specified angle in radian.
	 * @return This object itself for method chaining.
	 * @see Vector3#rotateXY
	 * @see Vector3#rotateXZ
	 */
	public Vector3 rotateYZ(double angle) {
		float temp = this.y;
		this.y = (float) (Math.cos(angle) * temp - Math.sin(angle) * this.z);
		this.z = (float) (Math.sin(angle) * temp + Math.cos(angle) * this.z);

		return this;
	}

	/**
	 * Rotate this vector in plane X-Z plane with the specified degree, according to the right-hand rule.
	 * 
	 * @param angle
	 *            the specified angle in radian.
	 * @return This object itself for method chaining.
	 * @see Vector3#rotateXY
	 * @see Vector3#rotateYZ
	 */
	public Vector3 rotateXZ(double angle) {
		float temp = this.x;
		this.x = (float) (Math.cos(angle) * temp - Math.sin(angle) * this.z);
		this.z = (float) (Math.sin(angle) * temp + Math.cos(angle) * this.z);

		return this;
	}

	/**
	 * Calculate the length of the vector. This is computationally expensive than {@link #getLengthSquared()}.
	 * 
	 * @return the length of the vector.
	 * @see Vector3#getLengthSquared()
	 */
	public double getLength() {
		return Math.sqrt(getLengthSquared());
	}

	/**
	 * Calculate the square of length of the vector.
	 * 
	 * @return the squared length of the vector.
	 * @see Vector3#getLength()
	 */
	public double getLengthSquared() {
		return x * x + y * y + z * z;
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Vector3))
			return false;
		Vector3 other = (Vector3) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}

}
