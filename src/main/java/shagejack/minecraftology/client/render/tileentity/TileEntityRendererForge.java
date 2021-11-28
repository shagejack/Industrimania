package shagejack.minecraftology.client.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import shagejack.minecraftology.tile.TileEntityForge;

public class TileEntityRendererForge extends TileEntitySpecialRenderer<TileEntityForge> {
    private EntityItem item;

    @Override
    public void render(TileEntityForge tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!tileEntity.shouldRender())
            return;

        if (item == null) {
            item = new EntityItem(tileEntity.getWorld());
        }

        ItemStack newStack = tileEntity.getStackInSlot(0);

        if (!newStack.isEmpty()) {
            item.setItem(newStack);
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.23, 0.69, 0);
            GlStateManager.rotate(90, 0, 1, 0);
            GlStateManager.rotate(90, 1, 0, 0);
            item.hoverStart = 0f;
            Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
            GlStateManager.popMatrix();
        }
    }
}
