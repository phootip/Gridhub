package util;

import java.awt.Color;

import core.geom.Vector2;
import core.geom.Vector3;

/**
 * Helper class contains helper method, such as interpolation and color blending.
 * 
 * @author Kasidit Iamthong
 *
 */
public class Helper {

	/**
	 * Get a {@link Color} that has the same color as base color, but has a specified alpha value.
	 * 
	 * @param baseColor
	 *            the base color
	 * @param alpha
	 *            the alpha value ranged from 0 to 255
	 * @return {@link Color} object.
	 */
	public static Color getAlphaColor(Color baseColor, int alpha) {
		return new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha);
	}

	/**
	 * Get a {@link Color} that has the same color as base color, but has a specified alpha value.
	 * 
	 * @param baseColor
	 *            the base color
	 * @param alphaRatio
	 *            the alpha ratio value ranged from 0.0 to 1.0
	 * @return {@link Color} object.
	 */
	public static Color getAlphaColorPercentage(Color baseColor, double alphaRatio) {
		return new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(),
				(int) Math.round(alphaRatio * 255));
	}

	/**
	 * Blend two {@link Color}s with specified ratio by linear interpolation method in each channel.
	 * 
	 * @param a
	 *            the first {@link Color}
	 * @param b
	 *            the second {@link Color}
	 * @param blendRatio
	 *            the ratio of mixing ranged from 0.0 to 1.0, inclusive. Value towards 0.0 gives more weight to first
	 *            color, and vice versa.
	 * @return A blended {@code Color} object.
	 * @throws IllegalArgumentException
	 *             if {@code blendRatio} is out of range.
	 */
	public static Color blendColor(Color a, Color b, float blendRatio) {
		if (blendRatio == 0) {
			return a;
		} else if (blendRatio == 1) {
			return b;
		} else if (blendRatio < 0 || blendRatio > 1) {
			throw new IllegalArgumentException("Invalid blend ratio: " + blendRatio);
		} else {
			return new Color((int) interpolate(a.getRed(), b.getRed(), blendRatio),
					(int) interpolate(a.getGreen(), b.getGreen(), blendRatio),
					(int) interpolate(a.getBlue(), b.getBlue(), blendRatio),
					(int) interpolate(a.getAlpha(), b.getAlpha(), blendRatio));
		}
	}

	/**
	 * Calculate interpolation between 0.0 and 1.0 using sine function.
	 * 
	 * @param t
	 *            the x-axis position in the graph, ranged from {@code 0.0f} and {@code 1.0f}, inclusive.
	 * @param easeIn
	 *            control whether or not the starting point slope is zero.
	 * @param easeOut
	 *            control whether or not the end point slope is zero.
	 * @return Value between {@code 0.0f} and {@code 1.0f}, inclusive.
	 * @throws IllegalArgumentException
	 *             if both {@code easeIn} and {@code easeOut} is both false. In this case, use {@link interpolate}
	 *             instead.
	 */
	public static float sineInterpolate(float t, boolean easeIn, boolean easeOut) {
		if (easeIn && easeOut) {
			return (float) Math.sin(Math.PI * t - Math.PI / 2.0f) / 2.0f + 0.5f;
		} else if (easeIn) {
			return (float) Math.sin(Math.PI * t / 2f - Math.PI / 2.0f) + 1;
		} else if (easeOut) {
			return (float) Math.sin(Math.PI / 2 * t);
		} else {
			throw new IllegalArgumentException("Invalid easing values. Cannot be both false.");
		}
	}

	/**
	 * Calculate interpolation between 0.0 and 1.0 using sine function.
	 * 
	 * @param t
	 *            the x-axis position in the graph, ranged from {@code 0.0f} and {@code 1.0f}, inclusive.
	 * @return Value between {@code 0.0f} and {@code 1.0f}, inclusive.
	 */
	public static float sineInterpolate(float t) {
		return sineInterpolate(t, true, true);
	}

	/**
	 * Calculate interpolation between start point and end point using sine function.
	 * 
	 * @param a
	 *            the value of start point
	 * @param b
	 *            the value of end point
	 * @param t
	 *            the x-axis position in the graph, ranged from {@code 0.0f} and {@code 1.0f}, inclusive.
	 * @param easeIn
	 *            control whether or not the starting point slope is zero.
	 * @param easeOut
	 *            control whether or not the end point slope is zero.
	 * @return Value between {@code a} and {@code b}, inclusive.
	 * @throws IllegalArgumentException
	 *             if both {@code easeIn} and {@code easeOut} is both false. In this case, use {@link interpolate}
	 *             instead.
	 */
	public static float sineInterpolate(float a, float b, float t, boolean easeIn, boolean easeOut) {
		return interpolate(a, b, sineInterpolate(t, easeIn, easeOut));
	}

	/**
	 * Calculate interpolation between start point and end point using sine function.
	 * 
	 * @param a
	 *            the value of start point
	 * @param b
	 *            the value of end point
	 * @param t
	 *            the x-axis position in the graph, ranged from {@code 0.0f} and {@code 1.0f}, inclusive.
	 * @return Value between {@code a} and {@code b}, inclusive.
	 */
	public static float sineInterpolate(float a, float b, float t) {
		return interpolate(a, b, sineInterpolate(t, true, true));
	}

	/**
	 * Calculate interpolation between start point and end point using linear function.
	 * 
	 * @param a
	 *            the value of start point
	 * @param b
	 *            the value of end point
	 * @param t
	 *            the x-axis position in the graph, ranged from {@code 0.0f} and {@code 1.0f}, inclusive.
	 * @return Value between {@code a} and {@code b}, inclusive.
	 */
	public static float interpolate(float a, float b, float t) {
		return a * (1 - t) + b * t;
	}

	/**
	 * Calculate interpolation between start vector and end vector using linear function.
	 * 
	 * @param a
	 *            the value of start point
	 * @param b
	 *            the value of end point
	 * @param t
	 *            the x-axis position in the graph, ranged from {@code 0.0f} and {@code 1.0f}, inclusive.
	 * @return Value between {@code a} and {@code b}, inclusive.
	 * @throws IllegalArgumentException
	 *             if {@code t} is out of range.
	 */
	public static Vector2 interpolate(Vector2 a, Vector2 b, float t) {
		return new Vector2(interpolate(a.getX(), b.getX(), t), interpolate(a.getY(), b.getY(), t));
	}

	/**
	 * Calculate interpolation between start vector and end vector using linear function.
	 * 
	 * @param a
	 *            the value of start point
	 * @param b
	 *            the value of end point
	 * @param t
	 *            the x-axis position in the graph, ranged from {@code 0.0f} and {@code 1.0f}, inclusive.
	 * @return Value between {@code a} and {@code b}, inclusive.
	 * @throws IllegalArgumentException
	 *             if {@code t} is out of range.
	 */
	public static Vector3 interpolate(Vector3 a, Vector3 b, float t) {
		return new Vector3(interpolate(a.getX(), b.getX(), t), interpolate(a.getY(), b.getY(), t),
				interpolate(a.getZ(), b.getZ(), t));
	}

	/**
	 * Constrain a value to an interval [low, high]. In other words, it return 1) the value itself if its value is
	 * between low and high 2) the low value if the specified value if less than the low value 3) the high value if the
	 * specified value if greater than the high value.
	 * 
	 * @param low
	 *            low value
	 * @param high
	 *            high value
	 * @param value
	 *            specified value
	 * @return A clamped value.
	 */
	public static float clamp(float low, float high, float value) {
		if (low > high) {
			throw new IllegalArgumentException("Low value cannot be higher than high value : " + low + " > " + high);
		}
		return Math.max(low, Math.min(high, value));

	}

}
