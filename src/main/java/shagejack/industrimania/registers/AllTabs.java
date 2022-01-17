package shagejack.industrimania.registers;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.Industrimania;

import java.util.function.Supplier;

public class AllTabs {
    public static final CreativeModeTab tab = tab(Industrimania.MOD_NAME, AllItems.omniMultimeter);

    public static final CreativeModeTab tabNature = tab(Industrimania.MOD_NAME + "_NATURE", AllBlocks.plant_lactuca_raddeana.item());

    public static final CreativeModeTab tabRock = tab(Industrimania.MOD_NAME + "_ROCK", AllBlocks.rock_rhyolite.item());

    public static final CreativeModeTab tabOre = tab(Industrimania.MOD_NAME + "_ORE", AllBlocks.ORES.get("rock_rhyolite_hematite_1").item());
    static private CreativeModeTab tab(String name, ItemStack itemStack) {
        return new CreativeModeTab(name) {
            @Override
            public ItemStack makeIcon() {
                return itemStack;
            }
        };
    }

    static private CreativeModeTab tab(String name, Item item) {
        return new CreativeModeTab(name) {
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(item);
            }
        };
    }

    static private CreativeModeTab tab(String name, Supplier<ItemStack> itemStack) {
        return new CreativeModeTab(name) {
            @Override
            public ItemStack makeIcon() {
                return itemStack.get();
            }
        };
    }

    static private CreativeModeTab tab(String name, RegistryObject<Item> itemStack) {
        return new CreativeModeTab(name) {
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(itemStack.get());
            }
        };
    }
}
