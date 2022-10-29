package shagejack.industrimania.content.misc.itemBase;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

import java.util.Map;

public class IMArmorItemBase extends ArmorItem {

    public IMArmorItemBase(Properties properties, Map<String, Object> extraParam) {
        super((ArmorMaterial) extraParam.get("ArmorMaterial"), (EquipmentSlot) extraParam.get("EquipmentSlot"), properties);
    }

}
