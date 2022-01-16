package shagejack.industrimania.content.contraptions.ore;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import shagejack.industrimania.content.worldGen.record.OreType;
import shagejack.industrimania.registers.AllItems;

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

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        //TODO: drop ore chunk item
        String key = rockName + "_" + oreType.name() + "_" + grade;
        int count = 1;
        ItemEntity oreChunk = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(AllItems.ORE_CHUNKS.get(key).get(), count));
        oreChunk.spawnAtLocation(AllItems.ORE_CHUNKS.get(key).get());
        return true;
    }
}
