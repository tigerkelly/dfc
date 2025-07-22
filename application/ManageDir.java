package application;

public class ManageDir {

	private String path;
	private String recursive;
	private String found;
	
	public ManageDir(String path, String recursive) {
		this.path = path;
		this.recursive = recursive;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getRecursive() {
		return recursive;
	}

	public void setRecursive(String recursive) {
		this.recursive = recursive;
	}

	public String getFound() {
		return found;
	}

	public void setFound(String found) {
		this.found = found;
	}

	@Override
	public String toString() {
		return "ManageDir [path=" + path + ", recursive=" + recursive + ", found=" + found + "]";
	}
}
