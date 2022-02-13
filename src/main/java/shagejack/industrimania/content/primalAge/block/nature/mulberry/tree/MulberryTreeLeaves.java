package shagejack.industrimania.content.primalAge.block.nature.mulberry.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import shagejack.industrimania.registers.item.AllItems;

import java.util.Random;

public class MulberryTreeLeaves extends LeavesBlock {

    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;

    public MulberryTreeLeaves(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, Integer.valueOf(0)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_48928_) {
        p_48928_.add(STAGE);
    }

    @Override
    public void tick(BlockState p_48896_, ServerLevel p_48897_, BlockPos p_48898_, Random p_48899_) {
        super.tick(p_48896_, p_48897_, p_48898_, p_48899_);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(STAGE) == 0;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (state.getValue(STAGE) == 0 && level.random.nextDouble() < 0.05) {
            if (level.getRawBrightness(pos.above(), 0) >= 9) {
                this.grow(level, pos);
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
            }

        }
    }

    public void grow(Level level, BlockPos pos) {
        level.setBlock(pos, this.defaultBlockState().setValue(STAGE, Integer.valueOf(1)), 3);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {

        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        level.setBlock(pos, this.defaultBlockState().setValue(STAGE, Integer.valueOf(0)), 3);
        dropItem(level, pos, new ItemStack(AllItems.mulberryFruit.get()));

        return InteractionResult.PASS;
    }

    public void dropItem(Level level, BlockPos pos, ItemStack stack) {
        float f = EntityType.ITEM.getHeight() / 2.0F;
        double d0 = (double)((float)pos.getX() + 0.5F) + Mth.nextDouble(level.random, -0.25D, 0.25D);
        double d1 = (double)((float)pos.getY() + 1.0F) + Mth.nextDouble(level.random, -0.25D, 0.25D) - (double)f;
        double d2 = (double)((float)pos.getZ() + 0.5F) + Mth.nextDouble(level.random, -0.25D, 0.25D);
        ItemEntity item = new ItemEntity(level, d0, d1, d2, stack);
        item.setDefaultPickUpDelay();
        level.addFreshEntity(item);
    }

}
