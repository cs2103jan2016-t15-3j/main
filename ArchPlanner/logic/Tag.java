package logic;

/**
 * This class defines the the properties of a Tag object.
 * 
 * @@author A0140021J
 *
 */
public class Tag {
	
	//This variable is the name of the Tag object
	private String _name;
	
	//This variable indicates whether the Tag is selected.
	private boolean _isSelected;
	
	//This is constructor of the class.
	public Tag(String name, boolean isSelected) {
		_name = name;
		_isSelected = isSelected;
	}
	
	/**
	 * This is setter method for Tag's name.
	 * 
	 * @param name This will be the name of the Tag.
	 */
	public void setName(String name) {
		_name = name;
	}
	
	/**
	 * This is setter method for isSelected.
	 * 
	 * @param isSelected This will be the status of isSelected of the Tag.
	 */
	public void setIsSelected(boolean isSelected) {
		_isSelected = isSelected;
	}
	
	/**
	 * This is getter method for Tag's name.
	 * 
	 * @return Tag's name.
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * This is getter method for Tag's isSelected.
	 * 
	 * @return Tag's isSelected that indicates whether the Tag is selected.
	 */
	public boolean getIsSelected() {
		return _isSelected;
	}
}
