package shagejack.industrimania.content.primalAge.block.nature.mulberry.bush;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import shagejack.industrimania.registers.item.AllItems;

import java.util.Random;

public class MulberryBush extends Block implements BonemealableBlock {

    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;

    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    public MulberryBush(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_48928_) {
        p_48928_.add(STAGE);
    }

    @Override
    public VoxelShape getShape(BlockState p_48945_, BlockGetter p_48946_, BlockPos p_48947_, CollisionContext p_48948_) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_48950_, BlockGetter p_48951_, BlockPos p_48952_, CollisionContext p_48953_) {
        return SHAPE;
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState p_181159_, BlockGetter p_181160_, BlockPos p_181161_) {
        return false;
    }

    @Override
    public void tick(BlockState p_48896_, ServerLevel p_48897_, BlockPos p_48898_, Random p_48899_) {
        if (!p_48896_.canSurvive(p_48897_, p_48898_)) {
            p_48897_.destroyBlock(p_48898_, true);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(STAGE) == 0;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (state.getValue(STAGE) == 0 && level.random.nextDouble() < 0.2) {
            if (level.getRawBrightness(pos.above(), 0) >= 9) {
                this.grow(level, pos);
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
            }

        }
    }

    @Override
    public boolean canSurvive(BlockState p_48917_, LevelReader p_48918_, BlockPos p_48919_) {
        return p_48918_.getBlockState(p_48919_.below()).is(BlockTags.DIRT);
    }

    public BlockState updateShape(BlockState p_48921_, Direction p_48922_, BlockState p_48923_, LevelAccessor p_48924_, BlockPos p_48925_, BlockPos p_48926_) {
        if (!p_48921_.canSurvive(p_48924_, p_48925_)) {
            p_48924_.scheduleTick(p_48925_, this, 1);
        }

        return super.updateShape(p_48921_, p_48922_, p_48923_, p_48924_, p_48925_, p_48926_);
    }

    public boolean isValidBonemealTarget(BlockGetter p_48886_, BlockPos pos, BlockState p_48888_, boolean p_48889_) {
        return p_48886_.getBlockState(pos).getValue(STAGE) != 1;
    }

    public boolean isBonemealSuccess(Level level, Random random, BlockPos pos, BlockState state) {
        return random.nextDouble() < 0.4;
    }

    public void performBonemeal(ServerLevel level, Random p_48877_, BlockPos pos, BlockState p_48879_) {
        this.grow(level, pos);
    }

    public void grow(Level level, BlockPos pos) {
        level.setBlock(pos, this.defaultBlockState().setValue(STAGE, 1), 3);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {

        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        if (state.getValue(STAGE).equals(1)) {
            level.setBlock(pos, this.defaultBlockState().setValue(STAGE, 0), 3);
            dropItem(level, pos, new ItemStack(AllItems.mulberryFruit.get()));
        }

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
