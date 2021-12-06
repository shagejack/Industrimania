package shagejack.shagecraft.container;

import shagejack.shagecraft.gui.element.ShageElementBase;

public interface IButtonHandler {
    void handleElementButtonClick(ShageElementBase element, String elementName, int mouseButton);
}

