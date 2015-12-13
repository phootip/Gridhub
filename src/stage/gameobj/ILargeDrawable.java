package stage.gameobj;

import java.awt.Graphics2D;
import java.util.Collection;

import core.geom.Vector3;
import stage.Camera;

public interface ILargeDrawable extends IDrawable {

	Collection<Vector3> getDrawPositionList();
	
	void draw(Graphics2D g, Camera camera, Vector3 position);

}
