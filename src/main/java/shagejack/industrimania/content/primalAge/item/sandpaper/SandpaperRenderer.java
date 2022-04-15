package shagejack.industrimania.content.primalAge.item.sandpaper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import shagejack.industrimania.Industrimania;

public class SandpaperRenderer extends BlockEntityWithoutLevelRenderer {

    public SandpaperRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {

        if (itemStack.getOrCreateTag().contains("ItemStack", Tag.TAG_COMPOUND) && !ItemStack.of(itemStack.getOrCreateTag().getCompound("ItemStack")).isEmpty()) {

            ItemStack containStack = ItemStack.of(itemStack.getOrCreateTag().getCompound("ItemStack"));

            poseStack.pushPose();

            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

            poseStack.translate(0.0d, 0.0d, 0.0d);

            renderer.renderStatic(containStack, transformType, 240, overlay, poseStack, source, 0);

            poseStack.popPose();
        }

    }


}
