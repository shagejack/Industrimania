package shagejack.industrimania.content.primalAge.item.itemPlaceable.base;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import shagejack.industrimania.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import shagejack.industrimania.registries.tags.AllTags;
import shagejack.industrimania.registries.item.AllItems;

import java.util.HashMap;
import java.util.Map;

public class ItemPlaceableRenderer extends SafeTileEntityRenderer<ItemPlaceableBaseTileEntity> {

	public ItemPlaceableRenderer(BlockEntityRendererProvider.Context context) {}

	private static final Map<Item, ItemStack> renderItems = new HashMap<>();

	static {
		renderItems.put(AllItems.logAcacia.get(), new ItemStack(AllItems.modelLog.get()));
		renderItems.put(AllItems.logBirch.get(), new ItemStack(AllItems.modelLog.get()));
		renderItems.put(AllItems.logDarkOak.get(), new ItemStack(AllItems.modelLog.get()));
		renderItems.put(AllItems.logJungle.get(), new ItemStack(AllItems.modelLog.get()));
		renderItems.put(AllItems.logOak.get(), new ItemStack(AllItems.modelLog.get()));
		renderItems.put(AllItems.logSpruce.get(), new ItemStack(AllItems.modelLog.get()));
		renderItems.put(AllItems.logRubber.get(), new ItemStack(AllItems.modelLog.get()));
		renderItems.put(AllItems.logMulberry.get(), new ItemStack(AllItems.modelLog.get()));

		renderItems.put(AllItems.woodAcacia.get(), new ItemStack(AllItems.modelWood.get()));
		renderItems.put(AllItems.woodBirch.get(), new ItemStack(AllItems.modelWood.get()));
		renderItems.put(AllItems.woodDarkOak.get(), new ItemStack(AllItems.modelWood.get()));
		renderItems.put(AllItems.woodJungle.get(), new ItemStack(AllItems.modelWood.get()));
		renderItems.put(AllItems.woodOak.get(), new ItemStack(AllItems.modelWood.get()));
		renderItems.put(AllItems.woodSpruce.get(), new ItemStack(AllItems.modelWood.get()));
		renderItems.put(AllItems.woodMulberry.get(), new ItemStack(AllItems.modelWood.get()));
		renderItems.put(AllItems.woodRubber.get(), new ItemStack(AllItems.modelWood.get()));
	}

	@Override
	protected void renderSafe(ItemPlaceableBaseTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light,
		int overlay) {
		renderItems(te, partialTicks, ms, buffer, light, overlay);
	}

	protected void renderItems(ItemPlaceableBaseTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		if (!te.inventory.isEmpty()) {

			for (int i = 0; i < te.inventory.getSlots(); i++) {
				ItemStack stack = te.inventory.getStackInSlot(i);
				if (!stack.isEmpty()) {

					ms.pushPose();

					Vec2 renderPos = te.getItemRenderPos(i);
					ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

					ms.translate(renderPos.x, renderPos.y, 0.5f);

					ItemStack renderStack;

					if (renderItems.containsKey(stack.getItem())) {
						renderStack = renderItems.get(stack.getItem());
					} else if (stack.is(AllTags.modItemTag(AllTags.IndustrimaniaTags.rockPiece))) {
						renderStack = new ItemStack(AllItems.modelRockPiece.get());
					} else {
						renderStack = new ItemStack(AllItems.modelDust.get());
					}

					itemRenderer.renderStatic(renderStack, ItemTransforms.TransformType.FIXED, 240, overlay, ms, buffer, 0);

					ms.popPose();

				}
			}

			if (te.isBurning()) {

			}
		}
	}

}
