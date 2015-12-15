package stage.gameobj;


/**
 * @author Thanat
 *
 */
public class GateToGateTeleport extends TeleportGate {
	
	private TeleportGate destinationGate;
	public GateToGateTeleport(int x, int y, int z) {
		super(x, y, z);
		// TODO Auto-generated constructor stub
		this.destinationGate = null;
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
		try {
			destinationGate.setActive(false);
			p.setPlayerPosition(destinationGate.getX(), destinationGate.getY(),	 destinationGate.getZ());
			
			
		} catch (NullPointerException e) {
			// TODO: handle exception
			System.out.println("No Destination TeleportGate");
			p.setPlayerPosition(0, 0, 0);
		}
		
	}
	
	public void setDestinationTelelportGate(TeleportGate destination) {
		this.destinationGate = destination;
	}

	public TeleportGate getDestinationGate() {
		return destinationGate;
	}

	public void setDestinationGate(TeleportGate destinationGate) {
		this.destinationGate = destinationGate;
	}
	
	
	
	

}
