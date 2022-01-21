package shagejack.industrimania.content.primalAge.block.dryingRack;

import com.google.common.base.Suppliers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import shagejack.industrimania.content.contraptions.processing.ProcessingInventory;
import shagejack.industrimania.foundation.item.ItemHelper;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.foundation.tileEntity.behaviour.filtering.FilteringBehaviour;
import shagejack.industrimania.foundation.utility.recipe.RecipeConditions;
import shagejack.industrimania.foundation.utility.recipe.RecipeFinder;
import shagejack.industrimania.registers.AllRecipeTypes;
import shagejack.industrimania.registers.AllTileEntities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DryingRackTileEntity extends SmartTileEntity {

    private static final AABB RENDER_BOX = new AABB(0, 0, 0, 1, 1, 1);

    private static final Object dryingRackRecipesKey = new Object();
    public ProcessingInventory inventory;
    private final LazyOptional<IItemHandler> invProvider;
    private int recipeIndex;
    private FilteringBehaviour filtering;
    private boolean processingRotten;

    private ItemStack playEvent;

    public DryingRackTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.bronze_tube.get(), pos, state);
        inventory = new ProcessingInventory(this::start).withSlotLimit(true);
        inventory.remainingTime = -1;
        invProvider = LazyOptional.of(() -> inventory);
        playEvent = ItemStack.EMPTY;
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
        compound.putBoolean("ProcessingRotten", processingRotten);
        super.write(compound, clientPacket);

        if (!clientPacket || playEvent.isEmpty())
            return;
        compound.put("PlayEvent", playEvent.serializeNBT());
        playEvent = ItemStack.EMPTY;
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inventory.deserializeNBT(compound.getCompound("Inventory"));
        recipeIndex = compound.getInt("RecipeIndex");
        processingRotten = compound.getBoolean("ProcessingRotten");
        if (compound.contains("PlayEvent"))
            playEvent = ItemStack.of(compound.getCompound("PlayEvent"));
    }

    protected AABB makeRenderBoundingBox() {
        return RENDER_BOX.inflate(.125f)
                .move(worldPosition);
    }

    @Override
    public void tick() {
        super.tick();

        if (inventory.remainingTime == -1) {
            if (!inventory.isEmpty() && !inventory.appliedRecipe)
                start(inventory.getStackInSlot(0));
            return;
        }

        inventory.remainingTime -= 1;

        if (inventory.remainingTime < 5 && !inventory.appliedRecipe) {
            if (level.isClientSide && !isVirtual())
                return;
            playEvent = inventory.getStackInSlot(0);
            applyRecipe();
            inventory.appliedRecipe = true;
            inventory.recipeDuration = 20;
            inventory.remainingTime = 20;
            sendData();
            return;
        }

    }

    private void applyRecipe() {
        List<? extends Recipe<?>> recipes = getRecipes();
        if (recipes.isEmpty())
            return;
        if (recipeIndex >= recipes.size())
            recipeIndex = 0;

        Recipe<?> recipe = recipes.get(recipeIndex);

        int rolls = inventory.getStackInSlot(0)
                .getCount();
        inventory.clear();

        List<ItemStack> list = new ArrayList<>();
        for (int roll = 0; roll < rolls; roll++) {
            List<ItemStack> results = new LinkedList<ItemStack>();
            if (recipe instanceof DryingRackRecipe) {
                results = ((DryingRackRecipe) recipe).rollResults();
            } else if (recipe instanceof DryingRackRottenRecipe) {
                results.add(recipe.getResultItem()
                        .copy());
            }

            for (int i = 0; i < results.size(); i++) {
                ItemStack stack = results.get(i);
                ItemHelper.addToList(stack, list);
            }
        }
        for (int slot = 0; slot < list.size() && slot < inventory.getSlots(); slot++) {
            inventory.setStackInSlot(slot, list.get(slot));
        }

    }

    public void onRemove(Level level, BlockPos pos, BlockState oldState) {

    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != Direction.DOWN)
            return invProvider.cast();
        return super.getCapability(cap, side);
    }

    public void start(ItemStack inserted) {
        if (inventory.isEmpty())
            return;
        if (level.isClientSide && !isVirtual())
            return;

        List<? extends Recipe<?>> recipes = getRecipes();
        boolean valid = !recipes.isEmpty();
        int time = 1200;

        if (recipes.isEmpty()) {
            inventory.remainingTime = inventory.recipeDuration = 10;
            inventory.appliedRecipe = false;
            sendData();
            return;
        }

        if (valid) {
            recipeIndex++;
            if (recipeIndex >= recipes.size())
                recipeIndex = 0;
        }

        Recipe<?> recipe = recipes.get(recipeIndex);

        if (recipe instanceof DryingRackRecipe) {
            time = ((DryingRackRecipe) recipe).getProcessingDuration();
        } else if (recipe instanceof DryingRackRottenRecipe) {
            time = ((DryingRackRottenRecipe) recipe).getProcessingDuration();
        }

        inventory.remainingTime = time;
        inventory.recipeDuration = inventory.remainingTime;
        inventory.appliedRecipe = false;
        sendData();
    }

    private List<? extends Recipe<?>> getRecipes() {
        Predicate<Recipe<?>> types;
        if (!level.isRaining() && level.getRandom().nextDouble() > 0.0001 && !processingRotten) {
            types = RecipeConditions.isOfType(AllRecipeTypes.DRYING_RACK.getType());
        } else {
            types = RecipeConditions.isOfType(AllRecipeTypes.DRYING_RACK_ROTTEN.getType());
            processingRotten = true;
        }

        List<Recipe<?>> startedSearch = RecipeFinder.get(dryingRackRecipesKey, level, types);
        return startedSearch.stream()
                .filter(RecipeConditions.outputMatchesFilter(filtering))
                .filter(RecipeConditions.firstIngredientMatches(inventory.getStackInSlot(0)))
                .filter(r -> !AllRecipeTypes.isManualRecipe(r))
                .collect(Collectors.toList());
    }

}
