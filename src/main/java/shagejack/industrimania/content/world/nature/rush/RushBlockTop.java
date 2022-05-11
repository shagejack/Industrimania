package shagejack.industrimania.content.world.nature.rush;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import shagejack.industrimania.content.world.nature.IMDoublePlantBottomGrowableBlock;
import shagejack.industrimania.content.world.nature.IMDoublePlantTopBlock;
import shagejack.industrimania.registers.block.AllBlocks;

public class RushBlockTop extends IMDoublePlantTopBlock {

    public RushBlockTop(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState p_52887_, LevelReader p_52888_, BlockPos p_52889_) {
        BlockState blockstate = p_52888_.getBlockState(p_52889_.below());
        if (p_52887_.getBlock() != this) return super.canSurvive(p_52887_, p_52888_, p_52889_); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
        return blockstate.getBlock() instanceof RushBlockBottom && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 1, false, true, false));
        }

        super.entityInside(state, level, pos, entity);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState p_60518_, boolean p_60519_) {

        if (!level.isClientSide) {
            if (level.getBlockState(pos.below()).is(getLowerBlock())) {
                level.setBlock(pos.below(), ((IMDoublePlantBottomGrowableBlock) getLowerBlock()).getStateForAge(2), 2);
            }
        }

        super.onRemove(state, level, pos, p_60518_, p_60519_);
    }

    @Override
    public Block getLowerBlock() {
        return AllBlocks.nature_rush_bottom.block().get();
    }

}
