package shagejack.industrimania.registers;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import shagejack.industrimania.Industrimania;

public class AllTabs {
    public static final CreativeModeTab tab = new CreativeModeTab(Industrimania.MOD_NAME){
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(AllItems.omniMultimeter.get());
        }
    };

    public static final CreativeModeTab tabNature = new CreativeModeTab(Industrimania.MOD_NAME + "_NATURE"){
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(AllBlocks.plant_lactuca_raddeana.item().get());
        }
    };


    public static final CreativeModeTab tabRock = new CreativeModeTab(Industrimania.MOD_NAME + "_ROCK"){
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(AllBlocks.rock_rhyolite.item().get());
        }
    };

    public static final CreativeModeTab tabOre = new CreativeModeTab(Industrimania.MOD_NAME + "_ORE"){
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(AllBlocks.ORES.get("rock_rhyolite_hematite_1").item().get());
        }
    };
}
