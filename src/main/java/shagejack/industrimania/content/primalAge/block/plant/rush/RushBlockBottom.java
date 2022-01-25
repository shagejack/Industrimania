package shagejack.industrimania.content.primalAge.block.plant.rush;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import shagejack.industrimania.content.primalAge.block.plant.IMDoublePlantBottomGrowableBlock;
import shagejack.industrimania.registers.block.AllBlocks;

public class RushBlockBottom extends IMDoublePlantBottomGrowableBlock {

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 24.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
    };

    public RushBlockBottom(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState p_52896_, LevelAccessor p_52897_, BlockPos p_52898_, BlockPos p_52899_) {
        DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
        return doubleblockhalf == DoubleBlockHalf.LOWER && !state.canSurvive(p_52897_, p_52898_) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, p_52896_, p_52897_, p_52898_, p_52899_);
    }

    @Override
    protected int getBonemealAgeIncrease(Level p_52262_) {
        return Mth.nextInt(p_52262_.random, 1, 2);
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state, Boolean bonemeal) {
        int i = this.getAge(state) + this.getBonemealAgeIncrease(level);

        if (!bonemeal)
            i = this.getAge(state) + 1;

        int j = this.getMaxAge();

        if (i >= j) {
            i = j;
            if (level.getBlockState(pos.above()).isAir())
                level.setBlock(pos.above(), getUpperBlock().defaultBlockState(), 2);
        }

        level.setBlock(pos, this.getStateForAge(i), 2);
    }

    @Override
    public Block getUpperBlock() {
        return AllBlocks.plant_rush_top.block().get();
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return AllBlocks.plant_rush_bottom.item().get();
    }

    @Override
    public BlockState getPlant(BlockGetter world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() != this) return defaultBlockState();
        return state;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).setSpeed(((LivingEntity) entity).getSpeed() * 0.1f);
        }

        super.entityInside(state, level, pos, entity);
    }
}
