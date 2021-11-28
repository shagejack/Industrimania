package shagejack.minecraftology.gui.element;

import net.minecraft.client.Minecraft;
import shagejack.minecraftology.Reference;
import shagejack.minecraftology.container.IButtonHandler;
import shagejack.minecraftology.gui.MCLGuiBase;
import shagejack.minecraftology.util.MCLStringHelper;

public class CloseButton extends MCLElementButton {

    public CloseButton(MCLGuiBase gui, IButtonHandler handler, int posX, int posY, String name) {
        super(gui, handler, posX, posY, name, 0, 0, 9, 0, 9, 9, Reference.PATH_ELEMENTS + "close_button.png");
        this.setTexture(Reference.PATH_ELEMENTS + "close_button.png", 18, 9);
        this.setToolTip(MCLStringHelper.translateToLocal("gui.tooltip.close"));
    }

    @Override
    public void onAction(int mouseX, int mouseY, int mouseButton) {
        Minecraft.getMinecraft().player.closeScreen();
        super.onAction(mouseX, mouseY, mouseButton);
    }

}
