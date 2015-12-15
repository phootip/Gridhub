package scene.level;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

import core.IScrollableListItem;
import core.geom.Vector2;
import jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm.SLOW_IC;
import stage.gameobj.Block;
import stage.gameobj.FloorSwitch;
import stage.gameobj.Gate;
import stage.gameobj.GateToGateTeleport;
import stage.gameobj.IControlable;
import stage.gameobj.Slope;
import stage.gameobj.SwitchController;
import stage.gameobj.TeleportDestionation;
import stage.gameobj.TeleportGate;
import stage.gameobj.TeleportToArea;
import util.Constants.ColorSwatch;
import util.Helper;
import util.Resource;
import util.Resource.FontWeight;

public final class LevelData implements IScrollableListItem {

	/**
	 * Get the thumbnail image of this level data.
	 * 
	 * @return Thumbnail image object, or {@code null} if the thumbnail has not been created.
	 */
	protected BufferedImage getThumbnail() {
		return thumbnail;
	}

	/**
	 * Set the thumbnail image of this level data. This should be called by {@link LevelThumbnailRenderer}.
	 * 
	 * @param thumbnail
	 *            a {@link BufferedImage} object of thumbnail image.
	 */
	protected void setThumbnail(BufferedImage thumbnail) {
		this.thumbnail = thumbnail;
	}

	private transient BufferedImage thumbnail = null;
	private transient Chapter chapter;

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	private static final int CONTENT_MARGIN_LEFT = 50;
	private static final int IMAGE_WIDTH = 180;
	private static final int IMAGE_HEIGHT = 90;
	private static final int TEXT_MARGIN = 50;
	private int playerCount;
	private String mapName;

	public int getPlayerCount() {
		return playerCount;
	}

	public String getMapName() {
		return mapName;
	}

	private LevelData(int playerCount, String mapName) {
		this.playerCount = playerCount;
		this.mapName = mapName;
	}

	public static LevelData parse(String jsonContent) {
		return new LevelData(1, jsonContent);
	}

	// public static void createBlank(PlayerMode playerMode ,int sizeX , int sizeY) {
	//
	// }

	@Override
	public int getListItemHeight() {
		return 100;
	}

	@Override
	public void drawListItemContent(Graphics2D g, int x, int y, int width, boolean isSelected) {

		int imgLeft = x + CONTENT_MARGIN_LEFT;
		int imgTop = y + (getListItemHeight() - IMAGE_HEIGHT) / 2;

		if (this.getThumbnail() != null) {
			AffineTransform thumbnailTransform = new AffineTransform();
			thumbnailTransform.scale((double) IMAGE_WIDTH / thumbnail.getWidth(),
					(double) IMAGE_HEIGHT / thumbnail.getHeight());
			g.drawImage(thumbnail, new AffineTransformOp(thumbnailTransform, AffineTransformOp.TYPE_BICUBIC), imgLeft,
					imgTop);
		} else {
			g.setColor(ColorSwatch.SHADOW);
			g.setStroke(Resource.getGameObjectThinStroke());
			g.drawRect(imgLeft, imgTop, IMAGE_WIDTH, IMAGE_HEIGHT);

			g.setColor(ColorSwatch.SHADOW);
			String loadingText = "Generating Preview...";
			Font loadingTextFont = Resource.getInstance().getDefaultFont(20, FontWeight.BOLD);
			Vector2 drawPosition = Helper.getCenteredTextPosition(loadingText, loadingTextFont, g, imgLeft, imgTop,
					IMAGE_WIDTH, IMAGE_HEIGHT);

			g.setFont(loadingTextFont);
			g.drawString(loadingText, drawPosition.getX(), drawPosition.getY());
		}

		Font levelNameFont = Resource.getInstance().getDefaultFont(50);
		Vector2 levelNameTextPos = Helper.getCenteredTextPosition(mapName, levelNameFont, g, x, y, width,
				getListItemHeight());
		g.setFont(levelNameFont);
		g.setColor(isSelected ? ColorSwatch.BACKGROUND : ColorSwatch.FOREGROUND);
		g.drawString(mapName, x + IMAGE_WIDTH + CONTENT_MARGIN_LEFT + TEXT_MARGIN, levelNameTextPos.getY());

	}
	
	private LevelDataBuilder levelDataBuilder;
	private String levelDataJSON;
	
