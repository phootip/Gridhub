package stage;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import stage.GameStage;
import stage.editor.AddPane;
import stage.editor.AddableObject;
import stage.editor.EditorCursor.EditorCursorState;
import stage.editor.IdlePane;
import stage.editor.EditorCursor;
import stage.editor.Pane;
import stage.gameobj.Block;
import stage.gameobj.FloorPiece;
import stage.gameobj.ObjectVector;
import stage.gameobj.Slope;
import stage.gameobj.TeleportDestionation;
import stage.gameobj.TeleportGate;
import util.InputManager;

public class LevelEditorManager {

	enum LevelEditorOperation {
		NONE, ADD;
	}

	private List<Pane> paneList;
	private AddPane addPane;
	private IdlePane idlePane = new IdlePane();
	private EditorCursor cursor;
	private GameStage stage;

	private LevelEditorOperation currentOperation = LevelEditorOperation.NONE;

	public LevelEditorManager(EditorCursor cursor, GameStage stage) {
		this.cursor = cursor;
		this.paneList = new ArrayList<>();
		this.stage = stage;

		this.idlePane.setVisible(true);
		this.paneList.add(idlePane);
	}

	public void update(int step) {

		Iterator<Pane> paneIt = paneList.iterator();
		while (paneIt.hasNext()) {
			Pane pane = paneIt.next();
			if (pane.shouldRemovePane() && !(pane instanceof IdlePane)) {
				paneIt.remove();
			} else {
				pane.update(step);
			}
		}

		switch (currentOperation) {
			case NONE:
				if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_1)) {
					currentOperation = LevelEditorOperation.ADD;

					addPane = new AddPane();
					addPane.setVisible(true);
					paneList.add(addPane);

					this.idlePane.setVisible(false);
				}
				if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_MINUS)) {
					int oldZValue = this.stage.getFloorLevelMap().getZValueFromXY(cursor.getCurrentX(),
							cursor.getCurrentY());
					if (oldZValue > 0 && this.stage.isAbleToPlaceObjectAtCursor(null)) {
						this.stage.getFloorLevelMap().setZValue(cursor.getCurrentX(), cursor.getCurrentY(),
								oldZValue - 1);
						this.stage.constructFloorPieces(true);
						for (FloorPiece piece : this.stage.getFloorPieceList()) {
							if (piece.getObjectVector().equals(new ObjectVector(cursor.getCurrentX(),
									cursor.getCurrentY(), oldZValue - 1, "FloorPiece"))) {
								piece.setZAnimation(1);
							}
						}
					}
				}
				if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_EQUALS)) {
					int oldZValue = this.stage.getFloorLevelMap().getZValueFromXY(cursor.getCurrentX(),
							cursor.getCurrentY());
					if (oldZValue < 10 && this.stage.isAbleToPlaceObjectAtCursor(null)) {
						this.stage.getFloorLevelMap().setZValue(cursor.getCurrentX(), cursor.getCurrentY(),
								oldZValue + 1);
						this.stage.constructFloorPieces(true);
						for (FloorPiece piece : this.stage.getFloorPieceList()) {
							if (piece.getObjectVector().equals(new ObjectVector(cursor.getCurrentX(),
									cursor.getCurrentY(), oldZValue + 1, "FloorPiece"))) {
								piece.setZAnimation(-1);
							}
						}
					}
				}

				break;
			case ADD:
				boolean isPlaceable;
				if (addPane.getSelectedAddableObject() == AddableObject.SLOPE) {
					isPlaceable = stage.isAbleToPlaceSlopeAtCursor(addPane.getSlopeAlignment());
				} else if (addPane.getSelectedAddableObject() == AddableObject.TELEPORT
						&& addPane.getOldPlacePosition() != null) {
					isPlaceable = stage.isAbleToPlaceObjectAtCursor(addPane.getSelectedAddableObject());
					isPlaceable &= (addPane.getOldPlacePosition().getX() != cursor.getCurrentX())
							|| (addPane.getOldPlacePosition().getY() != cursor.getCurrentY());
				} else {
					isPlaceable = stage.isAbleToPlaceObjectAtCursor(addPane.getSelectedAddableObject());
				}

				if (addPane.getSelectedAddableObject() != null) {
					cursor.setState(isPlaceable ? EditorCursorState.VALID : EditorCursorState.INVALID);
				}

				if (isEscapeKeyHandled()) {
					this.idlePane.setVisible(true);
					addPane.setVisible(false);
					currentOperation = LevelEditorOperation.NONE;
				} else if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_ENTER)) {
					if (addPane.getSelectedAddableObject() != null) {

						if (addPane.getSelectedAddableObject() == AddableObject.SLOPE) {
							isPlaceable = stage.isAbleToPlaceSlopeAtCursor(addPane.getSlopeAlignment());
						} else {
							isPlaceable = stage.isAbleToPlaceObjectAtCursor(addPane.getSelectedAddableObject());
						}

						if (isPlaceable) {

							switch (addPane.getSelectedAddableObject()) {
								case BOX:
									ObjectVector placePosition = this.stage.getPlacingObjectPositionAtCursor();
									Block obj = new Block(placePosition.getX(), placePosition.getY(),
											placePosition.getZ(), this.stage.getFloorLevelMap());
									this.stage.addObjectAtCursor(obj);
									break;
								case SLOPE:
									this.stage.addSlopeAtCursor(addPane.getSlopeAlignment());
									break;
								case TELEPORT:
									if (addPane.getOldPlacePosition() == null) {
										addPane.setOldPlacePosition(this.stage.getPlacingObjectPositionAtCursor());
									} else {
										this.stage.addTeleportPairAt(addPane.getOldPlacePosition(),
												this.stage.getPlacingObjectPositionAtCursor());
										addPane.setOldPlacePosition(null);
									}
									break;
							}

							// addPane.setVisible(false);
							// currentOperation = LevelEditorOperation.NONE;
							// this.idlePane.setVisible(true);
						}
					}
				}
				break;
			default:
				break;
		}
	}

	public boolean isEscapeKeyHandled() {
		return currentOperation != LevelEditorOperation.NONE
				&& InputManager.getInstance().isKeyTriggering(KeyEvent.VK_ESCAPE);
	}

	public int drawPane(Graphics2D g, int x, int y, int width, int height) {
		int totalPaneWidth = 0;
		for (Pane pane : paneList) {
			totalPaneWidth += pane.draw(g, totalPaneWidth, y, height);
		}
		return totalPaneWidth;
	}

	public void drawOverlay(Graphics2D g, Camera camera) {
		if (currentOperation == LevelEditorOperation.ADD) {
			if (addPane.getSelectedAddableObject() != null) {
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

				switch (addPane.getSelectedAddableObject()) {
					case BOX:
						Block.drawBlock(g, camera, stage.getPlacingObjectPositionAtCursor().toVector3(), false);
						break;
					case SLOPE:
						ObjectVector middlePos = stage.getPlacingObjectPositionAtCursor();
						ObjectVector startPos = Slope.getSlopeStartPosition(middlePos, addPane.getSlopeAlignment());
						ObjectVector endPos = Slope.getSlopeEndPosition(middlePos, addPane.getSlopeAlignment());

						if (camera.getDrawPosition(startPos.toVector3()).getY() < camera
								.getDrawPosition(endPos.toVector3()).getY()) {
							Slope.drawStartPiece(g, camera, startPos, endPos);
							Slope.drawMiddlePiece(g, camera, startPos, endPos);
							Slope.drawEndPiece(g, camera, startPos, endPos);
						} else {
							Slope.drawEndPiece(g, camera, startPos, endPos);
							Slope.drawMiddlePiece(g, camera, startPos, endPos);
							Slope.drawStartPiece(g, camera, startPos, endPos);
						}

						break;
					case TELEPORT:
						ObjectVector curPos = stage.getPlacingObjectPositionAtCursor();
						if (addPane.getOldPlacePosition() == null) {
							TeleportGate.draw(g, camera, curPos);
						} else {
							TeleportDestionation.draw(g, camera, curPos);
							TeleportGate.draw(g, camera, addPane.getOldPlacePosition());
						}
				}

				g.setComposite(AlphaComposite.SrcOver);
			}
		}

	}

}
