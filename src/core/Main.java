package core;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import scene.level.LevelFileManager;
import stage.GameStageType;
import util.Constants;
import util.Constants.ColorSwatch;
import util.InputManager;
import util.Resource;

public class Main {

	public static void main(String[] args) {

		if (!Resource.getInstance().initialize()) {
			JOptionPane.showMessageDialog(null, "Resource loading fail.", "Cannot open game",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!LevelFileManager.getInstance().initialize()) {
			JOptionPane.showMessageDialog(null, "Level files loading fail.", "Cannot open game",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Create JFrame
		JFrame frame = new JFrame(Constants.PROGRAM_NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// frame.setResizable(false);
		frame.setMinimumSize(Constants.MINIMUM_WINDOW_SIZE);
		frame.getContentPane().addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent arg0) {
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				DrawManager.getInstance().setCanvasSize(frame.getContentPane().getWidth(),
						frame.getContentPane().getHeight());
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
			}

			@Override
			public void componentHidden(ComponentEvent arg0) {
			}
		});

		if (Constants.IS_FULLSCREEN) {
			frame.setUndecorated(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			frame.getContentPane().setPreferredSize(Constants.DEFAULT_SCREEN_SIZE);
			frame.pack();
		}

		InputManager.setListenerTo(frame);

		frame.getContentPane().setBackground(ColorSwatch.BACKGROUND);
		frame.setVisible(true);

		// Add canvas to the frame
		DrawManager.getInstance().addCanvasInto(frame);
		DrawManager.getInstance().setCanvasSize(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());

		// Set initial scene

		 SceneManager.getInstance().setNextScene(new scene.mainmenu.MainMenuScene(false));
//		SceneManager.getInstance().setNextScene(new stage.GameStage(GameStageType.PLAY));

		// Game looper
		final long maximumWaitTime = 1000000000 / Constants.MAX_FRAME_PER_SECOND;
		long updateTime;
		while (true) {
			// Perform a game update (including game logic and painting)
			updateTime = System.nanoTime();
			SceneManager.getInstance().update();
			updateTime = System.nanoTime() - updateTime;

			if (updateTime < maximumWaitTime) {
				FPSCollector.add(Constants.MAX_FRAME_PER_SECOND);
			} else {
				FPSCollector.add(Math.round(1000000000 / updateTime));
			}

			// Perform sleeping to limit maximum FPS
			if (updateTime < maximumWaitTime) {
				try {
					Thread.sleep((maximumWaitTime - updateTime) / 1000000);
				} catch (InterruptedException e) {
					Thread.interrupted();
					e.printStackTrace();
				}
			}
		}

	}
}
