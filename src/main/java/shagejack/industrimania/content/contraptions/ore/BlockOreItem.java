package shagejack.industrimania.content.contraptions.ore;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.world.gen.OreTypeRegistry;
import shagejack.industrimania.content.world.gen.record.OreType;
import shagejack.industrimania.registers.item.grouped.AllOreChunks;

import javax.annotation.Nullable;
import java.util.Objects;

public class BlockOreItem extends BlockItem {

    public BlockOreItem(Block block, Properties properties) {
        super(block, properties);
    }

    public ItemStack getOreChunk(ItemStack stack, int count) {
        if (stack.getItem() instanceof BlockOreItem oreItem) {
            String key = Objects.requireNonNull(oreItem.getRegistryName()).toString().split(":")[1];
            return new ItemStack(AllOreChunks.ORE_CHUNKS.get(key).get(), count);
        } else {
            return ItemStack.EMPTY;
        }
    }

    public OreType getOreType(ItemStack stack) {
        if (stack.getItem() instanceof BlockOreItem oreItem) {
            String key = Objects.requireNonNull(oreItem.getRegistryName()).toString().split(":")[1].split("_")[2];
            return OreTypeRegistry.oreTypeMap.get(key);
        } else {
            return null;
        }
    }

    public int getOreGrade(ItemStack stack) {
        if (stack.getItem() instanceof BlockOreItem oreItem) {
            return Integer.parseInt(Objects.requireNonNull(oreItem.getRegistryName()).toString().split(":")[1].split("_")[3]);
        } else {
            return 0;
        }
    }

    public Block getRock(ItemStack stack) {
        if (stack.getItem() instanceof BlockOreItem oreItem) {
            String name = "rock_" + Objects.requireNonNull(oreItem.getRegistryName()).toString().split(":")[1].split("_")[1];
            ResourceLocation registryName = new ResourceLocation(Industrimania.MOD_ID, name);
            return ForgeRegistries.BLOCKS.getValue(registryName);
        } else {
            return null;
        }
    }

    public String getRockName(ItemStack stack) {
        if (stack.getItem() instanceof BlockOreItem oreItem) {
            return "rock_" + Objects.requireNonNull(oreItem.getRegistryName()).toString().split(":")[1].split("_")[1];
        } else {
            return "";
        }
    }

    @Override
    public @NotNull MutableComponent getName(@NotNull ItemStack stack) {
        var rockName = getRockName(stack);
        var oreType = getOreType(stack).name();
        var oreGrade = getOreGrade(stack);

        String rockNameLocal = I18n.get("block.industrimania." + rockName),
                oreTypeLocal = I18n.get("industrimania.oretype." + oreType),
                oreGradeLocal = I18n.get( "industrimania.oregrade." + oreGrade);


        String nameLocal = rockNameLocal + oreGradeLocal + oreTypeLocal;

        return new TextComponent(nameLocal);
    }
}
