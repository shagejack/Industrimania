package shagejack.minecraftology.client.render.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.opengl.GL11;
import shagejack.minecraftology.tile.MCLTileEntity;

public class MCLTileEntityRendererBase<T extends MCLTileEntity> extends TileEntitySpecialRenderer<T> {

    protected void renderStillItem(MCLTileEntity te, ItemStack stack, float x, float y, float z, float scale) {
        renderItem(te, stack, x, y, z, scale, false);
    }

    protected void renderItem(MCLTileEntity te, ItemStack stack, float x, float y, float z, float scale) {
        renderItem(te, stack, x, y, z, scale, true);
    }

    private void renderItem(MCLTileEntity te, ItemStack stack, float x, float y, float z, float scale, boolean rotate) {
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
