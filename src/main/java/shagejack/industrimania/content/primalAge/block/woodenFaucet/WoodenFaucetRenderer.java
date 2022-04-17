package shagejack.industrimania.content.primalAge.block.woodenFaucet;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import shagejack.industrimania.client.renderer.FluidRenderer;
import shagejack.industrimania.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import shagejack.industrimania.foundation.utility.AngleUtils;

import java.util.Optional;

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

        if (te.getBlockState().getValue(WoodenFaucetBlock.FACING) == Direction.DOWN) {
            float xMin, xMax, yMin, yMax, zMin, zMax;

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
        } else if (te.getBlockState().getValue(WoodenFaucetBlock.FACING) != Direction.UP) {
            float xMin, xMax, yMin, yMax, zMin, zMax,
                    xMin1, xMax1, yMin1, yMax1, zMin1, zMax1;

            //xMin = 0.375F;
            //xMax = 0.625F;
            //zMin = 0.375F;
            //zMax = 0.625F;
            //yMin = -0.8F;
            //yMax = 0.5F;

            xMin1 = 0.3125F - 0.5F;
            xMax1 = 0.6725F - 0.5F;
            zMin1 = 0.4225F - 0.5F;
            zMax1 = 0.6725F - 0.5F;
            yMin1 = -0.8F - 0.5F;
            yMax1 = 0.0F;

            ms.pushPose();

            Direction direction = te.getBlockState().getValue(WoodenFaucetBlock.FACING);

            ms.translate(0.5d, 0.5d, 0.5d);
            ms.mulPose(Vector3f.YP.rotationDegrees(AngleUtils.horizontalAngle(direction.getOpposite())));

            //FluidRenderer.renderFluidStack(te.tank.getFluid(), te, ms, buffer, xMin, xMax, yMin, yMax, zMin, zMax, true, false);

            FluidRenderer.renderFluidStack(te.tank.getFluid(), te, ms, buffer, xMin1, xMax1, yMin1, yMax1, zMin1, zMax1, true, false);

            ms.popPose();
        }
    }

}
