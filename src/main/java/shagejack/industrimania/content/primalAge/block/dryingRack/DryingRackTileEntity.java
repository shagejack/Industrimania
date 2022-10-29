package shagejack.industrimania.content.primalAge.block.dryingRack;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import shagejack.industrimania.content.misc.processing.ProcessingInventory;
import shagejack.industrimania.foundation.item.ItemHelper;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.foundation.tileEntity.behaviour.filtering.FilteringBehaviour;
import shagejack.industrimania.foundation.utility.recipe.RecipeConditions;
import shagejack.industrimania.foundation.utility.recipe.RecipeFinder;
import shagejack.industrimania.registries.AllRecipeTypes;
import shagejack.industrimania.registries.AllTileEntities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DryingRackTileEntity extends SmartTileEntity {

    private static final AABB RENDER_BOX = new AABB(0, 0, 0, 1, 1, 1);

    private static final Object dryingRackRecipesKey = new Object();
    private static final Object dryingRackRottenRecipesKey = new Object();
    public ProcessingInventory inventory;
    private final LazyOptional<IItemHandler> invProvider;
    private int recipeIndex;
    private int recipeIndexRotten;
    private FilteringBehaviour filtering;
    private boolean processingRotten;

    private ItemStack lastItem;

    public DryingRackTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.drying_rack.get(), pos, state);
        inventory = new ProcessingInventory(this::start).withSlotLimit(true);
        inventory.remainingTime = -1;
        invProvider = LazyOptional.of(() -> inventory);
        lastItem = ItemStack.EMPTY;
        processingRotten = false;
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
        filtering = new FilteringBehaviour(this, new DryingRackFilterSlot()).forRecipes();
        behaviours.add(filtering);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.put("Inventory", inventory.serializeNBT());
        compound.putInt("RecipeIndex", recipeIndex);
        compound.putInt("RecipeIndexRotten", recipeIndexRotten);
        compound.putBoolean("ProcessingRotten", processingRotten);
        super.write(compound, clientPacket);

        if (!clientPacket || lastItem.isEmpty())
            return;
        compound.put("LastItem", lastItem.serializeNBT());
        lastItem = ItemStack.EMPTY;
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inventory.deserializeNBT(compound.getCompound("Inventory"));
        recipeIndex = compound.getInt("RecipeIndex");
        recipeIndexRotten = compound.getInt("RecipeIndexRotten");
        processingRotten = compound.getBoolean("ProcessingRotten");
        if (compound.contains("LastItem"))
            lastItem = ItemStack.of(compound.getCompound("LastItem"));
    }

    protected AABB makeRenderBoundingBox() {
        return RENDER_BOX.inflate(.125f)
                .move(worldPosition);
    }

    @Override
    public void tick() {
        super.tick();

        if (inventory.isEmpty()) {
            inventory.remainingTime = -1;
            inventory.appliedRecipe = false;
            sendData();
            return;
        }

        if (startRotten(inventory.getStackInSlot(0))) {
            applyRecipe();
            inventory.appliedRecipe = true;
            inventory.recipeDuration = 20;
            inventory.remainingTime = -1;
            sendData();
            return;
        }

        if (!processingRotten && inventory.remainingTime == -1) {
            start(inventory.getStackInSlot(0));
            processingRotten = false;
            sendData();
            return;
        }

        inventory.remainingTime -= 1;

        if (inventory.remainingTime < 5 && !inventory.appliedRecipe && !lastItem.isEmpty()) {
            if (level.isClientSide && !isVirtual())
                return;

            if (inventory.getStackInSlot(0).isEmpty())
                return;

            if (lastItem.equals(inventory.getStackInSlot(0), true)) {
                inventory.appliedRecipe = false;
                inventory.recipeDuration = 20;
                inventory.remainingTime = -1;
                lastItem = ItemStack.EMPTY;
                sendData();
                return;
            }

            applyRecipe();
            inventory.appliedRecipe = true;
            inventory.recipeDuration = 20;
            inventory.remainingTime = -1;
            lastItem = ItemStack.EMPTY;
            sendData();
        }
    }

    private void applyRecipe() {
        Recipe<?> recipe;

        if (!processingRotten) {
            List<? extends Recipe<?>> recipes = getRecipes();
            if (recipes.isEmpty())
                return;

            if (recipeIndex >= recipes.size())
                recipeIndex = 0;

            recipe = recipes.get(recipeIndex);

        } else {
            List<? extends Recipe<?>> recipes = getRecipesRotten();
            if (recipes.isEmpty())
                return;

            if (recipeIndexRotten >= recipes.size())
                recipeIndexRotten = 0;

            recipe = recipes.get(recipeIndexRotten);
        }

        int rolls = inventory.getStackInSlot(0)
                .getCount();
        inventory.clear();

        List<ItemStack> list = new ArrayList<>();
        for (int roll = 0; roll < rolls; roll++) {
            List<ItemStack> results = new LinkedList<>();
            if (recipe instanceof DryingRackRecipe) {
                results = ((DryingRackRecipe) recipe).rollResults();

                if (results.isEmpty()) {
                    List<? extends Recipe<?>> recipesRotten = getRecipesRotten();
                    if (recipesRotten.isEmpty())
                        return;

                    if (recipeIndexRotten >= recipesRotten.size())
                        recipeIndexRotten = 0;

                    results.add(recipesRotten.get(recipeIndexRotten).getResultItem().copy());
                }

            } else if (recipe instanceof DryingRackRottenRecipe) {
                results.add(recipe.getResultItem()
                        .copy());
            }

            for (ItemStack stack : results) {
                ItemHelper.addToList(stack, list);
            }
        }
        for (int slot = 0; slot < list.size() && slot < inventory.getSlots(); slot++) {
            inventory.setStackInSlot(slot, list.get(slot));
        }

        processingRotten = false;
        sendData();

    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != Direction.DOWN)
            return invProvider.cast();
        return super.getCapability(cap, side);
    }

    public void start(ItemStack inserted) {
        assert level != null;
        if (level.isClientSide && !isVirtual())
            return;

        if (inventory.isEmpty() || (!lastItem.isEmpty() && !lastItem.equals(inserted, true))) {
            inventory.remainingTime = -1;
            inventory.appliedRecipe = false;
            lastItem = ItemStack.EMPTY;
            sendData();
            return;
        }

        List<? extends Recipe<?>> recipes = getRecipes();
        boolean valid = !recipes.isEmpty();
        int time;

        if (!valid) {
            inventory.remainingTime = -1;
            inventory.appliedRecipe = false;
            lastItem = ItemStack.EMPTY;
            sendData();
            return;
        }

        recipeIndex++;
        if (recipeIndex >= recipes.size() || recipeIndex < 0)
            recipeIndex = 0;


        Recipe<?> recipe = recipes.get(recipeIndex);

        if (recipe instanceof DryingRackRecipe) {
            time = ((DryingRackRecipe) recipe).getProcessingDuration();
            inventory.remainingTime = time;
            inventory.recipeDuration = time;
            inventory.appliedRecipe = false;
            lastItem = inserted.copy();
        }

        sendData();
    }

    public boolean startRotten(ItemStack inserted) {
        if (inventory.isEmpty()) {
            processingRotten = false;
            sendData();
            return false;
        }

        if (level.isClientSide && !isVirtual())
            return false;

        List<? extends Recipe<?>> recipes = getRecipesRotten();
        boolean valid = !recipes.isEmpty();

        if (!valid) {
            processingRotten = false;
            sendData();
            return false;
        }

        recipeIndexRotten++;
        if (recipeIndexRotten >= recipes.size() || recipeIndexRotten < 0)
            recipeIndexRotten = 0;


        Recipe<?> recipe = recipes.get(recipeIndexRotten);

        if (recipe instanceof DryingRackRottenRecipe) {
            if (level.isRainingAt(getBlockPos().above())) {
                return true;
            } else {
                recipeIndexRotten--;
            }
        }

        sendData();
        return false;
    }

    private List<? extends Recipe<?>> getRecipes() {
        Predicate<Recipe<?>> types;

        types = RecipeConditions.isOfType(AllRecipeTypes.DRYING_RACK.getType());

        List<Recipe<?>> startedSearch = RecipeFinder.get(dryingRackRecipesKey, level, types);
        return startedSearch.stream()
                .filter(RecipeConditions.outputMatchesFilter(filtering))
                .filter(RecipeConditions.firstIngredientMatches(inventory.getStackInSlot(0)))
                .filter(r -> !AllRecipeTypes.isManualRecipe(r))
                .collect(Collectors.toList());
    }

    private List<? extends Recipe<?>> getRecipesRotten() {
        Predicate<Recipe<?>> types;

        types = RecipeConditions.isOfType(AllRecipeTypes.DRYING_RACK_ROTTEN.getType());

        List<Recipe<?>> startedSearch = RecipeFinder.get(dryingRackRottenRecipesKey, level, types);
        return startedSearch.stream()
                .filter(RecipeConditions.outputMatchesFilter(filtering))
                .filter(RecipeConditions.firstIngredientMatches(inventory.getStackInSlot(0)))
                .filter(r -> !AllRecipeTypes.isManualRecipe(r))
                .collect(Collectors.toList());
    }

}
