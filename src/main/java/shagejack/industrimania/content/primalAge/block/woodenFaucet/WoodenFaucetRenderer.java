package shagejack.industrimania.content.primalAge.block.woodenFaucet;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import shagejack.industrimania.foundation.tileEntity.renderer.SafeTileEntityRenderer;

public class WoodenFaucetRenderer extends SafeTileEntityRenderer<WoodenFaucetTileEntity> {

    public WoodenFaucetRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(WoodenFaucetTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!te.getBlockState().isAir())
            renderLiquid(te, partialTicks, ms, buffer, light, overlay);
    }

    protected void renderLiquid(WoodenFaucetTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

    }

}
