package objectInterface;

import java.awt.Graphics2D;

import scene.test.Camera;

public interface IDrawable {

	int getCellX();

	int getCellY();

	int getCellZ();

	void draw(Graphics2D g, Camera camera);

}
