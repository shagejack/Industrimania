package shagejack.industrimania.content.primalAge.block.mixingBasin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import shagejack.industrimania.content.primalAge.block.dryingRack.DryingRackTileEntity;
import shagejack.industrimania.foundation.tileEntity.renderer.SafeTileEntityRenderer;

public class MixingBasinRenderer extends SafeTileEntityRenderer<MixingBasinTileEntity> {

    public MixingBasinRenderer(BlockEntityRendererProvider.Context context) {}


    @Override
    protected void renderSafe(MixingBasinTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!te.getBlockState().isAir())
            render(te, partialTicks, ms, buffer, light, overlay);
    }

    protected void render(DryingRackTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

    }
}
