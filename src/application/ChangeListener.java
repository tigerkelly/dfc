
package application;

import java.util.EventListener;

/**
 * Part of the custom event Changes class
 * @author Kelly Wiles
 *
 */
public interface ChangeListener extends EventListener {
	public abstract void changeEventOccurred(ChangeEvent e);
}
