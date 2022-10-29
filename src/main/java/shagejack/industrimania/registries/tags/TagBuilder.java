package shagejack.industrimania.registries.tags;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.registries.dataGen.DataGenHandle;
import shagejack.industrimania.registries.dataGen.TagTask;

import java.util.List;

public class TagBuilder {

    public static void blockTag(RegistryObject<Block> block, List<String> tags) {
        DataGenHandle.addBlockTagTask(new TagTask<>(block, tags));
    }

    public static void itemTag(RegistryObject<Item> item, List<String> tags) {
        DataGenHandle.addItemTagTask(new TagTask<>(item, tags));
    }

    public static void fluidTag(RegistryObject<? extends Fluid> fluid, List<String> tags) {
        DataGenHandle.addFluidTagTask(new TagTask<>(fluid, tags));
    }

}
