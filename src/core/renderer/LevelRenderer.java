package core.renderer;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import core.geom.Vector2;
import stage.Camera;
import stage.gameobj.IDrawable;

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
//				if (o1.getDrawZ() != o2.getDrawZ()) {
//					return (o1.getDrawZ() < o2.getDrawZ()) ? -1 : 1;
//				} else {
					float depthA = depthChecker.getX() * o1.getDrawX() + depthChecker.getY() * o1.getDrawY() + o1.getDrawZ();
					float depthB = depthChecker.getX() * o2.getDrawX() + depthChecker.getY() * o2.getDrawY() + o2.getDrawZ();

					return (int) Math.signum(depthA - depthB);
//				}
			}
		});

		for (IDrawable obj : drawList) {
			obj.draw(g, camera);
		}
	}

}
