package gamestage.gameobject;

import java.awt.Graphics2D;

import stage.Camera;

public interface IDrawable {

	float getDrawX();

	float getDrawY();

	float getDrawZ();

	void draw(Graphics2D g, Camera camera);

}
