package shagejack.shagecraft.gui.element;

import net.minecraft.client.Minecraft;
import shagejack.shagecraft.Reference;
import shagejack.shagecraft.container.IButtonHandler;
import shagejack.shagecraft.gui.ShageGuiBase;
import shagejack.shagecraft.util.ShageStringHelper;

public class CloseButton extends ShageElementButton {

    public CloseButton(ShageGuiBase gui, IButtonHandler handler, int posX, int posY, String name) {
        super(gui, handler, posX, posY, name, 0, 0, 9, 0, 9, 9, Reference.PATH_ELEMENTS + "close_button.png");
        this.setTexture(Reference.PATH_ELEMENTS + "close_button.png", 18, 9);
        this.setToolTip(ShageStringHelper.translateToLocal("gui.tooltip.close"));
    }

    @Override
    public void onAction(int mouseX, int mouseY, int mouseButton) {
        Minecraft.getMinecraft().player.closeScreen();
        super.onAction(mouseX, mouseY, mouseButton);
    }

}
