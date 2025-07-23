
package application;

import javax.swing.event.EventListenerList;

/**
 * This is a custom event to notify the workbench that a page has been changed.
 * @author Kelly Wiles
 *
 */
public class Changes {
	protected EventListenerList listenerList = new EventListenerList();
	
	public final static int FILE_CREATE = 1;
	public final static int FILE_MODIFY = 2;
	public final static int FILE_DELETE = 3;
	public final static int DIR_DELETE = 4;
	
	// This methods allows classes to register for Changes
    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    // This methods allows classes to unregister for Changes
    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    // This private class is used to fire Changes
    public void fireChange(ChangeEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener)listeners[i+1]).changeEventOccurred(evt);
            }
        }
    }
}
