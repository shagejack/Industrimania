package shagejack.industrimania.foundation.item.renderer;

import net.minecraft.world.item.Item;
import net.minecraftforge.client.IItemRenderProperties;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.IndustrimaniaClient;

public class SimpleCustomRenderer implements IItemRenderProperties {

    protected CustomRenderedItemModelRenderer<?> renderer;

    protected SimpleCustomRenderer(CustomRenderedItemModelRenderer<?> renderer) {
        this.renderer = renderer;
    }

    public static SimpleCustomRenderer create(Item item, CustomRenderedItemModelRenderer<?> renderer) {
        IndustrimaniaClient.MODEL_SWAPPER.getCustomRenderedItems().register(item.delegate, renderer::createModel);
        return new SimpleCustomRenderer(renderer);
    }

    @Override
    public CustomRenderedItemModelRenderer<?> getItemStackRenderer() {
        return renderer;
    }

}
