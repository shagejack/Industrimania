package shagejack.industrimania.content.primalAge.block.clayKiln;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shagejack.industrimania.content.misc.processing.ProcessingInventory;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.foundation.utility.recipe.RecipeConditions;
import shagejack.industrimania.foundation.utility.recipe.RecipeFinder;
import shagejack.industrimania.registries.AllRecipeTypes;
import shagejack.industrimania.registries.AllTileEntities;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ClayKilnTileEntity extends SmartTileEntity {

    private static final Object clayKilnRecipesKey = new Object();
    public ProcessingInventory inventory;
    private final LazyOptional<IItemHandler> invProvider;
    private ResourceLocation currentRecipeId = null;
    private boolean isBurning = false;

    public ClayKilnTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.clay_kiln.get(), pos, state);
        this.inventory = new ProcessingInventory(stack -> start()).withSlotLimit(true);
        this.invProvider = LazyOptional.of(() -> inventory);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tag.put("Inventory", inventory.serializeNBT());
        tag.putBoolean("IsBurning", isBurning);
        tag.putString("CurrentRecipeId", currentRecipeId.toString());
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        inventory.deserializeNBT(tag);
        isBurning = tag.getBoolean("IsBurning");
        currentRecipeId = new ResourceLocation(tag.getString("CurrentRecipeId"));
    }

    @Override
    public void tick() {

        if (level == null)
            return;

        this.isBurning = level.getBlockState(getBlockPos().below()).is(BlockTags.FIRE);

        if (this.isBurning) {
            if (this.inventory.appliedRecipe && currentRecipeId != null) {

                // reset if recipe changed
                Optional<ClayKilnRecipe> recipeOptional = getRecipeOptional();
                if (recipeOptional.isEmpty() || currentRecipeId != recipeOptional.get().getId()) {
                    start();
                    return;
                }

                if (inventory.remainingTime == 0) {
                    applyRecipe();
                } else if (inventory.remainingTime > 0) {
                    inventory.remainingTime--;
                }
            } else if (inventory.remainingTime == 0) {
                start();
            }
        } else {
            resetRecipe();
        }

    }

    public void start() {
        Optional<ClayKilnRecipe> recipeOptional = getRecipeOptional();

        if (recipeOptional.isPresent()) {
            this.currentRecipeId = recipeOptional.get().getId();
            this.inventory.recipeDuration = recipeOptional.get().getProcessingDuration();
            this.inventory.remainingTime = this.inventory.recipeDuration;
            this.inventory.appliedRecipe = true;
        } else {
            resetRecipe();
        }
    }

    public void applyRecipe() {
        if (currentRecipeId == null || getRecipeOptional().isEmpty())
            return;

        this.inventory.clear();
        this.inventory.setStackInSlot(1, getRecipeOptional().get().getResultItem());
    }

    private void resetRecipe() {
        this.currentRecipeId = null;
        this.inventory.recipeDuration = 0;
        this.inventory.remainingTime = 0;
        this.inventory.appliedRecipe = false;
    }

    private Optional<ClayKilnRecipe> getRecipeOptional() {
        Predicate<Recipe<?>> type;

        type = RecipeConditions.isOfType(AllRecipeTypes.CLAY_KILN.getType());

        List<Recipe<?>> recipesList = RecipeFinder.get(clayKilnRecipesKey, level, type)
                .stream()
                .filter(RecipeConditions.firstIngredientMatches(inventory.getStackInSlot(0)))
                .toList();

        if (!recipesList.isEmpty() && recipesList.get(0) instanceof ClayKilnRecipe recipe) {
            return Optional.of(recipe);
        }

        return Optional.empty();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != Direction.DOWN)
            return invProvider.cast();

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.invProvider.invalidate();
    }
}
