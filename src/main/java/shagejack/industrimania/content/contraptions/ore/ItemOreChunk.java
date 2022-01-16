package shagejack.industrimania.content.contraptions.ore;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import shagejack.industrimania.content.worldGen.record.OreType;
import shagejack.industrimania.registers.AllItems;

import java.util.List;

public class ItemOreChunk extends Item {

    public static String rockName;
    public static OreType oreType;
    public static int grade;

    public ItemOreChunk(Properties properties, List extraParam) {
        super(properties);
        rockName = (String) extraParam.get(0);
        oreType = (OreType) extraParam.get(1);
        grade = (int) extraParam.get(2);
    }

}
