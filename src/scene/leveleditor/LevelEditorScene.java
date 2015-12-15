package scene.leveleditor;

import java.io.FileNotFoundException;

import scene.level.LevelData;
import scene.level.LevelDataBuilder;
import scene.level.LevelFileManager;
import scene.play.PlayScene;
import stage.GameStage;
import stage.GameStageType;

public final class LevelEditorScene extends PlayScene {

	public LevelEditorScene(LevelData levelData) {
		super(levelData);
	}

	private String[] menuItems = new String[] { "Save Level", "Load Last Saved", "Exit" };

	@Override
	protected String[] getMenuItems() {
		return menuItems;
	}

	@Override
	protected void performMenuAction(int selectedMenuItem) {
		if (selectedMenuItem == 0) {
			try {
				LevelFileManager.getInstance().saveLevelData(gameStage.buildLevelData());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			super.performMenuAction(selectedMenuItem - 1);
		}
	}

	@Override
	protected void createGameStage() {
		this.gameStage = new GameStage(this.levelData, GameStageType.LEVEL_EDITOR);
	}

}