	public static LevelData createLevelDataAsJSON(LevelDataBuilder builder) {
		return new LevelData(builder);
	}
	
	private LevelData (LevelDataBuilder builder) {
		this.levelDataBuilder = builder;
		levelDataJSON = createLevelData(builder.getBlocks(), builder.getSlopes(), builder.getFloorSwitches(),
				builder.getGateTogateTeles(), builder.getTeleportToArea(), builder.getTelportDests(),
				builder.getGates(), builder.getSwControllers(), builder.getFloorLevel(), builder.getLevelName(),
				builder.getFinishX(), builder.getFinishY() ,builder.getStartX() , builder.getStartY());
	}
	
	private String createLevelData(ArrayList<Block> blocks, ArrayList<Slope> slopes,
			ArrayList<FloorSwitch> floorSwitches, ArrayList<GateToGateTeleport> gateTogateTeles,
			ArrayList<TeleportToArea> teleportToArea, ArrayList<TeleportDestionation> telportDests,
			ArrayList<Gate> gates, ArrayList<SwitchController> swControllers, int[][] floorLevel, String LevelName,
			ArrayList<Integer> finishX, ArrayList<Integer> finishY , int[] startX , int[] startY) {

		String blockJSON = getBlockArrayAsJSON(blocks);
		String slopeJSON = getSlopeArrayAsJSON(slopes);
		String fsJSON = getFloorSwitchesArrayAsJSON(floorSwitches);
		String gateToGateJSON = getGateToGateTeleportAsJSON(gateTogateTeles);
		String gateLinkJSON = getPortalLinkIndexAsJSON(gateTogateTeles);
		String teleToAreaJSON = getTeleportToAreaAsJSON(teleportToArea);
		String teleportDestination = getTeleportDestinationAsJSON(telportDests);
		String gateJSON = getGateArrayAsJSON(gates);
		String swControllerJSON = getControllerArrayAsJSON(swControllers);
		String controllerSwitchSetIndexJSON = getControllSwitchesSetIndexList(swControllers, floorSwitches);
		String controllerObjectIndexJSON = getControlObjectSetIndexList(swControllers, gates, gateTogateTeles, teleportToArea);
		String floorLevelJSON = getFloorLevelAsString(floorLevel);
		String finishXJSON = getFinishAreaAsJSON(finishX);
		String finishYJSON = getFinishAreaAsJSON(finishY);
		String startXJSON = getStartPositionASJSON(startX);
		String startYJSON =  getStartPositionASJSON(startY);
		
		HashMap<String, String> levelDataMap = new HashMap<>();
		levelDataMap.put("Block", blockJSON);
		levelDataMap.put("Slope", slopeJSON);
		levelDataMap.put("FloorSwitch", fsJSON);
		levelDataMap.put("GateToGateTeleport", gateToGateJSON);
		levelDataMap.put("GateLink",gateLinkJSON);
		levelDataMap.put("TeleportToArea", teleToAreaJSON);
		levelDataMap.put("TeleportDestination", teleportDestination);
		levelDataMap.put("Gate", gateJSON);
		levelDataMap.put("SwitchController", swControllerJSON);
		levelDataMap.put("ControllerSwitchSet", controllerSwitchSetIndexJSON);
		levelDataMap.put("ControlObject", controllerObjectIndexJSON);
		levelDataMap.put("FloorLevel", floorLevelJSON);
		levelDataMap.put("FinishX",finishXJSON);
		levelDataMap.put("FinishY", finishYJSON);
		levelDataMap.put("StartX", startXJSON);
		levelDataMap.put("StartY", startYJSON);
		
		Gson gson = new Gson();
		return gson.toJson(levelDataMap);

	}
	
	

	public String getLevelDataJSON() {
		return levelDataJSON;
	}

	public String getBlockArrayAsJSON(ArrayList<Block> a) {
		Gson gson = new Gson();
		return gson.toJson(a);

	}

	public String getSlopeArrayAsJSON(ArrayList<Slope> s) {
		Gson gson = new Gson();
		return gson.toJson(s);

	}

	public String getFloorSwitchesArrayAsJSON(ArrayList<FloorSwitch> fs) {
		Gson gson = new Gson();
		return gson.toJson(fs);

	}

	public String getGateToGateTeleportAsJSON(ArrayList<GateToGateTeleport> gateTele) {
		Gson gson = new Gson();
		return gson.toJson(gateTele);

	}

