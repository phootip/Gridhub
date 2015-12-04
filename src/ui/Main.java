package ui;

import java.awt.Color;

import javax.swing.JFrame;

import util.Settings;

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame(Settings.PROGRAM_NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		if (Settings.IS_FULLSCREEN) {
			frame.setUndecorated(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			frame.getContentPane().setPreferredSize(Settings.DEFAULT_SCREEN_SIZE);
			frame.pack();
		}

		frame.getContentPane().setBackground(Color.BLACK);

		frame.setVisible(true);
	}
}
