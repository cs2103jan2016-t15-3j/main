package logic;

import java.util.ArrayList;

public class Tag {
	private String name;
	private boolean isSelected;

	public Tag(String name, boolean isSelected) {
		this.name = name;
		this.isSelected = isSelected;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getName() {
		return name;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

}
