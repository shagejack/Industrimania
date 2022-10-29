package shagejack.industrimania.content.misc.itemBase;

import net.minecraft.world.item.Item;
import shagejack.industrimania.content.misc.materialBase.KnifeMaterial;

import java.util.Map;

public class IMKnifeItemBase extends Item implements Knife {

    protected final KnifeMaterial material;

    public IMKnifeItemBase(Properties properties, Map<String, Object> extraParam) {
        super(properties);
        this.material = (KnifeMaterial) extraParam.get("KnifeMaterial");
    }

    @Override
    public KnifeMaterial getMaterial() {
        return this.material;
    }

    @Override
    public float getSharpness() {
        return this.material.getSharpness();
    }

}
