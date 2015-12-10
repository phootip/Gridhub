package scene.test;

public class GateToGateTeleport extends TeleportGate {
	
	private TeleportGate destinationGate;
	public GateToGateTeleport(int x, int y, int z) {
		super(x, y, z);
		// TODO Auto-generated constructor stub
		this.isActive = true;
	}
	
	public GateToGateTeleport(int x, int y, int z , TeleportGate destinationGate) {
		super(x, y, z);
		// TODO Auto-generated constructor stub
		this.isActive = true;
		this.destinationGate = destinationGate;
	}

	@Override
	protected void teleport(Player p) {
		// TODO Auto-generated method stub
		p.setPlayerPosition(destinationGate.getCellX(), destinationGate.getCellY(),	 destinationGate.getCellZ());
		destinationGate.setActive(false);
	}
	
	public void setDestinationTelelportGate(TeleportGate destination) {
		this.destinationGate = destination;
	}
	
}
