package shagejack.industrimania.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import shagejack.industrimania.foundation.fluid.ITankTileEntity;
import shagejack.industrimania.foundation.utility.ColorUtils;

public class FluidRenderer {

    private static void add(VertexConsumer builder, PoseStack stack, float x, float y, float z, float u, float v, float r, float g, float b, float a) {
        builder.vertex(stack.last().pose(), x, y, z)
                .color(r, g, b, a)
                .uv(u, v)
                .uv2(LightTexture.FULL_BRIGHT)
                .normal(1, 0, 0)
                .endVertex();
    }

    private static void add(VertexConsumer builder, PoseStack stack, float x, float y, float z, float u, float v) {
        builder.vertex(stack.last().pose(), x, y, z)
                .color(1.0F, 1.0F, 1.0F, 1.0F)
                .uv(u, v)
                .uv2(LightTexture.FULL_BRIGHT)
                .normal(1, 0, 0)
                .endVertex();
    }

    public static ResourceLocation getFluidTexture(FluidStack stack) {
        return stack.getFluid().getAttributes().getStillTexture();
    }

    public static int getFluidColor(FluidStack stack) {
        final var fluid = stack.getFluid();
        final var attributes = fluid.getAttributes();
        final var normal = attributes.getColor();
        if (attributes.getClass() == FluidAttributes.class) {
            return normal;
        } else {
            return attributes.getColor(stack);
        }
    }

    public static int getFluidColor(Level level, BlockPos pos, FluidStack stack) {
        final var fluid = stack.getFluid();
        final var attributes = fluid.getAttributes();
        final var normal = attributes.getColor();
        if (attributes.getClass() == FluidAttributes.class) {
            return normal;
        } else {
            final var stackColor = attributes.getColor(stack);
            if (normal == stackColor) {
                //color in world
                return attributes.getColor(level, pos);
            } else {
                return stackColor;
            }
        }
    }

    public static void renderFluidStack(FluidStack stack, BlockEntity te, PoseStack ms, MultiBufferSource buffer, float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
        renderFluidStack(stack, te, ms, buffer, xMin, xMax, yMin, yMax, zMin, zMax, false, false);
    }

    public static void renderFluidStack(FluidStack stack, BlockEntity te, PoseStack ms, MultiBufferSource buffer, float xMin, float xMax, float yMin, float yMax, float zMin, float zMax, boolean topOnly) {
        renderFluidStack(stack, te, ms, buffer, xMin, xMax, yMin, yMax, zMin, zMax, false, topOnly);
    }

    public static void renderFluidStack(FluidStack stack, BlockEntity te, PoseStack ms, MultiBufferSource buffer, float xMin, float xMax, float zMin, float zMax, float y) {
        renderFluidStack(stack, te, ms, buffer, xMin, xMax, 0, y, zMin, zMax, true, true);
    }

    public static void renderFluidStack(FluidStack stack, Level level, BlockPos pos, PoseStack ms, MultiBufferSource buffer, float xMin, float xMax, float zMin, float zMax, float y) {
        renderFluidStack(stack, level, pos, ms, buffer, xMin, xMax, 0, y, zMin, zMax, true);
    }

    public static void renderFluidStack(FluidStack stack, Level level, BlockPos pos, PoseStack ms, MultiBufferSource buffer, int capacity, float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
        renderFluidStack(stack, level, pos, ms, buffer, capacity, xMin, xMax, yMin, yMax, zMin, zMax, true);
    }

    public static void renderFluidStack(FluidStack stack, BlockEntity te, PoseStack ms, MultiBufferSource buffer, float xMin, float xMax, float yMin, float yMax, float zMin, float zMax, boolean ignoreAmount, boolean topOnly) {
        Minecraft.getInstance().getProfiler().push("RenderFluidStack");

        if (stack.isEmpty())
            return;

        if (te instanceof ITankTileEntity || ignoreAmount) {
            ms.pushPose();
            var builder = buffer.getBuffer(RenderType.translucent());
            var resource = getFluidTexture(stack);
            var texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(resource);
            var color = getFluidColor(te.getLevel(), te.getBlockPos(), stack);
            float r = ColorUtils.getRed(color);
            float g = ColorUtils.getGreen(color);
            float b = ColorUtils.getBlue(color);
            float a = ColorUtils.getAlpha(color);

            float scale = ignoreAmount ? 1 : (float) stack.getAmount() / ((ITankTileEntity) te).getCapacity();

            if (!topOnly) {
                renderCube(builder, ms, texture, xMin, xMax, yMin, yMin + (yMax - yMin) * scale, zMin, zMax, r, g, b, a);
            } else {
                renderCuboid(builder, ms, texture, xMin, xMax, zMin, zMax, yMin + (yMax - yMin) * scale, r, g, b, a);
            }

            ms.popPose();
        }

        Minecraft.getInstance().getProfiler().pop();
    }

    public static void renderFluidStack(FluidStack stack, Level level, BlockPos pos, PoseStack ms, MultiBufferSource buffer, float xMin, float xMax, float yMin, float yMax, float zMin, float zMax, boolean topOnly) {
        Minecraft.getInstance().getProfiler().push("RenderFluidStack");

        if (stack.isEmpty())
            return;

        ms.pushPose();
        var builder = buffer.getBuffer(RenderType.translucent());
        var resource = getFluidTexture(stack);
        var texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(resource);
        var color = getFluidColor(level, pos, stack);
        float r = ColorUtils.getRed(color);
        float g = ColorUtils.getGreen(color);
        float b = ColorUtils.getBlue(color);
        float a = ColorUtils.getAlpha(color);

        if (!topOnly) {
            renderCube(builder, ms, texture, xMin, xMax, yMin, yMax, zMin, zMax, r, g, b, a);
        } else {
            renderCuboid(builder, ms, texture, xMin, xMax, zMin, zMax, yMax, r, g, b, a);
        }

        ms.popPose();
        Minecraft.getInstance().getProfiler().pop();
    }

