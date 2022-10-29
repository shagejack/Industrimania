package shagejack.industrimania.content.misc.ore;

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
import shagejack.industrimania.content.world.gen.OreGrade;
import shagejack.industrimania.content.world.gen.OreTypeRegistry;
import shagejack.industrimania.content.world.gen.record.OreType;
import shagejack.industrimania.registries.item.grouped.AllOreChunks;

import java.util.Objects;

public class BlockOreItem extends BlockItem {

    public BlockOreItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static ItemStack getOreChunk(ItemStack stack, int count) {
        if (stack.getItem() instanceof BlockOreItem oreItem) {
            String key = Objects.requireNonNull(oreItem.getRegistryName()).toString().split(":")[1];
            return new ItemStack(AllOreChunks.ORE_CHUNKS.get(key).get(), count);
        } else {
            return ItemStack.EMPTY;
        }
    }

    public static OreType getOreType(ItemStack stack) {
        if (stack.getItem() instanceof BlockOreItem oreItem) {
            String key = Objects.requireNonNull(oreItem.getRegistryName()).toString().split(":")[1].split("_")[2];
            return OreTypeRegistry.oreTypeMap.get(key);
        } else {
            return null;
        }
    }

    public static OreGrade getOreGrade(ItemStack stack) {
        if (stack.getItem() instanceof BlockOreItem oreItem) {
            return OreGrade.getGradeFromIndex(Integer.parseInt(Objects.requireNonNull(oreItem.getRegistryName()).toString().split(":")[1].split("_")[3]));
        } else {
            return OreGrade.POOR;
        }
    }

    public static Block getRock(ItemStack stack) {
        if (stack.getItem() instanceof BlockOreItem oreItem) {
            String name = "rock_" + Objects.requireNonNull(oreItem.getRegistryName()).toString().split(":")[1].split("_")[1];
            ResourceLocation registryName = new ResourceLocation(Industrimania.MOD_ID, name);
            return ForgeRegistries.BLOCKS.getValue(registryName);
        } else {
            return null;
        }
    }

    public static String getRockName(ItemStack stack) {
        if (stack.getItem() instanceof BlockOreItem oreItem) {
            return "rock_" + Objects.requireNonNull(oreItem.getRegistryName()).toString().split(":")[1].split("_")[1];
        } else {
            return "";
        }
    }

    public static String getRockLocalizedName(ItemStack stack) {
        return I18n.get("block.industrimania." + getRockName(stack));
    }

    @Override
    public @NotNull MutableComponent getName(@NotNull ItemStack stack) {
        String rockNameLocal = I18n.get(getRockLocalizedName(stack)),
                oreTypeLocal = I18n.get(Objects.requireNonNull(getOreType(stack)).getLocalizedName()),
                oreGradeLocal = I18n.get(getOreGrade(stack).getLocalizedName());


        String nameLocal = rockNameLocal + oreGradeLocal + oreTypeLocal;

        return new TextComponent(nameLocal);
    }
}
