package shagejack.shagecraft.gui.element;

import net.minecraft.client.renderer.GlStateManager;
import shagejack.shagecraft.client.data.Color;
import shagejack.shagecraft.client.render.HoloIcon;
import shagejack.shagecraft.container.IButtonHandler;
import shagejack.shagecraft.gui.ShageGuiBase;
import shagejack.shagecraft.proxy.ClientProxy;
import shagejack.shagecraft.util.RenderUtils;

public class ShageElementIconButton extends ShageElementButton {
    HoloIcon icon;
    Color iconColor;

    public ShageElementIconButton(ShageGuiBase gui, IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int disabledX, int disabledY, int sizeX, int sizeY, String texture, HoloIcon icon) {
        super(gui, handler, posX, posY, name, sheetX, sheetY, hoverX, hoverY, disabledX, disabledY, sizeX, sizeY, texture);
        this.icon = icon;
    }

    public ShageElementIconButton(ShageGuiBase gui, IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int sizeX, int sizeY, String texture, HoloIcon icon) {
        super(gui, handler, posX, posY, name, sheetX, sheetY, hoverX, hoverY, sizeX, sizeY, texture);
        this.icon = icon;
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        if (icon != null) {
            GlStateManager.enableAlpha();
            ClientProxy.holoIcons.bindSheet();
            if (iconColor != null) {
                RenderUtils.applyColorWithAlpha(iconColor);
            }
            ClientProxy.holoIcons.renderIcon(icon, posX - icon.getOriginalWidth() / 2 + sizeX / 2, posY - icon.getOriginalHeight() / 2 + sizeY / 2);
        }
    }

    public void setIconColor(Color iconColor) {
        this.iconColor = iconColor;
    }

    public void setIcon(HoloIcon icon) {
        this.icon = icon;
    }
}