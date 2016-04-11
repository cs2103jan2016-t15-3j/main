package application;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import logic.Tag;

/**
 * This class is used to set up the layout of Tag ToggleButton.
 * 
 * @@author A0140034B
 */
public class TagButton extends ToggleButton{
    
    private static final int WIDTH_OF_SCROLL_BAR = 40;
    
    /**
     * construct a new ToggleButton with the Tag parameters and bind the its width.
     * 
     * @param tag       the Tag Object with the parameters.
     * @param width     the width that this ToggleButton will bind to.
     */
    public TagButton(Tag tag, ReadOnlyDoubleProperty width) {
        setTagInfo(tag);
        this.prefWidthProperty().bind(width.subtract(WIDTH_OF_SCROLL_BAR));
        
        this.setMaxWidth(Double.MAX_VALUE);
        this.setAlignment(Pos.CENTER_LEFT);
    }
    
    /**
     * set tag parameters such as tag name and isSelected.
     * 
     * @param tag     the Tag Object with the parameters.
     */
    public void setTagInfo(Tag tag) {
        assert(tag.getName() != null);
        
        this.setText(tag.getName());
        this.setSelected(tag.getIsSelected());
    }
}
