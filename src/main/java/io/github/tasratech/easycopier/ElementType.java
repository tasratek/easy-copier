package io.github.tasratech.easycopier;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public enum ElementType {

	FILE("File", "file.png"), FOLDER("Folder", "folder.png"), UNKNOWN("Unknown", "unknown.png");

	private ElementType(String name, String iconPath) {
		this.name = name;
		icon = new ImageIcon(getClass().getClassLoader().getResource(iconPath));
	}

	private String name;
	private Icon icon;

	public String getName() {
		return name;
	}

	public Icon getIcon() {
		return icon;
	}
}
