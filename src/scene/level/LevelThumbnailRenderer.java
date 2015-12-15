package scene.level;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import core.DrawManager;
import stage.GameStage;
import stage.GameStageType;

class LevelThumbnailRenderer {

	private static final int RENDER_IMAGE_WIDTH = 180 * 8;
	private static final int RENDER_IMAGE_HEIGHT = 90 * 8;

	private static class RendererRunnable implements Runnable {

		private RendererRunnable previousRunnable;
		private LevelData levelData;
		private Object finishNotifer = new Object();

		public RendererRunnable(LevelData levelData, RendererRunnable previousRunnable) {
			this.levelData = levelData;
			this.previousRunnable = previousRunnable;
		}

		float[] sharpenKernel = { 0, -1, 0, -1, 5, -1, 0, -1, 0 };
		BufferedImageOp shapenOp = new ConvolveOp(new Kernel(3, 3, sharpenKernel));

		private BufferedImage shapenImage(BufferedImage in) {
			BufferedImage out = DrawManager.getInstance().createBlankBufferedImage(in.getWidth(), in.getHeight(),
					false);
			Graphics2D g = out.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

			g.drawImage(in, shapenOp, 0, 0);

			g.dispose();

			return out;
		}

		private BufferedImage reduceImageSizeByFactorOfTwo(BufferedImage in) {
			BufferedImage out = DrawManager.getInstance().createBlankBufferedImage(in.getWidth() / 2,
					in.getHeight() / 2, false);
			Graphics2D g = out.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

			g.drawImage(in, 0, 0, in.getWidth() / 2, in.getHeight() / 2, null);

			g.dispose();

			return out;
		}

		@Override
		public void run() {
			// System.out.println("Render job \"" + levelData.getMapName() + "\" : start");

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

			// System.out.println("Render job \"" + levelData.getMapName() + "\" : rendering");

			BufferedImage renderImg = DrawManager.getInstance().createBlankBufferedImage(RENDER_IMAGE_WIDTH,
					RENDER_IMAGE_HEIGHT, false);
			Graphics2D graphic = renderImg.createGraphics();
			graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphic.setClip(0, 0, RENDER_IMAGE_WIDTH, RENDER_IMAGE_HEIGHT);

			GameStage renderStage = new GameStage(levelData, GameStageType.THUMBNAIL);
			renderStage.update(0);
			renderStage.draw(graphic, RENDER_IMAGE_WIDTH, RENDER_IMAGE_HEIGHT);
			graphic.dispose();

			// Resize image

			renderImg = shapenImage(renderImg);
			for (int i = 0; i < 3; i++) {
				renderImg = reduceImageSizeByFactorOfTwo(renderImg);
			}

			this.levelData.setThumbnail(renderImg);

			// System.out.println("Render job \"" + levelData.getMapName() + "\" : finished");

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
