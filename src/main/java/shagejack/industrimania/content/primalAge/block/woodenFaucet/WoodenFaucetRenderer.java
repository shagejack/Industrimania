package shagejack.industrimania.content.primalAge.block.woodenFaucet;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import shagejack.industrimania.client.renderer.FluidRenderer;
import shagejack.industrimania.foundation.tileEntity.renderer.SafeTileEntityRenderer;

public class WoodenFaucetRenderer extends SafeTileEntityRenderer<WoodenFaucetTileEntity> {

    public WoodenFaucetRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(WoodenFaucetTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!te.getBlockState().isAir())
            renderLiquid(te, partialTicks, ms, buffer, light, overlay);
    }

    protected void renderLiquid(WoodenFaucetTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (te == null)
            return;

        if (!te.shouldRender())
            return;

        float xMin, xMax, yMin, yMax, zMin, zMax;

        if (te.getBlockState().getValue(WoodenFaucetBlock.FACING) == Direction.DOWN) {

            xMin = 0.375F;
            xMax = 0.625F;
            zMin = 0.375F;
            zMax = 0.625F;
            yMin = -0.8F;
            yMax = 0.9F;

            ms.pushPose();
            ms.translate(0.0d, 0.0d, 0.0d);

            // render top only
            FluidRenderer.renderFluidStack(te.tank.getFluid(), te, ms, buffer, xMin, xMax, yMin, yMax, zMin, zMax, true, false);

            ms.popPose();
        }
    }

}
