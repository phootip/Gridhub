package stage.renderer;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import core.geom.Vector3;
import stage.Camera;
import stage.gameobj.IDrawable;
import stage.gameobj.ILargeDrawable;

public class LevelRenderer {

	static class RenderEntity implements Comparable<RenderEntity> {
		private Vector3 position;
		private IDrawable drawableObject;
		private float drawYPosition;

		public RenderEntity(Vector3 position, IDrawable drawableObject, Camera camera) {
			this.position = position;
			this.drawableObject = drawableObject;
			this.drawYPosition = camera.getYPosition(-position.getX(), -position.getY(), position.getZ());
		}

		/**
		 * @return the position
		 */
		protected Vector3 getPosition() {
			return position;
		}

		/**
		 * @return the drawableObject
		 */
		protected IDrawable getDrawableObject() {
			return drawableObject;
		}

		@Override
		public int compareTo(RenderEntity o) {
			return (int) Math.signum(o.drawYPosition - this.drawYPosition);
		}
	}

	private static <T> ArrayList<T> createListWithUniqueItem(Collection<T> objCollection) {
		// Note: this would create new collection, not edit the old one.
		Set<T> checker = new HashSet<>();
		ArrayList<T> newList = new ArrayList<>();

		Iterator<T> collectionIt = objCollection.iterator();
		while (collectionIt.hasNext()) {
			T obj = collectionIt.next();
			if (!checker.contains(obj)) {
				checker.add(obj);
				newList.add(obj);
			}
		}

		return newList;
	}

	public static void draw(Collection<IDrawable> objectCollection, Graphics2D g, Camera camera) {

		List<IDrawable> drawList = createListWithUniqueItem(objectCollection);
		List<RenderEntity> renderEntities = createRenderEntityList(drawList, camera);

		Collections.sort(renderEntities);

		// final Vector2 depthChecker = new Vector2(0, 1).rotate(camera.getRotationAngle()).multiply(1, 0.5f);

		/*
		 * drawList.sort(new Comparator<IDrawable>() {
		 * 
		 * @Override public int compare(IDrawable o1, IDrawable o2) { // if (o1.getDrawZ() != o2.getDrawZ()) { // return
		 * (o1.getDrawZ() < o2.getDrawZ()) ? -1 : 1; // } else {
		 * 
		 * // float depthA = depthChecker.getX() * o1.getDrawX() + depthChecker.getY() * o1.getDrawY() + //
		 * o1.getDrawZ(); float depthB = depthChecker.getX() * o2.getDrawX() + depthChecker.getY() * // o2.getDrawY() +
		 * o2.getDrawZ();
		 * 
		 * float depthA = camera.getYPosition(-o1.getDrawX(), -o1.getDrawY(), o1.getDrawZ()); float depthB =
		 * camera.getYPosition(-o2.getDrawX(), -o2.getDrawY(), o2.getDrawZ());
		 * 
		 * return (int) Math.signum(depthB - depthA); // } } });
		 */

		for (RenderEntity renderEntity : renderEntities) {
			IDrawable drawableObj = renderEntity.getDrawableObject();
			if (drawableObj instanceof ILargeDrawable) {
				((ILargeDrawable) drawableObj).draw(g, camera, renderEntity.getPosition());
			} else {
				drawableObj.draw(g, camera);
			}
		}
	}

	private static List<RenderEntity> createRenderEntityList(List<IDrawable> drawList, Camera camera) {
		List<RenderEntity> entitiesList = new ArrayList<>();

		for (IDrawable obj : drawList) {
			if (obj instanceof ILargeDrawable) {
				Collection<Vector3> drawPositionList = ((ILargeDrawable) obj).getDrawPositionList();
				for (Vector3 position : drawPositionList) {
					entitiesList.add(new RenderEntity(position, obj, camera));
				}
			} else {
				entitiesList.add(new RenderEntity(obj.getDrawPosition(), obj, camera));
			}
		}

		return entitiesList;
	}

}
