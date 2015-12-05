package util;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

// TODO: Implement MouseListener and MouseMoveListener

/**
 * This manages the input of the game, e.g. keyboard. It also support inputing
 * in the triggering way, in the similar manner to monostable multivibrator,
 * i.e. the input that is asserted true for only one frame.
 * 
 * @author Kasidit Iamthong
 *
 */
public class InputManager implements KeyListener {
	private final static InputManager instance = new InputManager();

	/**
	 * Get an instance of {@link InputManager}.
	 * 
	 * @return An instance of {@link InputManager}.
	 */
	public static InputManager getInstance() {
		return instance;
	}

	/**
	 * Set keyboard and mouse listener to specified {@code Component}.
	 * 
	 * @param c
	 *            the component to listen.
	 */
	public static void setListenerTo(Component c) {
		c.addKeyListener(InputManager.getInstance());
	}

	private HashSet<Integer> keyPressHasher = new HashSet<>();
	private HashSet<Integer> keyTriggerHasher = new HashSet<>();

	private InputManager() {
	}

	/**
	 * Determines whether specified key is currently being pressed or not
	 * 
	 * @param keyCode
	 *            an integer representing key code to check.
	 * @return Whether or not the specified key is being pressed.
	 */
	public boolean isKeyPressing(int keyCode) {
		return keyPressHasher.contains(keyCode);
	}

	/**
	 * This should be called from only from listener, and should not be called
	 * explicitly by user.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		keyPressHasher.add(e.getKeyCode());
		keyTriggerHasher.add(e.getKeyCode());
	}

	/**
	 * This should be called from only from listener, and should not be called
	 * explicitly by user.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		keyPressHasher.remove(e.getKeyCode());
	}

	/**
	 * This should be called from only from listener, and should not be called
	 * explicitly by user.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Reset the trigger values of keyboard. This should be called every frame.
	 */
	public void resetTriggerer() {
		keyTriggerHasher.clear();
	}

}
