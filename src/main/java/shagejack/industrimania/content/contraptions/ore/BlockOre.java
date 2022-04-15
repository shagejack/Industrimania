package shagejack.industrimania.content.contraptions.ore;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.world.gen.OreTypeRegistry;
import shagejack.industrimania.content.world.gen.record.OreType;
import shagejack.industrimania.registers.block.grouped.AllOres;
import shagejack.industrimania.registers.item.grouped.AllOreChunks;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Objects;

public class BlockOre extends Block {

    public BlockOre(Properties properties) {
        super(properties);
    }

    public ItemStack getOreChunk(Block block, int count) {
        if (block instanceof BlockOre) {
            String key = Objects.requireNonNull(block.getRegistryName()).toString().split(":")[1];
            return new ItemStack(AllOreChunks.ORE_CHUNKS.get(key).get(), count);
        } else {
            return ItemStack.EMPTY;
        }
    }

    public OreType getOreType(Block block) {
        if (block instanceof BlockOre) {
            String key = Objects.requireNonNull(block.getRegistryName()).toString().split(":")[1].split("_")[2];
            return OreTypeRegistry.oreTypeMap.get(key);
        } else {
            return null;
        }
    }

    public int getOreGrade(Block block) {
        if (block instanceof BlockOre) {
            return Integer.parseInt(Objects.requireNonNull(block.getRegistryName()).toString().split(":")[1].split("_")[3]);
        } else {
            return 0;
        }
    }

    public Block getRock(Block block) {
        if (block instanceof BlockOre) {
            String name = "rock_" + Objects.requireNonNull(block.getRegistryName()).toString().split(":")[1].split("_")[1];
            ResourceLocation registryName = new ResourceLocation(Industrimania.MOD_ID, name);
            return ForgeRegistries.BLOCKS.getValue(registryName);
        } else {
            return null;
        }
    }

    public String getRockName(Block block) {
        if (block instanceof BlockOre) {
            return "rock_" + Objects.requireNonNull(block.getRegistryName()).toString().split(":")[1].split("_")[1];
        } else {
            return "";
        }
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);
        if (itemStack.getItem() instanceof PickaxeItem) {
            Block block = blockState.getBlock();
            Tier toolTier = ((PickaxeItem) itemStack.getItem()).getTier();
            if (toolTier.getLevel() >= getOreType(block).harvestLevel()) {
                popResource(level, blockPos, getOreChunk(block, 1));
            } else {
                if (TierSortingRegistry.isCorrectTierForDrops(toolTier, getRock(block).defaultBlockState())) {
                    popResource(level, blockPos, new ItemStack(getRock(block)));
                }
            }
        }
    }

    @Override
    public @NotNull MutableComponent getName() {
        var rockName = getRockName(this);
        var oreType = getOreType(this).name();
        var oreGrade = getOreGrade(this);

        String rockNameLocal = I18n.get("block.industrimania." + rockName),
                oreTypeLocal = I18n.get("industrimania.oretype." + oreType),
                oreGradeLocal = I18n.get( "industrimania.oregrade." + oreGrade);


        String nameLocal = rockNameLocal + oreGradeLocal + oreTypeLocal;

        return new TextComponent(nameLocal);
    }
}
