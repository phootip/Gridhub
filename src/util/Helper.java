package util;

import java.awt.Color;

public class Helper {

	public static Color getAlphaColor(Color baseColor, int alpha) {
		return new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha);
	}

	public static Color getAlphaColorPercentage(Color baseColor, double alphaRatio) {
		return new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), Math.round(alphaRatio * 255));
	}

}
