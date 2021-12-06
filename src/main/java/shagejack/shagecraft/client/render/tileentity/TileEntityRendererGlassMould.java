package shagejack.shagecraft.client.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import shagejack.shagecraft.tile.TileEntityGlassMould;

public class TileEntityRendererGlassMould extends ShageTileEntityRendererBase<TileEntityGlassMould> {
    private EntityItem item;

    @Override
    public void render(TileEntityGlassMould te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(te == null || !te.hasWorld())
            return;

        float fluidLevel = te.tank.getFluidAmount();
        float height = 0.0625F;
        if (fluidLevel > 0) {
            FluidStack fluidStack = new FluidStack(te.tank.getFluid(), 100);
            height = (0.5F / te.tank.getCapacity()) * te.tank.getFluidAmount();

            TextureAtlasSprite fluidStillSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluidStack.getFluid().getStill().toString());
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            int fluidColor = fluidStack.getFluid().getColor(fluidStack);

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y + 0.25F, z);
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            setGLColorFromInt(fluidColor);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            float xMax, zMax, xMin, zMin, yMin = 0;

                xMax = 1.75F;
                zMax = 1.75F;
                xMin = 0.25F;
                zMin = 0.25F;
                yMin = 0F;

            renderCuboid(buffer, xMax, xMin, yMin, height, zMin, zMax, fluidStillSprite);
            tessellator.draw();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        if (!te.getStackInSlot(0).isEmpty()) {
            renderStillItem(te, te.getStackInSlot(0), 0.5F, 0.5F, 0.5F, 2.0F);
        }
        GlStateManager.popMatrix();

        super.render(te, x, y + 1, z, partialTicks, destroyStage, alpha);



    }

    private void setGLColorFromInt(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        GlStateManager.color(red, green, blue, 1.0F);
    }

    private void renderCuboid(BufferBuilder buffer, float xMax, float xMin, float yMin, float height, float zMin, float zMax, TextureAtlasSprite textureAtlasSprite) {

        double uMin = (double) textureAtlasSprite.getMinU();
        double uMax = (double) textureAtlasSprite.getMaxU();
        double vMin = (double) textureAtlasSprite.getMinV();
        double vMax = (double) textureAtlasSprite.getMaxV();

        final double vHeight = vMax - vMin;

        // top only needed ;)
        addVertexWithUV(buffer, xMax, height, zMax, uMax, vMin);
        addVertexWithUV(buffer, xMax, height, zMin, uMin, vMin);
        addVertexWithUV(buffer, xMin, height, zMin, uMin, vMax);
        addVertexWithUV(buffer, xMin, height, zMax, uMax, vMax);

    }

    private void addVertexWithUV(BufferBuilder buffer, float x, float y, float z, double u, double v) {
        buffer.pos(x / 2f, y, z / 2f).tex(u, v).endVertex();
    }

}
