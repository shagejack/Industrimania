package shagejack.industrimania.registers;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import shagejack.industrimania.Industrimania;

public class AllTabs {
    public static final CreativeModeTab tab= new CreativeModeTab(Industrimania.MOD_NAME){

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(AllItems.omniMultimeter.get());
        }
    };
}
