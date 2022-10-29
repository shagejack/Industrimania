package shagejack.industrimania.content.misc.itemBase;

import net.minecraft.world.item.Item;
import shagejack.industrimania.content.misc.materialBase.SawMaterial;

import java.util.Map;

public class IMSawItemBase extends Item implements Saw {

    protected final SawMaterial material;

    public IMSawItemBase(Properties properties, Map<String, Object> extraParam) {
        super(properties);
        this.material = (SawMaterial) extraParam.get("SawMaterial");
    }

    @Override
    public SawMaterial getMaterial() {
        return this.material;
    }

    @Override
    public float getSharpness() {
        return this.material.getSharpness();
    }

}
