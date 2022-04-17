package shagejack.industrimania.content.primalAge.item.sandpaper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import shagejack.industrimania.foundation.item.renderer.CustomRenderedItemModelRenderer;
import shagejack.industrimania.foundation.item.renderer.IMCustomRenderedItemModel;
import shagejack.industrimania.foundation.item.renderer.PartialItemModelRenderer;
import shagejack.industrimania.foundation.utility.AnimationTickHolder;

public class SandpaperRenderer extends CustomRenderedItemModelRenderer<SandpaperRenderer.SandpaperModel> {

    @Override
    protected void render(ItemStack stack, SandpaperModel model, PartialItemModelRenderer renderer,
                          ItemTransforms.TransformType transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        LocalPlayer player = Minecraft.getInstance().player;
        float partialTicks = AnimationTickHolder.getPartialTicks();

        boolean leftHand = transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
        boolean firstPerson = leftHand || transformType == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND;

        CompoundTag tag = stack.getOrCreateTag();
        boolean jeiMode = tag.contains("JEI");

        ms.pushPose();

        if (Sandpaper.containItem(stack)) {
            ms.pushPose();

            if (transformType == ItemTransforms.TransformType.GUI) {
                ms.translate(0.0F, .2f, 1.0F);
                ms.scale(.75f, .75f, .75f);
            } else {
                int modifier = leftHand ? -1 : 1;
                ms.mulPose(Vector3f.YP.rotationDegrees(modifier * 40));
            }

            assert player != null;

            // Reverse bobbing
            float time = (float) (!jeiMode ? player.getUseItemRemainingTicks() : (-AnimationTickHolder.getTicks()) % stack.getUseDuration()) + 1.0F;
            if (time / (float) stack.getUseDuration() < 0.8F) {
                float bobbing = -Mth.abs(Mth.cos(time / 4.0F * (float) Math.PI) * 0.1F);

                if (transformType == ItemTransforms.TransformType.GUI)
                    ms.translate(bobbing, bobbing, 0.0F);
                else
                    ms.translate(0.0f, bobbing, 0.0F);
            }

            ItemStack toPolish = Sandpaper.getStackContained(stack);
            itemRenderer.renderStatic(toPolish, ItemTransforms.TransformType.NONE, light, overlay, ms, buffer, 0);

            ms.popPose();
        }

        if (firstPerson) {
            int itemInUseCount = player.getUseItemRemainingTicks();
            if (itemInUseCount > 0) {
                int modifier = leftHand ? -1 : 1;
                ms.translate(modifier * .5f, 0, -.25f);
                ms.mulPose(Vector3f.ZP.rotationDegrees(modifier * 40));
                ms.mulPose(Vector3f.XP.rotationDegrees(modifier * 10));
                ms.mulPose(Vector3f.YP.rotationDegrees(modifier * 90));
            }
        }

        itemRenderer.render(stack, ItemTransforms.TransformType.NONE, false, ms, buffer, light, overlay, model.getOriginalModel());

        ms.popPose();
    }

    @Override
    public SandpaperModel createModel(BakedModel originalModel) {
        return new SandpaperModel(originalModel);
    }

    public static class SandpaperModel extends IMCustomRenderedItemModel {

        public SandpaperModel(BakedModel template) {
            super(template, "");
        }

    }
}
