package geom;

/**
 * The 2-dimensional vector (or 2-tuple) for easier geometry operation. This
 * class support method chaining.
 * 
 * @author Kasidit Iamthong
 *
 */
public class Vector2 {

	private float x;
	private float y;

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
	 * Create a 2-dimensional vector object with value ({@code x}, {@code y}).
	 * 
	 * @param x
	 *            the value of x.
	 * @param y
	 *            the value of y.
	 */
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Add this vector with specified value.
	 * 
	 * @param x
	 *            the value of x
	 * @param y
	 *            the value of y
	 * @return This object itself for method chaining.
	 */
	public Vector2 add(float x, float y) {
		this.x += x;
		this.y += y;

		return this;
	}

	/**
	 * Add this vector with another vector.
	 * 
	 * @param v
	 *            another vector that its value will be used for addition.
	 * @return This object itself for method chaining.
	 */
	public Vector2 add(Vector2 v) {
		return this.add(v.getX(), v.getY());
	}

	/**
	 * Subtract this vector with specified value. This is equivalent to adding
	 * by negation of these values.
	 * 
	 * @param x
	 *            the value of x
	 * @param y
	 *            the value of y
	 * @return This object itself for method chaining.
	 */
	public Vector2 subtract(float x, float y) {
		return this.add(-x, -y);
	}

	/**
	 * Subtract this vector with specified value. This is equivalent to adding
	 * by negation of these values.
	 * 
	 * @param v
	 *            another vector that its value will be used for subtraction.
	 * @return This object itself for method chaining.
	 */
	public Vector2 subtract(Vector2 v) {
		return this.subtract(v.x, v.y);
	}

	/**
	 * Multiply this vector by a constant factor.
	 * 
	 * @param factor
	 *            the factor value
	 * @return This object itself for method chaining.
	 */
	public Vector2 multiply(float factor) {
		this.x *= factor;
		this.y *= factor;
		return this;
	}

	/**
	 * Negate the value of this vector. This is equivalent to multiplying vector
	 * by -1.
	 * 
	 * @return This object itself for method chaining.
	 */
	public Vector2 negate() {
		this.x *= -1;
		this.y *= -1;

		return this;
	}

	/**
	 * Rotate this vector 90 degree clockwise. This method is preferred to
	 * {@code rotate()}, which is slower.
	 * 
	 * @return This object itself for method chaining.
	 * @see Vector2#rotateCCW
	 * @see Vector2#rotate
	 */
	public Vector2 rotateCW() {
		float temp = this.x;
		this.x = -this.y;
		this.y = temp;

		return this;
	}

	/**
	 * Rotate this vector 90 degree counterclockwise. This method is preferred
	 * to {@code rotate()}, which is slower.
	 * 
	 * @return This object itself for method chaining.
	 * @see Vector2#rotateCW
	 * @see Vector2#rotate
	 */
	public Vector2 rotateCCW() {
		float temp = this.x;
		this.x = this.y;
		this.y = -temp;

		return this;
	}

	/**
	 * Rotate this vector counterclockwise with the specified degree.
	 * 
	 * @param angle the specified angle in radian.
	 * @return This object itself for method chaining.
	 * @see Vector2#rotateCCW
	 * @see Vector2#rotateCW
	 * @see Vector2#negate
	 */
	public Vector2 rotate(double angle) {
		this.x = (float) (Math.cos(angle) * this.x - Math.sin(angle) * this.y);
		this.y = (float) (Math.sin(angle) * this.x + Math.cos(angle) * this.y);
		
		return this;
	}

}
