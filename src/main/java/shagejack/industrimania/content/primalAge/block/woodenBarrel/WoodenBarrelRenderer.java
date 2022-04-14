package shagejack.industrimania.content.primalAge.block.woodenBarrel;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.fluids.FluidStack;
import shagejack.industrimania.client.renderer.FluidRenderer;
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

        xMin = 0.25F;
        xMax = 0.75F;
        zMin = 0.25F;
        zMax = 0.75F;
        yMin = 0.2F;
        yMax = 0.8F;

        ms.pushPose();


        ms.translate(0.0d, 0.0d, 0.0d);

        // render top only
        FluidRenderer.renderFluidStack(te.tank.getFluid(), te, ms, buffer, xMin, xMax, yMin, yMax, zMin, zMax, false);

        ms.popPose();
    }

}
