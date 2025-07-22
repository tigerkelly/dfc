package application;

import java.io.File;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.regex.Pattern;

public class DfcThread extends Thread {

	private boolean stopWatching = false;
	
	private DfcGlobal dg = DfcGlobal.getInstance();
	
	public DfcThread(String name) {
		if (name != null)
			setName(name);
	}
	
	@Override
	public void run() {
		
//		long skipCount = 0;
		WatchKey key;
		while (stopWatching == false) {
			try {
				if (dg.sleepMode == false) {
					key = dg.ws.take();
				} else {
					Thread.sleep(200);
					continue;
				}
				
				
				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					
					String path = key.watchable().toString();
					
					boolean flag = false;
					for (String s : dg.ignoreExts) {
						String p = (String)event.context().toString();
                		if (p.endsWith(s) == true) {
                			flag = true;
                			break;
                		}
                	}
					
					if (flag == true) {
						continue;
					}
					
					flag = false;
					for (String s : dg.ignoreFiles) {
						String p = (String)event.context().toString();
						boolean isMatch = Pattern.matches(s, p);
                		if (isMatch == true) {
                			flag = true;
                			break;
                		}
                	}
					
					if (flag == true) {
						continue;
					}
					
					if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
						
						ChangeEvent ce = new ChangeEvent(Changes.FILE_CREATE, path + File.separator + event.context());
						dg.changes.fireChange(ce);
						
					} else if(kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
						File f = new File(path + File.separator + event.context());
						if (f.isDirectory() == false) {
							ChangeEvent ce = new ChangeEvent(Changes.FILE_MODIFY, path + File.separator + event.context());
							dg.changes.fireChange(ce);
						}
					} else if(kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
						ChangeEvent ce = new ChangeEvent(Changes.FILE_DELETE, path + File.separator + event.context());
						dg.changes.fireChange(ce);
					}
		        }
		        key.reset(); // Reset the key to receive further events
			} catch (InterruptedException e) {
				// e.printStackTrace();
			} catch (ClosedWatchServiceException e2) {
				// don't display anything.
			}
		}
	}
	
	public void stopWatching() {
		stopWatching = true;
		this.interrupt();
	}
}
