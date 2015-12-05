package util;

import java.awt.Color;

public class Helper {

	public static Color getAlphaColor(Color baseColor, int alpha) {
		return new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha);
	}

	public static Color getAlphaColorPercentage(Color baseColor, double alphaRatio) {
		return new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), Math.round(alphaRatio * 255));
	}

	public static Color blendColor(Color a, Color b, float blendRatio) {
		if (blendRatio == 0) {
			return a;
		} else if (blendRatio == 1) {
			return b;
		} else if (blendRatio < 0 || blendRatio > 1) {
			throw new IllegalArgumentException("Invalid blend ratio: " + blendRatio);
		} else {
			return new Color((int) (a.getRed() * (1 - blendRatio) + b.getRed() * blendRatio),
					(int) (a.getGreen() * (1 - blendRatio) + b.getGreen() * blendRatio),
					(int) (a.getBlue() * (1 - blendRatio) + b.getBlue() * blendRatio),
					(int) (a.getAlpha() * (1 - blendRatio) + b.getAlpha() * blendRatio));
		}
	}

}
