package shagejack.shagecraft.registers;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import shagejack.shagecraft.ShageCraft;

public class AllTabs {
    public static final CreativeModeTab tab= new CreativeModeTab(ShageCraft.MOD_NAME){

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(AllItems.omniMultimeter.get());
        }
    };
}
