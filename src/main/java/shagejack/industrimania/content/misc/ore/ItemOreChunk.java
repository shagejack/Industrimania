package shagejack.industrimania.content.misc.ore;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.world.gen.OreGrade;
import shagejack.industrimania.content.world.gen.OreTypeRegistry;
import shagejack.industrimania.content.world.gen.record.OreType;

import java.util.Objects;

public class ItemOreChunk extends Item {

    public ItemOreChunk(Properties properties) {
        super(properties);
    }

    public static OreType getOreType(ItemStack stack) {
        if (stack.getItem() instanceof ItemOreChunk) {
            String key = Objects.requireNonNull(stack.getItem().getRegistryName()).toString().split(":")[1].split("_")[3];
            return OreTypeRegistry.oreTypeMap.get(key);
        } else {
            return null;
        }
    }

    public static OreGrade getOreGrade(ItemStack stack) {
        if (stack.getItem() instanceof ItemOreChunk) {
            return OreGrade.getGradeFromIndex(Integer.parseInt(Objects.requireNonNull(stack.getItem().getRegistryName()).toString().split(":")[1].split("_")[4]));
        } else {
            return OreGrade.POOR;
        }
    }

    public static Block getRock(ItemStack stack) {
        if (stack.getItem() instanceof ItemOreChunk) {
            String name = "rock_" + Objects.requireNonNull(stack.getItem().getRegistryName()).toString().split(":")[1].split("_")[2];
            ResourceLocation registryName = new ResourceLocation(Industrimania.MOD_ID, name);
            return ForgeRegistries.BLOCKS.getValue(registryName);
        } else {
            return null;
        }
    }

    public static String getRockName(ItemStack stack) {
        if (stack.getItem() instanceof ItemOreChunk) {
            return  "rock_" + Objects.requireNonNull(stack.getItem().getRegistryName()).toString().split(":")[1].split("_")[2];
        } else {
            return "";
        }
    }

    public static String getRockLocalizedName(ItemStack stack) {
        return I18n.get("block.industrimania." + getRockName(stack));
    }

    @Override
    public @NotNull MutableComponent getName(@NotNull ItemStack stack) {
        String rockNameLocal = getRockLocalizedName(stack),
                oreTypeLocal = Objects.requireNonNull(getOreType(stack)).getLocalizedName(),
                oreGradeLocal = I18n.get( getOreGrade(stack).getLocalizedName());


        String nameLocal = rockNameLocal + oreGradeLocal + oreTypeLocal + I18n.get("industrimania.orechunk");

        return new TextComponent(nameLocal);
    }

}
