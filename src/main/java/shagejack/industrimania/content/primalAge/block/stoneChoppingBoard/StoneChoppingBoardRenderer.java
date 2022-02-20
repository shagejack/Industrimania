package shagejack.industrimania.content.primalAge.block.stoneChoppingBoard;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import shagejack.industrimania.foundation.tileEntity.renderer.SafeTileEntityRenderer;

public class StoneChoppingBoardRenderer extends SafeTileEntityRenderer<StoneChoppingBoardTileEntity> {

    public StoneChoppingBoardRenderer(BlockEntityRendererProvider.Context context) {}

    protected void renderSafe(StoneChoppingBoardTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,
                              int overlay) {
        if (!te.getBlockState().isAir())
            renderItems(te, partialTicks, ms, buffer, light, overlay);
    }

    protected void renderItems(StoneChoppingBoardTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!te.inventory.isEmpty()) {

            ItemStack stack = te.inventory.getStackInSlot(0);
            if (!stack.isEmpty()) {

                ms.pushPose();

                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                BakedModel modelWithOverrides = itemRenderer.getModel(stack, te.getLevel(), null, 0);
                boolean blockItem = modelWithOverrides.isGui3d();

                ms.translate(.5, blockItem ? .425f : 5f / 16f, .5);

                ms.scale(.5f, .5f, .5f);
                ms.mulPose(Vector3f.XP.rotationDegrees(90));
                itemRenderer.renderStatic(stack, ItemTransforms.TransformType.FIXED, light, overlay, ms, buffer, 0);

                ms.popPose();

            }
        }
    }

}
