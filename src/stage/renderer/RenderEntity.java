package stage.renderer;

import core.geom.Vector3;
import stage.Camera;
import stage.gameobj.IDrawable;

class RenderEntity implements Comparable<RenderEntity> {
	private Vector3 position;
	private IDrawable drawableObject;
	private float drawYPosition;

	protected RenderEntity(Vector3 position, IDrawable drawableObject, Camera camera) {
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