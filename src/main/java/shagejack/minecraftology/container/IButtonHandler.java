package shagejack.minecraftology.container;

import shagejack.minecraftology.gui.element.MCLElementBase;

public interface IButtonHandler {
    void handleElementButtonClick(MCLElementBase element, String elementName, int mouseButton);
}

