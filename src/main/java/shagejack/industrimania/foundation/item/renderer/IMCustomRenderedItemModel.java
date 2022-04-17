package shagejack.industrimania.foundation.item.renderer;

import net.minecraft.client.resources.model.BakedModel;
import shagejack.industrimania.Industrimania;

public abstract class IMCustomRenderedItemModel extends CustomRenderedItemModel {

    public IMCustomRenderedItemModel(BakedModel template, String basePath) {
        super(template, Industrimania.MOD_ID, basePath);
    }

}
