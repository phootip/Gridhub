package scene.level;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import core.DrawManager;
import stage.GameStage;
import stage.GameStageType;

class LevelThumbnailRenderer {

	private static final int THUMBNAIL_IMAGE_WIDTH = 1000;
	private static final int THUMBNAIL_IMAGE_HEIGHT = 500;

	private static class RendererRunnable implements Runnable {

		private RendererRunnable previousRunnable;
		private LevelData levelData;
		private Object finishNotifer = new Object();

		public RendererRunnable(LevelData levelData, RendererRunnable previousRunnable) {
			this.levelData = levelData;
			this.previousRunnable = previousRunnable;
		}

		@Override
		public void run() {
			System.out.println("Render job \"" + levelData.getMapName() + "\" : start");

			// Wait for previous runnable job to complete first
			if (previousRunnable != null) {
				synchronized (previousRunnable.finishNotifer) {
					try {
						previousRunnable.finishNotifer.wait();
					} catch (InterruptedException e) {
						// The code should not reach here...
						e.printStackTrace();
					}
				}
			}

			System.out.println("Render job \"" + levelData.getMapName() + "\" : rendering");

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			BufferedImage thumbnailImg = DrawManager.getInstance().createBlankBufferedImage(THUMBNAIL_IMAGE_WIDTH,
					THUMBNAIL_IMAGE_HEIGHT, false);
			Graphics2D graphic = thumbnailImg.createGraphics();
			graphic.setClip(0, 0, THUMBNAIL_IMAGE_WIDTH, THUMBNAIL_IMAGE_HEIGHT);

			GameStage renderStage = new GameStage(levelData, GameStageType.THUMBNAIL);
			renderStage.update(0);
			renderStage.draw(graphic, THUMBNAIL_IMAGE_WIDTH, THUMBNAIL_IMAGE_HEIGHT);
			graphic.dispose();

			this.levelData.setThumbnail(thumbnailImg);

			System.out.println("Render job \"" + levelData.getMapName() + "\" : finished");

			synchronized (finishNotifer) {
				finishNotifer.notifyAll();
			}
		}

	}

	protected static void startRenderThumbnail(Chapter chapter) {
		if (!chapter.isRenderingJobStarted()) {
			chapter.setRenderingJobStarted(true);

			System.out.println("Thumbnail rendering job of chapter \"" + chapter.getChapterName() + "\" has started");

			RendererRunnable previousJob = null;
			for (LevelData level : chapter.getLevelDataList()) {
				previousJob = new RendererRunnable(level, previousJob);
				new Thread(previousJob).start();
			}
		}
	}

}
