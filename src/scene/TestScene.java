package scene;

import java.awt.Color;
import java.awt.Graphics2D;

import scene.core.Scene;
import util.Constants.*;

public class TestScene extends Scene {

	float positionX;
	int elapsedTime = 0;
	int gridDensity = 10;
	int oscillationPeriod = 20000;
	int oscillationAmplitude = 50;

	public TestScene() {
		positionX = 0;
	}

	@Override
	public void update(final int step) {
		positionX += step / 100f;
		if (positionX > gridDensity)
			positionX = 0;

		elapsedTime = (elapsedTime + step) % oscillationPeriod;
	}

	@Override
	public void draw(final Graphics2D g, final int sceneWidth, final int sceneHeight) {
		int i;

		g.setColor(ColorSwatch.BACKGROUND);
		g.fillRect(0, 0, sceneWidth, sceneHeight);

		g.setColor(ColorSwatch.SHADOW);

		// Horizontal lines
		for (i = 0; i < sceneHeight; i++) {
			g.drawLine((int) positionX + i * gridDensity, 0, (int) positionX + i * gridDensity, sceneHeight);
		}

		// Vertical lines
		int shifter = (int) Math.round(Math.sin(Math.PI * 2 * elapsedTime / oscillationPeriod) * oscillationAmplitude);
		int currentY = shifter;
		while (currentY < sceneHeight) {
			g.drawLine(0, currentY, sceneWidth, currentY);
			currentY += oscillationAmplitude;
		}

		int rectCount = (int) Math.round(Math.sin(Math.PI * 2 * elapsedTime / oscillationPeriod) * 1000);
		for (i = 0; i < rectCount; i++) {
			g.setColor(new Color(0xFF, 0xFF, 0xFF, 0x20));
			g.fillOval((int) Math.floor(Math.random() * sceneWidth), (int) Math.floor(Math.random() * sceneHeight), 10,
					10);
		}
	}
}