    public static void renderFluidStack(FluidStack stack, Level level, BlockPos pos, PoseStack ms, MultiBufferSource buffer, int capacity, float xMin, float xMax, float yMin, float yMax, float zMin, float zMax, boolean topOnly) {
        Minecraft.getInstance().getProfiler().push("RenderFluidStack");

        if (stack.isEmpty())
            return;

        ms.pushPose();
        var builder = buffer.getBuffer(RenderType.translucent());
        var resource = getFluidTexture(stack);
        var texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(resource);
        var color = getFluidColor(level, pos, stack);
        float r = ColorUtils.getRed(color);
        float g = ColorUtils.getGreen(color);
        float b = ColorUtils.getBlue(color);
        float a = ColorUtils.getAlpha(color);

        float scale = (float) stack.getAmount() / capacity;

        if (!topOnly) {
            renderCube(builder, ms, texture, xMin, xMax, yMin, yMin + (yMax - yMin) * scale, zMin, zMax, r, g, b, a);
        } else {
            renderCuboid(builder, ms, texture, xMin, xMax, zMin, zMax, yMin + (yMax - yMin) * scale, r, g, b, a);
        }

        ms.popPose();
        Minecraft.getInstance().getProfiler().pop();
    }

    public static void renderCube(VertexConsumer builder, PoseStack ms, TextureAtlasSprite texture, float xMin, float xMax, float yMin, float yMax, float zMin, float zMax, float r, float g, float b, float a) {

        ms.pushPose();

        float u0 = texture.getU0();
        float v0 = texture.getV0();
        float u1 = texture.getU1();
        float v1 = texture.getV1();

        // render top face
        add(builder, ms, xMin, yMax, zMax, u0, v1, r, g, b, a);
        add(builder, ms, xMax, yMax, zMax, u1, v1, r, g, b, a);
        add(builder, ms, xMax, yMax, zMin, u1, v0, r, g, b, a);
        add(builder, ms, xMin, yMax, zMin, u0, v0, r, g, b, a);

        // render south face
        add(builder, ms, xMax, yMax, zMax, u0, v0, r, g, b, a);
        add(builder, ms, xMin, yMax, zMax, u1, v0, r, g, b, a);
        add(builder, ms, xMin, yMin, zMax, u1, v1, r, g, b, a);
        add(builder, ms, xMax, yMin, zMax, u0, v1, r, g, b, a);

        // render east face
        add(builder, ms, xMax, yMin, zMin, u0, v0, r, g, b, a);
        add(builder, ms, xMax, yMax, zMin, u1, v0, r, g, b, a);
        add(builder, ms, xMax, yMax, zMax, u1, v1, r, g, b, a);
        add(builder, ms, xMax, yMin, zMax, u0, v1, r, g, b, a);

        // render bottom face
        add(builder, ms, xMin, yMin, zMin, u1, v1, r, g, b, a);
        add(builder, ms, xMax, yMin, zMin, u0, v1, r, g, b, a);
        add(builder, ms, xMax, yMin, zMax, u0, v0, r, g, b, a);
        add(builder, ms, xMin, yMin, zMax, u1, v0, r, g, b, a);

        // render north face
        add(builder, ms, xMin, yMax, zMin, u0, v1, r, g, b, a);
        add(builder, ms, xMax, yMax, zMin, u1, v1, r, g, b, a);
        add(builder, ms, xMax, yMin, zMin, u1, v0, r, g, b, a);
        add(builder, ms, xMin, yMin, zMin, u0, v0, r, g, b, a);

        // render west face
        add(builder, ms, xMin, yMax, zMin, u0, v1, r, g, b, a);
        add(builder, ms, xMin, yMin, zMin, u1, v1, r, g, b, a);
        add(builder, ms, xMin, yMin, zMax, u1, v0, r, g, b, a);
        add(builder, ms, xMin, yMax, zMax, u0, v0, r, g, b, a);

        ms.popPose();

    }

    public static void renderCube(VertexConsumer builder, PoseStack ms, TextureAtlasSprite texture, float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
        renderCube(builder, ms, texture, xMin, xMax, yMin, yMax, zMin, zMax, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void renderCuboid(VertexConsumer builder, PoseStack ms, TextureAtlasSprite texture, float xMin, float xMax, float zMin, float zMax, float y, float r, float g, float b, float a) {

        ms.pushPose();

        float u0 = texture.getU0();
        float v0 = texture.getV0();
        float u1 = texture.getU1();
        float v1 = texture.getV1();

        add(builder, ms, xMin, y, zMax, u0, v1, r, g, b, a);
        add(builder, ms, xMax, y, zMax, u1, v1, r, g, b, a);
        add(builder, ms, xMax, y, zMin, u1, v0, r, g, b, a);
        add(builder, ms, xMin, y, zMin, u0, v0, r, g, b, a);

        ms.popPose();

    }

    public static void renderCuboid(VertexConsumer builder, PoseStack ms, TextureAtlasSprite texture, float xMin, float xMax, float zMin, float zMax, float y) {
        renderCuboid(builder, ms, texture, xMin, xMax, zMin, zMax, y, 1.0F, 1.0F, 1.0F, 1.0F);
    }

}
