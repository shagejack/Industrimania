package shagejack.industrimania.content.misc.itemBase;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Tier;
import shagejack.industrimania.content.misc.materialBase.MattockMaterial;

import java.util.Map;

public class IMMattockItemBase extends HoeItem {

    protected final MattockMaterial material;

    public IMMattockItemBase(Properties properties, Map<String, Object> extraParam) {
        super((Tier) extraParam.get("Tier"), (int) extraParam.get("AttackDamageBaseline"), (float) extraParam.get("AttackSpeed"), properties);
        this.material = (MattockMaterial) extraParam.get("MattockMaterial");
    }
}
