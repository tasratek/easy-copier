package io.github.tasratech.easycopier;

import java.io.File;

public class CopyElement {

	private File src;
	private File dest;
	private String status;

	public File getSrc() {
		return src;
	}

	public void setSrc(File src) {
		this.src = src;
	}

	public File getDest() {
		return dest;
	}

	public void setDest(File dest) {
		this.dest = dest;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public CopyElement(File src, File dest) {
		this.src = src;
		this.dest = dest;
		updateStatus();
	}

	public ElementType getType() {
		if(src.isFile()) {
			return ElementType.FILE;
		}else if(src.isDirectory()) {
			return ElementType.FOLDER;
		}else {
			return ElementType.UNKNOWN;
		}
	}

	public void updateStatus() {
		if(dest.exists()) {
			status = "Exists";
		}else {
			status = "Unexists";
		}
	}
}
