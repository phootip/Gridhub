package core.geom;

import java.util.HashMap;

/**
 * The 2-dimensional vector (or 2-tuple) for easier geometry operation. This class support method chaining.
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
	 * Get the value of x of this vector, casted as integer.
	 * 
	 * @return The value of x, casted into integer.
	 */
	public int getIntX() {
		return (int) x;
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
	 * Get the value of y of this vector, casted as integer.
	 * 
	 * @return The value of y, casted into integer.
	 */
	public int getIntY() {
		return (int) y;
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
	 * Do a copy of existing vector.
	 * 
	 * @param v
	 *            the vector to be copied.
	 */
	public Vector2(Vector2 v) {
		this.x = v.getX();
		this.y = v.getY();
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
	 * Subtract this vector with specified value. This is equivalent to adding by negation of these values.
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
	 * Subtract this vector with specified value. This is equivalent to adding by negation of these values.
	 * 
	 * @param v
	 *            another vector that its value will be used for subtraction.
	 * @return This object itself for method chaining.
	 */
	public Vector2 subtract(Vector2 v) {
		return this.subtract(v.getX(), v.getY());
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
	 * Multiply each component of this vector by a constant factor.
	 * 
	 * @param factorX
	 *            the factor value to be multiplied with x value
	 * @param factorY
	 *            the factor value to be multiplied with y value
	 * @return This object itself for method chaining.
	 */
	public Vector2 multiply(float factorX, float factorY) {
		this.x *= factorX;
		this.y *= factorY;
		return this;
	}

	/**
	 * Negate the value of this vector. This is equivalent to multiplying vector by -1.
	 * 
	 * @return This object itself for method chaining.
	 */
	public Vector2 negate() {
		this.x *= -1;
		this.y *= -1;

		return this;
	}

	/**
	 * Rotate this vector 90 degree clockwise. This method is preferred to {@code rotate()}, which is slower.
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
	 * Rotate this vector 90 degree counterclockwise. This method is preferred to {@code rotate()}, which is slower.
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

	private static HashMap<Float, Float> sinCache = new HashMap<>();
	private static HashMap<Float, Float> cosCache = new HashMap<>();

	/**
	 * Rotate this vector counterclockwise with the specified degree.
	 * 
	 * @param angle
	 *            the specified angle in radian.
	 * @return This object itself for method chaining.
	 * @see Vector2#rotateCCW
	 * @see Vector2#rotateCW
	 * @see Vector2#negate
	 */
	public Vector2 rotate(float angle) {
		float temp = this.x;

		float sinAngle, cosAngle;
		if (sinCache.containsKey(angle)) {
			sinAngle = sinCache.get(angle);
			cosAngle = cosCache.get(angle);
		} else {
			sinAngle = (float) Math.sin(angle);
			cosAngle = (float) Math.cos(angle);
			sinCache.put(angle, sinAngle);
			cosCache.put(angle, cosAngle);
		}

		this.x = (float) (Math.cos(angle) * temp - Math.sin(angle) * this.y);
		this.y = (float) (Math.sin(angle) * temp + Math.cos(angle) * this.y);

		return this;
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}

}
