package core;

import javax.swing.JFrame;

import util.Constants;

public class Main {
	public static void main(String[] args) {

		// Create JFrame
		JFrame frame = new JFrame(Constants.PROGRAM_NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		if (Constants.IS_FULLSCREEN) {
			frame.setUndecorated(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			frame.getContentPane().setPreferredSize(Constants.DEFAULT_SCREEN_SIZE);
			frame.pack();
		}

		// Add canvas to the frame
		DrawManager.getInstance().addCanvasInto(frame);
		DrawManager.getInstance().setCanvasSize(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());

		frame.setBackground(java.awt.Color.BLACK);
		frame.setVisible(true);
		
		// Set initial scene
		
		SceneManager.getInstance().setNextScene(new scene.TestScene());

		// Game looper
		final long maximumWaitTime = 1000000 / Constants.MAX_FRAME_PER_SECOND;
		long updateTime;
		while (true) {
			// Perform a game update (including game logic and painting)
			updateTime = System.nanoTime();
			SceneManager.getInstance().update();
			updateTime = System.nanoTime() - updateTime;
			
			if (updateTime < maximumWaitTime) {
				FPSCollector.add(Constants.MAX_FRAME_PER_SECOND);
			} else {
				FPSCollector.add(Math.round(1000000 / updateTime));
			}

			// Perform sleeping to limit maximum FPS
			if (updateTime < maximumWaitTime) {
				try {
					Thread.sleep(maximumWaitTime - updateTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
