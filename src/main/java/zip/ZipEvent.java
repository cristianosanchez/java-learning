package zip;


import java.io.File;

public class ZipEvent {
	
	private String message = "";
	private File file;
	
	public ZipEvent(File file) {
		this.file = file;
	}

	public ZipEvent(File file, String message) {
		this.message = message;
		this.file = file;
	}

	public String getMessage() {
		return message;
	}

	public File getFile() {
		return file;
	}
}
