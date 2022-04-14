package shagejack.industrimania.content.primalAge.block.woodenBarrel;

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

public class WoodenBarrelBlockItemRenderer extends BlockEntityWithoutLevelRenderer {

    public WoodenBarrelBlockItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {

        if (itemStack.getOrCreateTag().contains("Open", Tag.TAG_BYTE)) {

            ModelResourceLocation location;
            BakedModel model;

            if (itemStack.getOrCreateTag().getBoolean("Open")) {
                location = new ModelResourceLocation(Industrimania.MOD_ID, "block/mechanic/wooden_barrel", "inventory");
            } else {
                location = new ModelResourceLocation(Industrimania.MOD_ID, "block/mechanic/wooden_barrel_0", "inventory");
            }

            model = Minecraft.getInstance().getModelManager().getModel(location);

            poseStack.pushPose();

            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            VertexConsumer buffer = source.getBuffer(RenderType.translucentNoCrumbling());

            renderer.render(itemStack, transformType, false, poseStack, source, light, overlay, model);

            poseStack.popPose();
        }

    }


}
