package shagejack.industrimania.content.primalAge.block.mixingBasin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import shagejack.industrimania.content.primalAge.block.clayKiln.ClayKilnRecipe;
import shagejack.industrimania.foundation.fluid.CombinedTankWrapper;
import shagejack.industrimania.foundation.fluid.FluidTankBase;
import shagejack.industrimania.foundation.item.SmartInventory;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.foundation.utility.Couple;
import shagejack.industrimania.foundation.utility.recipe.RecipeConditions;
import shagejack.industrimania.foundation.utility.recipe.RecipeFinder;
import shagejack.industrimania.registers.AllRecipeTypes;
import shagejack.industrimania.registers.AllTileEntities;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MixingBasinTileEntity extends SmartTileEntity {

    private static final Object mixingBasinRecipesKey = new Object();

    private int prevStirProgress = 0;
    private int stirCount = 0;
    private int itemPos = 0;
    private boolean mixing = false;
    private boolean countUp = true;

    public MixingBasinInventory inputInventory;
    public FluidTankBase<MixingBasinTileEntity> inputTank;
    protected SmartInventory outputInventory;
    protected FluidTankBase<MixingBasinTileEntity> outputTank;

    private boolean contentsChanged;

    private Couple<SmartInventory> invs;
    private Couple<FluidTankBase<MixingBasinTileEntity>> tanks;

    protected LazyOptional<IItemHandlerModifiable> itemCapability;
    protected LazyOptional<IFluidHandler> fluidCapability;

    public MixingBasinTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.mixing_basin.get(), pos, state);

        inputInventory = new MixingBasinInventory(9, this);
        inputInventory.whenContentsChanged($ -> contentsChanged = true);
        outputInventory = new MixingBasinInventory(9, this).forbidInsertion().withMaxStackSize(64);
        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(inputInventory, outputInventory));
        contentsChanged = true;

        invs = Couple.create(inputInventory, outputInventory);
        tanks = Couple.create(inputTank, outputTank);

        fluidCapability = LazyOptional.of(() -> {
            LazyOptional<? extends IFluidHandler> inputCap = LazyOptional.of(() -> inputTank);
            LazyOptional<? extends IFluidHandler> outputCap = LazyOptional.of(() -> outputTank);
            return new CombinedTankWrapper(inputCap.orElse(null), outputCap.orElse(null));
        });
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inputInventory.deserializeNBT(compound.getCompound("InputItems"));
        outputInventory.deserializeNBT(compound.getCompound("OutputItems"));

        stirCount = compound.getInt("stirCount");
        mixing = compound.getBoolean("mixing");

        if (!clientPacket)
            return;

        // client stuff
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("InputItems", inputInventory.serializeNBT());
        compound.put("OutputItems", outputInventory.serializeNBT());

        compound.putInt("StirCount", stirCount);
        compound.putBoolean("Mixing", getMixing());


        if (!clientPacket)
            return;

        // client stuff
    }

    public boolean getMixing() {
        return this.mixing;
    }

    public void setMixing(boolean mixing) {
        this.mixing = mixing;
    }

    public void onEmptied() {

    }

    @Override
    public void setRemoved() {
        itemCapability.invalidate();
        fluidCapability.invalidate();
        super.setRemoved();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemCapability.invalidate();
        fluidCapability.invalidate();
    }

    @Override
    protected void setRemovedNotDueToChunkUnload() {
        onEmptied();
        super.setRemovedNotDueToChunkUnload();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemCapability.cast();
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }

    public boolean acceptOutputs(List<ItemStack> outputItems, List<FluidStack> outputFluids, boolean simulate) {
        outputInventory.allowInsertion();
        boolean acceptOutputsInner = acceptOutputsInner(outputItems, outputFluids, simulate);
        outputInventory.forbidInsertion();
        return acceptOutputsInner;
    }

    private boolean acceptOutputsInner(List<ItemStack> outputItems, List<FluidStack> outputFluids, boolean simulate) {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof MixingBasinBlock))
            return false;

        Direction direction = blockState.getValue(MixingBasinBlock.FACING);
        IItemHandler targetInv = outputInventory;
        IFluidHandler targetTank = outputTank;

        if (!outputItems.isEmpty() && targetInv == null)
            return false;

        // accept output items
        if (!acceptItemOutputsIntoBasin(outputItems, simulate, targetInv))
            return false;
        if (outputFluids.isEmpty())
            return true;
        if (targetTank == null)
            return false;
        if (!acceptFluidOutputsIntoBasin(outputFluids, simulate, targetTank))
            return false;

        return true;
    }

    private boolean acceptFluidOutputsIntoBasin(List<FluidStack> outputFluids, boolean simulate, IFluidHandler targetTank) {
        for (FluidStack fluidStack : outputFluids) {
            IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
            int fill = targetTank.fill(fluidStack.copy(), action);
            if (fill != fluidStack.getAmount())
                return false;
        }
        return true;
    }

    private boolean acceptItemOutputsIntoBasin(List<ItemStack> outputItems, boolean simulate, IItemHandler targetInv) {
        for (ItemStack itemStack : outputItems) {
            // Catalyst items are never consumed
            if (itemStack.hasContainerItem() && itemStack.getContainerItem().sameItem(itemStack))
                continue;
            if (!ItemHandlerHelper.insertItemStacked(targetInv, itemStack.copy(), simulate).isEmpty())
                return false;
        }
        return true;
    }

    public void notifyChangeOfContents() {
        contentsChanged = true;
    }

    public SmartInventory getInputInventory() {
        return inputInventory;
    }

    public SmartInventory getOutputInventory() {
        return outputInventory;
    }

    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
    }

    @Override
    public void tick() {
        super.tick();

        handleStir();

        if (level.isClientSide()) {
            // handle client side stuff
        }

        if (!contentsChanged)
            return;

        contentsChanged = false;
    }

    public int getStirProgress() {
        return stirProgress;
    }

    public void setStirProgress(int stirProgress) {
        this.stirProgress = stirProgress;
    }

    private int stirProgress = 0;

    public int getStirCount() {
        return stirCount;
    }

    public void setStirCount(int stirCount) {
        this.stirCount = stirCount;
    }

    private void handleStir() {
        prevStirProgress = stirProgress;
        if (level.isClientSide()) {
            if (countUp && itemPos <= 20) {
                itemPos++;
                if (itemPos == 20)
                    countUp = false;
            }
            if (!countUp && itemPos >= 0) {
                itemPos--;
                if (itemPos == 0)
                    countUp = true;
            }
        }

        if (getMixing() && stirProgress < 90)
            stirProgress += 2;
        if (stirProgress >= 90) {
            setMixing(false);
            stirProgress = 0;
            prevStirProgress = 0;
        }

        if (!level.isClientSide()) {
            if (getStirCount() >= 20 &&  inputTank.getFluidAmount() >= 1000 && stirProgress >= 88) {
                ItemStack output = ItemStack.EMPTY;

                Optional<MixingBasinRecipe> recipeOptional = getRecipeOptional();

                if (recipeOptional.isPresent()) {
                    if (MixingBasinRecipe.apply(this, recipeOptional.get(), true))
                        MixingBasinRecipe.apply(this, recipeOptional.get());
                } else {
                    setStirCount(0);
                }
            }
        }
    }

    private Optional<MixingBasinRecipe> getRecipeOptional() {
        Predicate<Recipe<?>> type;

        type = RecipeConditions.isOfType(AllRecipeTypes.MIXING_BASIN.getType());

        List<Recipe<?>> recipesList = RecipeFinder.get(mixingBasinRecipesKey, level, type).stream().toList();

        recipesList = recipesList.stream().filter(r -> r instanceof MixingBasinRecipe basinRecipe && MixingBasinRecipe.match(this, basinRecipe)).toList();

        if (!recipesList.isEmpty() && recipesList.get(0) instanceof MixingBasinRecipe recipe) {
            return Optional.of(recipe);
        }

        return Optional.empty();
    }


}
