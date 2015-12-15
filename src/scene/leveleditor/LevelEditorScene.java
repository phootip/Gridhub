package scene.leveleditor;

import scene.level.LevelData;
import scene.play.PlayScene;
import stage.GameStage;
import stage.GameStageType;

public final class LevelEditorScene extends PlayScene {

	public LevelEditorScene(LevelData levelData) {
		super(levelData);
	}

	@Override
	protected void createGameStage() {
		this.gameStage = new GameStage(this.levelData, GameStageType.LEVEL_EDITOR);
	}

}
