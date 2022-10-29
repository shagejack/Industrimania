package shagejack.industrimania.content.misc.itemBase;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Tier;

import java.util.Map;

public class IMAxeItemBase extends AxeItem {

    public IMAxeItemBase(Properties properties, Map<String, Object> extraParam) {
        super((Tier) extraParam.get("Tier"), (float) extraParam.get("AttackDamageBaseline"), (float) extraParam.get("AttackSpeed"), properties);
    }

}
