package stage.gameobj;

import java.awt.Graphics2D;

import core.geom.Vector3;
import stage.Camera;

/**
 * 
 * This interface represent every object which will be drawn on the screen
 * @author Thanat Jatuphatthachat		
 *
 */
public interface IDrawable {
	/**
	 * 
	 * @return Vector3D which is used as component when calculate the draw position of the objects
	 */
	Vector3 getDrawPosition();
	/**
	 * This method is called by any class that neeed to draw the object implement this class
	 * @param g
	 * @param camera
	 */
	void draw(Graphics2D g, Camera camera);

}
