package application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangedFile {

	private String fileName;
	private String path;
	private String date;
	private String event;
	private String type;
	
	private DfcGlobal dg = DfcGlobal.getInstance();
	
	public ChangedFile(String fileName, String path, String event) {
		this.fileName = fileName;
		this.path = path;
		
		DateFormat simple = new SimpleDateFormat(dg.tfmt);
		Date result = new Date();
		this.date = simple.format(result);
		
		if (event != null)
			this.event = event;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ChangedFile [fileName=" + fileName + ", path=" + path + ", date=" + date + ", event=" + event
				+ ", type=" + type + ", dg=" + dg + "]";
	}
}
