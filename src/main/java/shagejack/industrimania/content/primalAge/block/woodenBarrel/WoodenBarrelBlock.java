package shagejack.industrimania.content.primalAge.block.woodenBarrel;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shagejack.industrimania.content.contraptions.multimeter.IMultimeterInfo;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.foundation.utility.TileEntityUtils;
import shagejack.industrimania.registers.AllTileEntities;
import shagejack.industrimania.registers.item.AllItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WoodenBarrelBlock extends Block implements ITE<WoodenBarrelTileEntity>, IMultimeterInfo {

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    public WoodenBarrelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(OPEN, true));
    }

    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable BlockGetter getter, List<Component> components, TooltipFlag tooltipFlag) {
        CompoundTag tag = stack.getOrCreateTag();

        if (tag.contains("Open", Tag.TAG_BYTE) && !tag.getBoolean("Open")) {

            FluidTank tank = new FluidTank(WoodenBarrelTileEntity.CAPACITY);
            tank.readFromNBT(tag);

            MutableComponent infoFluidText = new TextComponent(I18n.get("industrimania.mechanic_wooden_barrel_info_fluid")).append(": ").withStyle(ChatFormatting.AQUA);
            MutableComponent infoAmountText = new TextComponent(I18n.get("industrimania.mechanic_wooden_barrel_info_amount")).append(": ").withStyle(ChatFormatting.RED);

            components.add(infoFluidText.append(new TextComponent(tank.getFluid().getDisplayName().getString()).withStyle(ChatFormatting.WHITE)));
            components.add(infoAmountText.append(new TextComponent(String.valueOf(tank.getFluid().getAmount())).append("mB / ").append(String.valueOf(tank.getCapacity())).append("mB").withStyle(ChatFormatting.GREEN)));

        }
    }

    @Override
    public List<Component> getInfo(Level level, BlockPos pos) {
        List<Component> components = new ArrayList<>();

        BlockState state = level.getBlockState(pos);

        if (level.getBlockEntity(pos) instanceof WoodenBarrelTileEntity te) {

            MutableComponent infoOpenText = new TextComponent(I18n.get("industrimania.mechanic_wooden_barrel_info_state")).append(": ").withStyle(ChatFormatting.GOLD);
            MutableComponent infoFluidText = new TextComponent(I18n.get("industrimania.mechanic_wooden_barrel_info_fluid")).append(": ").withStyle(ChatFormatting.AQUA);
            MutableComponent infoAmountText = new TextComponent(I18n.get("industrimania.mechanic_wooden_barrel_info_amount")).append(": ").withStyle(ChatFormatting.RED);
            MutableComponent openState = state.getValue(OPEN) ?
                    new TextComponent(I18n.get("industrimania.mechanic_wooden_barrel_info_state_open")).withStyle(ChatFormatting.DARK_GREEN) :
                    new TextComponent(I18n.get("industrimania.mechanic_wooden_barrel_info_state_sealed")).withStyle(ChatFormatting.DARK_RED);

            components.add(infoOpenText.append(openState));
            components.add(infoFluidText.append(new TextComponent(te.tank.getFluid().getDisplayName().getString()).withStyle(ChatFormatting.WHITE)));
            components.add(infoAmountText.append(new TextComponent(String.valueOf(te.tank.getFluid().getAmount())).append("mB / ").append(String.valueOf(te.tank.getCapacity())).append("mB").withStyle(ChatFormatting.GREEN)));

        }

        return components;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPEN);
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<WoodenBarrelTileEntity> getTileEntityClass() {
        return WoodenBarrelTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends WoodenBarrelTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends WoodenBarrelTileEntity>) AllTileEntities.wooden_barrel.get();
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean p_48717_) {
        if (oldState.hasBlockEntity() && oldState.getBlock() != newState.getBlock()) {
            withTileEntityDo(level, pos, barrel -> barrel.destroy(oldState.getValue(OPEN)));
            level.removeBlockEntity(pos);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
        Optional<WoodenBarrelTileEntity> te = TileEntityUtils.get(getTileEntityClass(), getter, pos);
        if (te.isPresent()) {
            return te.get().getDrop(state.getValue(OPEN));
        }
        return ItemStack.EMPTY;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        super.use(state, level, pos, player, hand, result);

        if (!state.getValue(OPEN)) {
            if (player.isShiftKeyDown()) {
                level.setBlock(pos, state.setValue(OPEN, true), 3);
                player.addItem(new ItemStack(AllItems.woodenBarrelCover.get()));
                return InteractionResult.SUCCESS;
            }
        } else {
            ItemStack stack = player.getItemInHand(hand);

            if (stack.isEmpty())
                return InteractionResult.FAIL;

            LazyOptional<IFluidHandlerItem> handler = FluidUtil.getFluidHandler(stack);

            if (handler.isPresent()) {
                if (FluidUtil.interactWithFluidHandler(player, hand, level, pos, result.getDirection()))
                    return InteractionResult.CONSUME_PARTIAL;

                return InteractionResult.FAIL;
            } else if (stack.is(AllItems.woodenBarrelCover.get())) {
                player.setItemInHand(hand, ItemStack.EMPTY);
                level.setBlock(pos, state.setValue(OPEN, false), 3);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.FAIL;

    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(level, pos, state, entity, stack);

        if (stack.getOrCreateTag().contains("Open", Tag.TAG_BYTE)) {
            level.setBlock(pos, state.setValue(OPEN, stack.getOrCreateTag().getBoolean("Open")), 3);
        } else {
            level.setBlock(pos, state.setValue(OPEN, true), 3);
        }

        withTileEntityDo(level, pos, barrel -> barrel.initByItem(stack));
    }

}
