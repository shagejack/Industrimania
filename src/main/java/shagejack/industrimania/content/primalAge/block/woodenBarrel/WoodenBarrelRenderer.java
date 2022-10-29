package shagejack.industrimania.content.primalAge.block.woodenBarrel;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import shagejack.industrimania.foundation.render.FluidRenderer;
import shagejack.industrimania.foundation.tileEntity.renderer.SafeTileEntityRenderer;

public class WoodenBarrelRenderer extends SafeTileEntityRenderer<WoodenBarrelTileEntity> {

    public WoodenBarrelRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(WoodenBarrelTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!te.getBlockState().isAir())
            renderFluid(te, partialTicks, ms, buffer, light, overlay);
    }

    protected void renderFluid(WoodenBarrelTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (te == null)
            return;

        if (te.tank.isEmpty() && te.tank.getFluidAmount() <= 0)
            return;

        float xMin, xMax, yMin, yMax, zMin, zMax;

        xMin = 0.125F;
        xMax = 0.875F;
        zMin = 0.125F;
        zMax = 0.875F;
        yMin = 0.0625F;
        yMax = 0.875F;

        ms.pushPose();


        ms.translate(0.0d, 0.0d, 0.0d);

        // render top only
        FluidRenderer.renderFluidStack(te.tank.getFluid(), te, ms, buffer, xMin, xMax, yMin, yMax, zMin, zMax);

        ms.popPose();
    }

}
