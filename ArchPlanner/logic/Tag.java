package logic;

public class Tag {
	private String _name;
	private boolean _isSelected;
	
	public Tag(String name, boolean isSelected) {
		_name = name;
		_isSelected = isSelected;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public void setIsSelected(boolean isSelected) {
		_isSelected = isSelected;
	}
	
	public String getName() {
		return _name;
	}
	
	public boolean getIsSelected() {
		return _isSelected;
	}
}
