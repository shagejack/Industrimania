package shagejack.minecraftology.gui.inventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class guiContainerDemo extends GuiContainer {
    public guiContainerDemo(ContainerDemo inventorySlotsIn) {
        super(inventorySlotsIn);
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        // TODO
    }
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        // TODO
    }
}
