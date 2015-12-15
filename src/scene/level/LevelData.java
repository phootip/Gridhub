package scene.level;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import core.IScrollableListItem;
import core.geom.Vector2;
import stage.GameStage;
import stage.gameobj.Block;
import stage.gameobj.FloorSwitch;
import stage.gameobj.Gate;
import stage.gameobj.GateController;
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

// TODO: Complete Javadoc

/**
 * A class representing a level data. Used for representing a single level in the game. This can be passed to
 * {@link GameStage} for constructing game stage. Also, {@link LevelData} can be used in serializing or deserializing
 * JSON string for level loading from file and level saving to file.
 * 
 * @author Kasidit Iamthong, Thanat
 * @see LevelDataBuilder
 */
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
	public static final int THUMBNAIL_IMAGE_WIDTH = 180;
	public static final int THUMBNAIL_IMAGE_HEIGHT = 90;
	private static final int TEXT_MARGIN = 50;
	private int playerCount;
	private String mapName;

	public int getPlayerCount() {
		return playerCount;
	}

	public String getMapName() {
		return mapName;
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
		int imgTop = y + (getListItemHeight() - THUMBNAIL_IMAGE_HEIGHT) / 2;

		if (this.getThumbnail() != null) {
			AffineTransform thumbnailTransform = new AffineTransform();
			thumbnailTransform.scale((double) THUMBNAIL_IMAGE_WIDTH / thumbnail.getWidth(),
					(double) THUMBNAIL_IMAGE_HEIGHT / thumbnail.getHeight());
			g.drawImage(thumbnail, new AffineTransformOp(thumbnailTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR),
					imgLeft, imgTop);
		} else {
			g.setColor(ColorSwatch.SHADOW);
			String loadingText = "Generating Preview...";
			Font loadingTextFont = Resource.getInstance().getDefaultFont(20, FontWeight.BOLD);
			Vector2 drawPosition = Helper.getCenteredTextPosition(loadingText, loadingTextFont, g, imgLeft, imgTop,
					THUMBNAIL_IMAGE_WIDTH, THUMBNAIL_IMAGE_HEIGHT);

			g.setFont(loadingTextFont);
			g.drawString(loadingText, drawPosition.getX(), drawPosition.getY());
		}
		g.setColor(ColorSwatch.SHADOW);
		g.setStroke(Resource.getGameObjectThinStroke());
		g.drawRect(imgLeft, imgTop, THUMBNAIL_IMAGE_WIDTH, THUMBNAIL_IMAGE_HEIGHT);

		Font levelNameFont = Resource.getInstance().getDefaultFont(50);
		Vector2 levelNameTextPos = Helper.getCenteredTextPosition(mapName, levelNameFont, g, x, y, width,
				getListItemHeight());
		g.setFont(levelNameFont);
		g.setColor(isSelected ? ColorSwatch.BACKGROUND : ColorSwatch.FOREGROUND);
		g.drawString(mapName, x + THUMBNAIL_IMAGE_WIDTH + CONTENT_MARGIN_LEFT + TEXT_MARGIN, levelNameTextPos.getY());

	}

	private LevelDataBuilder levelDataBuilder;
	private String levelDataJSON;

	public static LevelData createLevelDataAsJSON(LevelDataBuilder builder) {
		return new LevelData(builder);
	}

	private LevelData(LevelDataBuilder builder) {
		this.levelDataBuilder = builder;
		levelDataJSON = createLevelData(builder.getBlocks(), builder.getSlopes(), builder.getFloorSwitches(),
				builder.getGateTogateTeles(), builder.getTeleportToArea(), builder.getTelportDests(),
				builder.getGates(), builder.getSwControllers(), builder.getFloorLevel(), builder.getLevelName(),
				builder.getFinishX(), builder.getFinishY(), builder.getStartX(), builder.getStartY(),
				builder.getPlayerCount());
	}

	private String createLevelData(ArrayList<Block> blocks, ArrayList<Slope> slopes,
			ArrayList<FloorSwitch> floorSwitches, ArrayList<GateToGateTeleport> gateTogateTeles,
			ArrayList<TeleportToArea> teleportToArea, ArrayList<TeleportDestionation> telportDests,
			ArrayList<Gate> gates, ArrayList<SwitchController> swControllers, int[][] floorLevel, String levelName,
			ArrayList<Integer> finishX, ArrayList<Integer> finishY, int[] startX, int[] startY, int playerCount) {

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
		String controllerObjectIndexJSON = getControlObjectSetIndexList(swControllers, gates, gateTogateTeles,
				teleportToArea);
		String floorLevelJSON = getFloorLevelAsString(floorLevel);
		String finishXJSON = getFinishAreaAsJSON(finishX);
		String finishYJSON = getFinishAreaAsJSON(finishY);
		String startXJSON = getStartPositionASJSON(startX);
		String startYJSON = getStartPositionASJSON(startY);
		String levelNameJSON = getLevelNameAsJSON(levelName);
		String playerCounJSON = getPlayerCountAsJSON(playerCount);

		HashMap<String, String> levelDataMap = new HashMap<>();
		levelDataMap.put("LevelName", levelNameJSON);
		levelDataMap.put("PlayerCount", playerCounJSON);
		levelDataMap.put("Block", blockJSON);
		levelDataMap.put("Slope", slopeJSON);
		levelDataMap.put("FloorSwitch", fsJSON);
		levelDataMap.put("GateToGateTeleport", gateToGateJSON);
		levelDataMap.put("GateLink", gateLinkJSON);
		levelDataMap.put("TeleportToArea", teleToAreaJSON);
		levelDataMap.put("TeleportDestination", teleportDestination);
		levelDataMap.put("Gate", gateJSON);
		levelDataMap.put("SwitchController", swControllerJSON);
		levelDataMap.put("ControllerSwitchSet", controllerSwitchSetIndexJSON);
		levelDataMap.put("ControlObject", controllerObjectIndexJSON);
		levelDataMap.put("FloorLevel", floorLevelJSON);
		levelDataMap.put("FinishX", finishXJSON);
		levelDataMap.put("FinishY", finishYJSON);
		levelDataMap.put("StartX", startXJSON);
		levelDataMap.put("StartY", startYJSON);

		Gson gson = new Gson();
		return gson.toJson(levelDataMap);

	}

	public String getLevelDataJSON() {
		return levelDataJSON;
	}

	private String getBlockArrayAsJSON(ArrayList<Block> a) {
		Gson gson = new Gson();
		return gson.toJson(a);

	}

	private String getSlopeArrayAsJSON(ArrayList<Slope> s) {
		Gson gson = new Gson();
		return gson.toJson(s);

	}

	private String getFloorSwitchesArrayAsJSON(ArrayList<FloorSwitch> fs) {
		Gson gson = new Gson();
		return gson.toJson(fs);

	}

	private String getGateToGateTeleportAsJSON(ArrayList<GateToGateTeleport> gateTele) {
		Gson gson = new Gson();
		return gson.toJson(gateTele);

	}

	private String getPortalLinkIndexAsJSON(ArrayList<GateToGateTeleport> gateTeleport) {
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

	private String getTeleportToAreaAsJSON(ArrayList<TeleportToArea> gateTele) {
		Gson gson = new Gson();
		return gson.toJson(gateTele);
	}

	private String getTeleportDestinationAsJSON(ArrayList<TeleportDestionation> destinations) {
		Gson gson = new Gson();
		return gson.toJson(destinations);
	}

	private String getGateArrayAsJSON(ArrayList<Gate> gates) {
		Gson gson = new Gson();
		return gson.toJson(gates);
	}

	private String getControllerArrayAsJSON(ArrayList<SwitchController> swControllers) {
		Gson gson = new Gson();
		return gson.toJson(swControllers);
	}

	private String getControllSwitchesSetIndexList(ArrayList<SwitchController> swControllers,
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

	private String getControlObjectSetIndexList(ArrayList<SwitchController> swController, ArrayList<Gate> gates,
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

	private String getFloorLevelAsString(int[][] floorLevel) {
		Gson gson = new Gson();
		return gson.toJson(floorLevel);
	}

	private String getFinishAreaAsJSON(ArrayList<Integer> finish) {
		Gson gson = new Gson();
		return gson.toJson(finish);
	}

	private String getLevelNameAsJSON(String levelName) {
		Gson gson = new Gson();
		return gson.toJson(levelName);
	}

	private String getPlayerCountAsJSON(int playerCount) {
		Gson gson = new Gson();
		return gson.toJson(playerCount);
	}

	private String getStartPositionASJSON(int[] start) {
		Gson gson = new Gson();
		return gson.toJson(start);
	}

	private final Type blockType = new TypeToken<ArrayList<Block>>() {
	}.getType();
	private final Type slopeType = new TypeToken<ArrayList<Slope>>() {
	}.getType();
	private final Type floorSwitchType = new TypeToken<ArrayList<FloorSwitch>>() {
	}.getType();
	private final Type teleportToGateType = new TypeToken<ArrayList<GateToGateTeleport>>() {
	}.getType();
	// private final Type gateLinkType = new TypeToken<ArrayList<int[]>>() {}.getType();
	private final Type teleportToAreaType = new TypeToken<ArrayList<TeleportToArea>>() {
	}.getType();
	private final Type teleportDestinationType = new TypeToken<ArrayList<TeleportDestionation>>() {
	}.getType();
	private final Type gateType = new TypeToken<ArrayList<Gate>>() {
	}.getType();
	private final Type switchControllerType = new TypeToken<ArrayList<SwitchController>>() {
	}.getType();
	private final Type controlObjectIndexType = new TypeToken<ArrayList<int[]>>() {
	}.getType();
	private final Type switchSetIndexType = new TypeToken<ArrayList<int[]>>() {
	}.getType();
	private final Type hashType = new TypeToken<HashMap<String, String>>() {
	}.getType();
	private final Type integerType = new TypeToken<ArrayList<Integer>>() {
	}.getType();

	private HashMap<String, String> levelContents;
	private ArrayList<Block> blocks;
	private ArrayList<Slope> slopes;
	private ArrayList<FloorSwitch> floorSwitches;
	private ArrayList<GateToGateTeleport> gateTogateTeles;
	private int[] gateLink;
	private ArrayList<TeleportToArea> teleportToArea;
	private ArrayList<TeleportDestionation> teleportDests;
	private ArrayList<Gate> gates;
	private ArrayList<SwitchController> swControllers;
	private ArrayList<int[]> controlObjectIndex;
	private ArrayList<int[]> switchSetIndex;
	private int[][] floorLevel;
	private String levelName;
	private ArrayList<Integer> finishX;
	private ArrayList<Integer> finishY;
	private int[] startX;
	private int[] startY;

	private LevelData(String jsonContent) {
		levelContents = getContentList(jsonContent);
		blocks = getBlocks(levelContents.get("Block"));
		slopes = getSlopes(levelContents.get("Slope"));
		floorSwitches = getFloorSwitches(levelContents.get("FloorSwitch"));
		gateTogateTeles = getGateToGateTeleport(levelContents.get("GateToGateTeleport"));
		gateLink = getGateLink(levelContents.get("GateLink"));
		teleportToArea = getTeleportToArea(levelContents.get("TeleportToArea"));
		teleportDests = getTeleportDestination(levelContents.get("TeleportDestination"));
		gates = getGates(levelContents.get("Gate"));
		swControllers = getController(levelContents.get("SwitchController"));
		switchSetIndex = getSwitchSetIndex(levelContents.get("ControllerSwitchSet"));
		controlObjectIndex = getControlObject(levelContents.get("ControlObject"));
		floorLevel = getFloorLevel(levelContents.get("FloorLevel"));
		this.mapName = getLevelNameFromGson(levelContents.get("LevelName"));
		this.playerCount = getPlayerCountFromGson(levelContents.get("PlayerCount"));
		finishX = getFisnishArea(levelContents.get("FinishX"));
		finishY = getFisnishArea(levelContents.get("FinishY"));
		startX = getPositionArray(levelContents.get("StartX"));
		startY = getPositionArray(levelContents.get("StartY"));

	}

	public static LevelData parse(String jsonContent) {
		return new LevelData(1, jsonContent);
	}

	// mock up only
	private LevelData(int p, String jsonContent) {
		this.mapName = jsonContent;
		this.playerCount = p;
	}

	public HashMap<String, String> getContentList(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, hashType);
	}

	public ArrayList<Block> getBlocks(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, blockType);
	}

	public ArrayList<Slope> getSlopes(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, slopeType);
	}

	public ArrayList<FloorSwitch> getFloorSwitches(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, floorSwitchType);
	}

	public ArrayList<GateToGateTeleport> getGateToGateTeleport(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, teleportToGateType);
	}

	public int[] getGateLink(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, int[].class);
	}

	public ArrayList<TeleportToArea> getTeleportToArea(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, teleportToAreaType);
	}

	public ArrayList<TeleportDestionation> getTeleportDestination(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, teleportDestinationType);
	}

	public ArrayList<Gate> getGates(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, gateType);
	}

	public ArrayList<SwitchController> getController(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, switchControllerType);
	}

	public ArrayList<int[]> getSwitchSetIndex(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, switchSetIndexType);
	}

	public ArrayList<int[]> getControlObject(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, controlObjectIndexType);
	}

	public int[][] getFloorLevel(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, int[][].class);
	}

	public int[] getPositionArray(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, int[].class);
	}

	public ArrayList<Integer> getFisnishArea(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, integerType);
	}

	public String getLevelNameFromGson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, String.class);
	}

	public int getPlayerCountFromGson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, int.class);
	}

	public ArrayList<int[]> getControlObjectIndex() {
		return controlObjectIndex;
	}

	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public ArrayList<Slope> getSlopes() {
		return slopes;
	}

	public ArrayList<FloorSwitch> getFloorSwitches() {
		return floorSwitches;
	}

	public ArrayList<GateToGateTeleport> getGateTogateTeles() {
		return gateTogateTeles;
	}

	public int[] getGateLink() {
		return gateLink;
	}

	public ArrayList<TeleportToArea> getTeleportToArea() {
		return teleportToArea;
	}

	public ArrayList<TeleportDestionation> getTeleportDests() {
		return teleportDests;
	}

	public ArrayList<Gate> getGates() {
		return gates;
	}

	public ArrayList<SwitchController> getSwControllers() {
		return swControllers;
	}

	public ArrayList<int[]> getSwitchSetIndex() {
		return switchSetIndex;
	}

	public int[][] getFloorLevel() {
		return floorLevel;
	}

	public ArrayList<Integer> getFinishX() {
		return finishX;
	}

	public ArrayList<Integer> getFinishY() {
		return finishY;
	}

	public int[] getStartX() {
		return startX;
	}

	public int[] getStartY() {
		return startY;
	}

	private transient String levelFileName;

	public void setLevelFileName(String levelFileName) {
		this.levelFileName = levelFileName;
	}

	public String getLevelFileName() {
		return levelFileName;
	}

}
