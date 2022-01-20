package shagejack.industrimania.registers;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.registers.block.AllBlocks;
import shagejack.industrimania.registers.block.grouped.AllOres;
import shagejack.industrimania.registers.block.grouped.AllRocks;
import shagejack.industrimania.registers.item.AllItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AllTabs {

    public static final List<CreativeModeTab> tabs = new ArrayList<>();

    public static final CreativeModeTab tab = tab(Industrimania.MOD_ID, () -> () -> AllItems.omniMultimeter);

    public static final CreativeModeTab tabMaterial = tab(Industrimania.MOD_ID + "_material", () -> () -> AllItems.clinker);

    public static final CreativeModeTab tabTool = tab(Industrimania.MOD_ID + "_tool", () -> () -> AllItems.bronzeSaw);

    public static final CreativeModeTab tabEquipment = tab(Industrimania.MOD_ID + "_equipment", () -> () -> AllItems.hazardProtectiveChestplate);

    public static final CreativeModeTab tabNature = tab(Industrimania.MOD_ID + "_nature", () -> AllBlocks.plant_lactuca_raddeana::item);

    public static final CreativeModeTab tabRock = tab(Industrimania.MOD_ID + "_rock", () -> AllBlocks.rock_rhyolite::item);

    public static final CreativeModeTab tabOre = tab(Industrimania.MOD_ID + "_ore", () -> () -> AllOres.ORES.get("rock_rhyolite_hematite_1").item());

    static private CreativeModeTab tab(String name, Supplier<Supplier<RegistryObject<Item>>> itemStack) {
        var tab = new CreativeModeTab(name) {
            @Override
            public @NotNull ItemStack makeIcon() {
                return new ItemStack(itemStack.get().get().get());
            }
        };
        tabs.add(tab);
        return tab;
    }
}
