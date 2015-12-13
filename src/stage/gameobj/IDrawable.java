package stage.gameobj;

import java.awt.Graphics2D;

import core.geom.Vector3;
import stage.Camera;

public interface IDrawable {
	
	Vector3 getDrawPosition();

	void draw(Graphics2D g, Camera camera);

}
