package shagejack.industrimania.content.contraptions.ore;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.TierSortingRegistry;
import shagejack.industrimania.content.worldGen.record.OreType;
import shagejack.industrimania.registers.AllItems;

import javax.annotation.Nullable;
import java.util.List;

public class BlockOre extends Block {

    public static String rockName;
    public static OreType oreType;
    public static int grade;

    public BlockOre(Properties properties, List extraParam) {
        super(properties);
        rockName = (String) extraParam.get(0);
        oreType = (OreType) extraParam.get(1);
        grade = (int) extraParam.get(2);
    }

    public Item getOreChunk() {
        String key = rockName + "_" + oreType.name() + "_" + grade;
        return AllItems.ORE_CHUNKS.get(key).get();
    }


    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);
        if (itemStack.getItem() instanceof PickaxeItem) {
            if (((PickaxeItem) itemStack.getItem()).getTier().getLevel() >= oreType.harvestLevel()) {
                popResource(level, blockPos, new ItemStack(getOreChunk()));
            }
        }
    }
}
