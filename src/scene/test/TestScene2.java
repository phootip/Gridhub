package scene.test;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import scene.core.Scene;
import util.Resource;
import util.Constants.ColorSwatch;

public class TestScene2 extends Scene {

	private Camera camera;
	private Player player1;

	public TestScene2() {
		player1 = new Player();
		camera = new Camera(player1);
	}

	@Override
	public void update(int step) {

		player1.update(step);
		camera.update(step);

	}

	@Override
	public void draw(Graphics2D g, int sceneWidth, int sceneHeight) {
		int i;

		camera.setSceneSize(sceneWidth, sceneHeight);

		// Draw background
		g.setColor(ColorSwatch.BACKGROUND);
		g.fillRect(0, 0, sceneWidth, sceneHeight);

		// Draw class name
		g.setColor(ColorSwatch.SHADOW);
		g.setFont(Resource.getInstance().getDefaultFont(50));
		g.drawString(this.getClass().getName(), 20, 20 + g.getFontMetrics().getAscent());

		// Draw grid line

		float gridSpacingX = camera.getDrawSizeX(1);
		float gridSpacingY = camera.getDrawSizeY(1);
		float startX = camera.getXPosition(player1.getCellX()) + gridSpacingX / 2;
		float startY = camera.getYPosition(player1.getCellX(), 0) + gridSpacingY / 2;
		float temp;

		g.setStroke(new BasicStroke(1));
		g.setColor(ColorSwatch.SHADOW);
		for (temp = startX; temp >= 0; temp -= gridSpacingX) {
			g.draw(new Line2D.Float(temp, 0, temp, sceneHeight));
		}
		for (temp = startX + gridSpacingX; temp <= sceneWidth; temp += gridSpacingX) {
			g.draw(new Line2D.Float(temp, 0, temp, sceneHeight));
		}
		for (temp = startY; temp >= 0; temp -= gridSpacingY) {
			g.draw(new Line2D.Float(0, temp, sceneWidth, temp));
		}
		for (temp = startY + gridSpacingY; temp <= sceneHeight; temp += gridSpacingY) {
			g.draw(new Line2D.Float(0, temp, sceneWidth, temp));
		}

		player1.draw(g, camera);
	}

}
