package scene.test;

public class TeleportToArea extends TeleportGate {
	private int destinationX , destinationY, destinationZ;
	
	public TeleportToArea(int x, int y, int z , int destinationX , int destinationY , int destinationZ) {
		super(x, y, z);
		// TODO Auto-generated constructor stub
		this.destinationX = destinationX;
		this.destinationY = destinationY;
		this.destinationZ = destinationZ;
		this.isActive = true;
	}

	@Override
	public void teleport(Player p) {
		// TODO Auto-generated method stub
		p.setPlayerPosition(destinationX , destinationY , destinationZ);
	}


}
