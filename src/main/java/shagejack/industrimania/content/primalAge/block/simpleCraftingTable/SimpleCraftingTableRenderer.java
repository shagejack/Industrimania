package shagejack.industrimania.content.primalAge.block.simpleCraftingTable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import shagejack.industrimania.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import shagejack.industrimania.foundation.utility.AngleHelper;

import java.util.Optional;

public class SimpleCraftingTableRenderer extends SafeTileEntityRenderer<SimpleCraftingTableTileEntity> {

    public SimpleCraftingTableRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    protected void renderSafe(SimpleCraftingTableTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,
                              int overlay) {
        if (!te.getBlockState().isAir())
            renderItems(te, partialTicks, ms, buffer, light, overlay);
    }

    protected void renderItems(SimpleCraftingTableTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!te.inventory.isEmpty()) {

            for (int i = 0; i < te.inventory.getSlots(); i++) {
                ItemStack stack = te.inventory.getStackInSlot(i);

                if (!stack.isEmpty()) {

                    Vec2 renderPos = te.getItemRenderPos(i);
                    float rotation = te.itemRotation[i];
                    int jump = te.itemJump[i];
                    int prevJump = te.itemJumpPrev[i];
                    float jumpUP = jump * 0.05F + (jump * 0.05F - prevJump * 0.05F) * partialTicks;

                    ms.pushPose();

                    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                    BakedModel modelWithOverrides = itemRenderer.getModel(stack, te.getLevel(), null, 0);
                    boolean blockItem = modelWithOverrides.isGui3d();

                    if (te.craftingTick == -1) {
                        ms.translate(renderPos.x, blockItem ? 1.05f + jumpUP : 1.0375f + jumpUP, renderPos.y);
                    } else {
                        float lambda = (float) (te.REQUIRED_CRAFTING_TICK - te.craftingTick) / (float) te.REQUIRED_CRAFTING_TICK;

                        lambda = (float) Math.pow(lambda, 2);

                        Vec2 centerPos = te.getItemRenderPos(4);
                        Vec2 nPos = renderPos.scale(1 - lambda).add(centerPos.scale(lambda));

                        ms.translate(nPos.x, blockItem ? 1.05f : 1.0375f, nPos.y);
                    }


                    ms.scale(.25f, .25f, .25f);

                    Optional<Direction> direction = Optional.ofNullable(te.getDirection());

                    if (direction.isPresent()) {

                        ms.mulPose(Vector3f.YP.rotationDegrees(AngleHelper.horizontalAngle(direction.get().getOpposite()) + rotation));

                        ms.mulPose(Vector3f.XP.rotationDegrees(90));

                        itemRenderer.renderStatic(stack, ItemTransforms.TransformType.FIXED, 240, overlay, ms, buffer, 0);

                    }

                    ms.popPose();
                }
            }
        }
    }

}
