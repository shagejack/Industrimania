package shagejack.minecraftology.gui.element;

import net.minecraft.client.renderer.GlStateManager;
import shagejack.minecraftology.client.data.Color;
import shagejack.minecraftology.client.render.HoloIcon;
import shagejack.minecraftology.container.IButtonHandler;
import shagejack.minecraftology.gui.MCLGuiBase;
import shagejack.minecraftology.proxy.ClientProxy;
import shagejack.minecraftology.util.RenderUtils;

public class MCLElementIconButton extends MCLElementButton {
    HoloIcon icon;
    Color iconColor;

    public MCLElementIconButton(MCLGuiBase gui, IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int disabledX, int disabledY, int sizeX, int sizeY, String texture, HoloIcon icon) {
        super(gui, handler, posX, posY, name, sheetX, sheetY, hoverX, hoverY, disabledX, disabledY, sizeX, sizeY, texture);
        this.icon = icon;
    }

    public MCLElementIconButton(MCLGuiBase gui, IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int sizeX, int sizeY, String texture, HoloIcon icon) {
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