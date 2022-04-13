package shagejack.industrimania.content.primalAge.block.woodenBarrel;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import shagejack.industrimania.foundation.tileEntity.renderer.SafeTileEntityRenderer;

public class WoodenBarrelRenderer extends SafeTileEntityRenderer<WoodenBarrelTileEntity> {

    public WoodenBarrelRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(WoodenBarrelTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!te.getBlockState().isAir())
            renderLiquid(te, partialTicks, ms, buffer, light, overlay);
    }

    protected void renderLiquid(WoodenBarrelTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

    }

}
