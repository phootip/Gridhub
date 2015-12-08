package core.renderer;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import core.geom.Vector2;
import objectInterface.IDrawable;
import scene.test.Camera;

public class LevelRenderer {

	public static void draw(Collection<IDrawable> objectCollection, Graphics2D g, Camera camera) {
		ArrayList<IDrawable> drawList = new ArrayList<IDrawable>();

		for (IDrawable obj : objectCollection) {
			drawList.add(obj);
		}
		
		final Vector2 depthChecker = new Vector2(0, 1).rotate(-camera.getRotationAngle()); 

		drawList.sort(new Comparator<IDrawable>() {

			@Override
			public int compare(IDrawable o1, IDrawable o2) {
				if (o1.getCellZ() != o2.getCellZ()) {
					return (o1.getCellZ() < o2.getCellZ()) ? -1 : 1;
				} else {
					float depthA = depthChecker.getX() * o1.getCellX() + depthChecker.getY() * o1.getCellY();
					float depthB = depthChecker.getX() * o2.getCellX() + depthChecker.getY() * o2.getCellY();
					
					if (depthA < depthB) return -1;
					return 1;
				}
			}
		});

		for (IDrawable obj : drawList) {
			obj.draw(g, camera);
		}
	}

}
