
package application;

import java.util.EventObject;

/**
 * Part of the custom event Changes class.
 * @author Kelly Wiles
 *
 */
public class ChangeEvent extends EventObject {
	public int type = 0;
	public String data = null;

	public ChangeEvent(Object arg0, String data) {
		super(arg0);
		type = (Integer)arg0;
		this.data = data;
	}

	private static final long serialVersionUID = 1L;

}
