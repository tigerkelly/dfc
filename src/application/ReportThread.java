package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportThread extends Thread {
	
	private boolean stopReport = false;
	private DfcGlobal dg = DfcGlobal.getInstance();
	
	public ReportThread(String name) {
		if (name != null)
			setName(name);
	}
	
	@Override
	public void run() {
		
		String path = dg.ini.getString("System", "reportPath");
		String t = dg.ini.getString("System", "reportTime");
		boolean enabled = dg.ini.getBoolean("System", "reportEnable");
		
		while (stopReport == false) {
			
			LocalDateTime currentDateTime = LocalDateTime.now();
			
			String pattern1 = "HH:mm";
	        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern(pattern1);
	        String fd1 = currentDateTime.format(formatter1);
	        
	        if (path == null) {
	        	try {
					Thread.sleep(61000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        	continue;
	        }
	        
	        if (fd1.equals(t) == true && enabled == true) {
	        	dg.generateReport(currentDateTime, path);
	        }
			
			try {
				Thread.sleep(61000);
			} catch (InterruptedException e) {
				if (e.getMessage().contains("sleep interrupt") == false)
					e.printStackTrace();
			}
			
			if (stopReport == false)
				dg.purgeReports(currentDateTime, path);
		}
	}
	
	public void stopReporting() {
		stopReport = true;
		this.interrupt();
	}

}
