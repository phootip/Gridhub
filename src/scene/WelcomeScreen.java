package scene;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import core.SceneManager;
import scene.core.Scene;
import util.Resource;
import util.Constants;
import util.Constants.ColorSwatch;
import util.Helper;

public class WelcomeScreen extends Scene {

	long cumulativeStep = 0;
	Font centerTextFont;

	public WelcomeScreen() {
		this.centerTextFont = Resource.getInstance().getDefaultFont(200);
	}

	@Override
	public void update(int step) {
		cumulativeStep += step;

		if (cumulativeStep > 100 * 60 * 4) {
			SceneManager.getInstance().setNextScene(new TestScene2());
		}
	}

	@Override
	public void draw(Graphics2D g, int sceneWidth, int sceneHeight) {
		// Draw background
		g.setColor(ColorSwatch.BACKGROUND);
		g.fillRect(0, 0, sceneWidth, sceneHeight);

		// Draw text
		FontMetrics centerTextFontMetric = g.getFontMetrics(this.centerTextFont);
		Rectangle2D centerTextBound = centerTextFontMetric.getStringBounds(Constants.PROGRAM_NAME, g);

		g.setFont(this.centerTextFont);
		float centerTextX = (float) (sceneWidth - centerTextBound.getWidth()) / 2.0f;
		float centerTextY = (float) ((sceneHeight - centerTextBound.getHeight()) / 2.0f
				+ centerTextFontMetric.getAscent());

		float blendRatio = 0;
		if (cumulativeStep < 100 * 30) {
			blendRatio = cumulativeStep / (float) (100 * 30);
		} else if (cumulativeStep < 100 * 150) {
			blendRatio = 1;
		} else if (cumulativeStep < 100 * 180) {
			blendRatio = 1 - (cumulativeStep - 100 * 150) / (float) (100 * 30);
		}

		g.setColor(Helper.blendColor(ColorSwatch.BACKGROUND, ColorSwatch.SHADOW, blendRatio));
		g.drawString(Constants.PROGRAM_NAME, centerTextX + 5, centerTextY + 5);

		g.setColor(Helper.blendColor(ColorSwatch.BACKGROUND, ColorSwatch.FOREGROUND, blendRatio));
		g.drawString(Constants.PROGRAM_NAME, centerTextX, centerTextY);

		g.setColor(ColorSwatch.FOREGROUND);
		g.setStroke(new BasicStroke(5));

		// Draw line
		if (cumulativeStep > 100 * 30 && cumulativeStep < 100 * 75) {
			float topLineStartRatio = 1;
			if (cumulativeStep < 100 * 60) {
				topLineStartRatio = (float) Math.cos(Math.PI * (cumulativeStep - 100 * 30) / 100 / 30) / -2 + 0.5f;
				topLineStartRatio *= topLineStartRatio; // For more power
			}
			float lineEndRatio = 0;
			if (cumulativeStep > 100 * 45) {
				lineEndRatio = (float) Math.cos(Math.PI * (cumulativeStep - 100 * 45) / 100 / 30) / -2 + 0.5f;
				lineEndRatio *= lineEndRatio; // For more power
			}

			int topLineY = (int) ((sceneHeight - centerTextBound.getHeight()) / 2.0f);
			int topLineStartX = (int) (sceneWidth - centerTextBound.getWidth()) / 2;
			g.drawLine(topLineStartX + (int) (centerTextBound.getWidth() * topLineStartRatio), topLineY,
					topLineStartX + (int) (centerTextBound.getWidth() * lineEndRatio), topLineY);
		}
		int bottomDelay = 10;
		if (cumulativeStep > 100 * (30 + bottomDelay) && cumulativeStep < 100 * (75 + bottomDelay)) {
			float topLineStartRatio = 1;
			if (cumulativeStep < 100 * (60 + bottomDelay)) {
				topLineStartRatio = (float) Math.cos(Math.PI * (cumulativeStep - 100 * (30 + bottomDelay)) / 100 / 30)
						/ -2 + 0.5f;
				topLineStartRatio *= topLineStartRatio; // For more power
			}
			float lineEndRatio = 0;
			if (cumulativeStep > 100 * (45 + bottomDelay)) {
				lineEndRatio = (float) Math.cos(Math.PI * (cumulativeStep - 100 * (45 + bottomDelay)) / 100 / 30) / -2
						+ 0.5f;
				lineEndRatio *= lineEndRatio; // For more power
			}

			int bottomLineY = (int) ((sceneHeight + centerTextBound.getHeight()) / 2.0f);
			int bottomLineStartX = (int) (sceneWidth + centerTextBound.getWidth()) / 2;
			g.drawLine(bottomLineStartX - (int) (centerTextBound.getWidth() * topLineStartRatio), bottomLineY,
					bottomLineStartX - (int) (centerTextBound.getWidth() * lineEndRatio), bottomLineY);
		}
	}

}
