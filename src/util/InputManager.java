package util;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

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

	private Set<Integer> keyPressHasher = new HashSet<>();
	private Set<Integer> keyTriggerHasher = new HashSet<>();

	private Set<Integer> keyPressBuffer = new HashSet<>();
	private Set<Integer> keyReleaseBuffer = new HashSet<>();

	private InputManager() {
		keyPressHasher = new HashSet<>();
		keyTriggerHasher = new HashSet<>();
	}

	/**
	 * Determines whether specified key is currently being pressed or not.
	 * 
	 * @param keyCode
	 *            an integer representing key code to check.
	 * @return Whether or not the specified key is being pressed.
	 */
	public boolean isKeyPressing(int keyCode) {
		return keyPressHasher.contains(keyCode);
	}

	/**
	 * Determines whether specified key is triggering or not.
	 * 
	 * @param keyCode
	 *            an integer representing key code to check.
	 * @return Whether or not the specified key is being triggered.
	 */
	public boolean isKeyTriggering(int keyCode) {
		return keyTriggerHasher.contains(keyCode);
	}

	/**
	 * This should be called from only from listener, and should not be called
	 * explicitly by user.
	 */
	@Override
	public synchronized void keyPressed(KeyEvent e) {
		if (!keyPressHasher.contains(e.getKeyCode())) {
			keyPressBuffer.add(e.getKeyCode());
		}
	}

	/**
	 * This should be called from only from listener, and should not be called
	 * explicitly by user.
	 */
	@Override
	public synchronized void keyReleased(KeyEvent e) {
		keyReleaseBuffer.add(e.getKeyCode());
	}

	/**
	 * This should be called from only from listener, and should not be called
	 * explicitly by user.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Update the InputManager, such as clear triggered value. This should be
	 * called every frame before doing any operation.
	 */
	public synchronized void update() {
		keyTriggerHasher.clear();

		for (Integer i : keyPressBuffer) {
			keyPressHasher.add(i);
			keyTriggerHasher.add(i);
		}

		for (Integer i : keyReleaseBuffer) {
			keyPressHasher.remove(i);
		}
		
		keyPressBuffer.clear();
		keyReleaseBuffer.clear();
	}

}