	public String getPortalLinkIndexAsJSON(ArrayList<GateToGateTeleport> gateTeleport) {
		Gson gson = new Gson();
		int[] index = new int[gateTeleport.size()];
		ArrayList<TeleportGate> linkGates = new ArrayList<>();
		for (int i = 0; i < gateTeleport.size(); i++) {
			linkGates.add(gateTeleport.get(i).getDestinationGate());
		}
		for (int i = 0; i < linkGates.size(); i++) {
			for (int j = 0; j < gateTeleport.size(); j++) {
				if (linkGates.get(i) == gateTeleport.get(j)) {
					index[i] = j;
				}
			}
		}
		String json = gson.toJson(index);
		return json;
	}

	public String getTeleportToAreaAsJSON(ArrayList<TeleportToArea> gateTele) {
		Gson gson = new Gson();
		return gson.toJson(gateTele);
	}

	public String getTeleportDestinationAsJSON(ArrayList<TeleportDestionation> destinations) {
		Gson gson = new Gson();
		return gson.toJson(destinations);
	}

	public String getGateArrayAsJSON(ArrayList<Gate> gates) {
		Gson gson = new Gson();
		return gson.toJson(gates);
	}

	public String getControllerArrayAsJSON(ArrayList<SwitchController> swControllers) {
		Gson gson = new Gson();
		return gson.toJson(swControllers);
	}

	public String getControllSwitchesSetIndexList(ArrayList<SwitchController> swControllers,
			ArrayList<FloorSwitch> fs) {
		Gson gson = new Gson();
		ArrayList<int[]> SwitchIndexList = new ArrayList<>();
		int[] switchSetIndex;

		for (int i = 0; i < swControllers.size(); i++) {
			ArrayList<FloorSwitch> floorSwitchInController = swControllers.get(i).getFloorSwitchesControllerSet();
			switchSetIndex = new int[floorSwitchInController.size()];
			for (int j = 0; j < switchSetIndex.length; j++) {
				switchSetIndex[i] = -1;
			}
			// if no switch is match then switchSetIndex[i] = -1
			for (int j = 0; j < floorSwitchInController.size(); j++) {
				for (int k = 0; k < fs.size(); k++) {
					if (floorSwitchInController.get(j) == fs.get(k)) {
						switchSetIndex[j] = k;
					}
				}
			}
			SwitchIndexList.add(switchSetIndex);
		}
		return gson.toJson(SwitchIndexList);
	}

	public String getControlObjectSetIndexList(ArrayList<SwitchController> swController, ArrayList<Gate> gates,
			ArrayList<GateToGateTeleport> gtogTele, ArrayList<TeleportToArea> toAreaTele) {
		Gson gson = new Gson();
		ArrayList<int[]> indexList = new ArrayList<>();

		for (int i = 0; i < swController.size(); i++) {
			IControlable controlObject = swController.get(i).getControlObject();
			int[] index = new int[3];
			for (int j = 0; j < index.length; j++) {
				index[j] = -1;
			}

			if (controlObject instanceof Gate) {
				for (int j = 0; j < gates.size(); j++) {
					if (controlObject == gates.get(j)) {
						index[0] = j;
					}
				}
			} else if (controlObject instanceof GateToGateTeleport) {
				for (int j = 0; j < gtogTele.size(); j++) {
					if (controlObject == gtogTele.get(j)) {
						index[1] = j;
					}
				}
			} else if (controlObject instanceof TeleportToArea) {
				for (int j = 0; j < toAreaTele.size(); j++) {
					if (controlObject == toAreaTele.get(j)) {
						index[2] = j;
					}
				}
			}
			indexList.add(index);
		}
		return gson.toJson(indexList);
	}

	public String getFloorLevelAsString(int[][] floorLevel) {
		Gson gson = new Gson();
		return gson.toJson(floorLevel);
	}

	public String getFinishAreaAsJSON(ArrayList<Integer> finish) {
		Gson gson = new Gson();
		return gson.toJson(finish);
	}

	public String getLevelNameAsJSON(String levelName) {
		Gson gson = new Gson();
		return gson.toJson(levelName);
	}

	public String getLevelNameAsJSON(int playerCount) {
		Gson gson = new Gson();
		return gson.toJson(playerCount);
	}

	public String getStartPositionASJSON(int[] start) {
		Gson gson = new Gson();
		return gson.toJson(start);
	}

}
