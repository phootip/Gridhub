package scene;

import java.awt.Graphics2D;

import scene.core.Scene;
import util.Resource;
import util.Constants.ColorSwatch;

public class TestScene2 extends Scene {

	@Override
	public void update(int step) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics2D g, int sceneWidth, int sceneHeight) {
		// Draw Background
		g.setColor(ColorSwatch.BACKGROUND);
		g.fillRect(0, 0, sceneWidth, sceneHeight);
		
		g.setColor(ColorSwatch.FOREGROUND);
		g.setFont(Resource.getInstance().getDefaultFont(50));
		g.drawString(this.getClass().getName(), 20, 20 + g.getFontMetrics().getAscent());
	}
	
}
