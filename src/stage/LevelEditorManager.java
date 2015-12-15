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
import stage.editor.AddPane.AddableObject;
import stage.editor.EditorCursor.EditorCursorState;
import stage.editor.EditorCursor;
import stage.editor.Pane;
import stage.gameobj.Block;
import util.InputManager;

public class LevelEditorManager {

	enum LevelEditorOperation {
		NONE, ADD;
	}

	private List<Pane> paneList;
	private AddPane addPane;
	private EditorCursor cursor;
	private GameStage callback;

	private LevelEditorOperation currentOperation = LevelEditorOperation.NONE;

	public LevelEditorManager(EditorCursor cursor, GameStage callback) {
		this.cursor = cursor;
		this.paneList = new ArrayList<>();
		this.callback = callback;
	}

	public void update(int step) {

		Iterator<Pane> paneIt = paneList.iterator();
		while (paneIt.hasNext()) {
			Pane pane = paneIt.next();
			if (pane.shouldRemovePane()) {
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
				}
				break;
			case ADD:
				if (addPane.getSelectedAddableObject() != null) {
					if (callback.isAbleToPlaceObjectAtCursor(addPane.getSelectedAddableObject())) {
						cursor.setState(EditorCursorState.VALID);
					} else {
						cursor.setState(EditorCursorState.INVALID);
					}
				}
				if (isEscapeKeyHandled()) {
					addPane.setVisible(false);
					currentOperation = LevelEditorOperation.NONE;
				} else if (InputManager.getInstance().isKeyTriggering(KeyEvent.VK_ENTER)) {
					if (addPane.getSelectedAddableObject() != null) {
						if (callback.isAbleToPlaceObjectAtCursor(addPane.getSelectedAddableObject())) {

							this.callback.addObjectAtCursor(addPane.getSelectedAddableObject());

							addPane.setVisible(false);
							currentOperation = LevelEditorOperation.NONE;
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
						Block.drawBlock(g, camera, callback.getPlacingObjectPositionAtCursor().toVector3(), false);
						break;
				}

				g.setComposite(AlphaComposite.SrcOver);
			}
		}

	}

}
