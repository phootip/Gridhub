package objectInterface;

public interface PushableObject extends IDrawable{
	public boolean isPushable();
	public boolean push(int previousWeight, int nextX, int nextY, int nextZ);
}
