package shagejack.shagecraft.client.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import shagejack.shagecraft.tile.ShageTileEntity;

public class ShageTileEntityRendererBase<T extends ShageTileEntity> extends TileEntitySpecialRenderer<T> {

    protected void renderStillItem(ShageTileEntity te, ItemStack stack, float x, float y, float z, float scale) {
        renderItem(te, stack, x, y, z, scale, false);
    }

    protected void renderItem(ShageTileEntity te, ItemStack stack, float x, float y, float z, float scale) {
        renderItem(te, stack, x, y, z, scale, true);
    }

    private void renderItem(ShageTileEntity te, ItemStack stack, float x, float y, float z, float scale, boolean rotate) {
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        if (stack != null) {
            GlStateManager.translate(x, y, z);
            EntityItem entityitem = new EntityItem(te.getWorld(), 0.0D, 0.0D, 0.0D, stack.copy());
            entityitem.hoverStart = 0.0F;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

            float rotation = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

            if (rotate)
                GlStateManager.rotate(rotation, 0.0F, 1.0F, 0);
            GlStateManager.scale(0.5F * scale, 0.5F * scale, 0.5F * scale);
            GlStateManager.pushAttrib();
            RenderHelper.enableStandardItemLighting();
            itemRenderer.renderItem(entityitem.getItem(), ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

}
