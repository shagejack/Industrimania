package shagejack.industrimania.content.misc.itemBase;

import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

import java.util.Map;

public class IMPickaxeItemBase extends PickaxeItem {

    public IMPickaxeItemBase(Properties properties, Map<String, Object> extraParam) {
        super((Tier) extraParam.get("Tier"), (int) extraParam.get("AttackDamageBaseline"), (float) extraParam.get("AttackSpeed"), properties);
    }

}
