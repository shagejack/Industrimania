package shagejack.industrimania.content.primalAge.item.itemPlaceable.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import shagejack.industrimania.content.primalAge.block.dryingRack.DryingRackFilterSlot;
import shagejack.industrimania.content.primalAge.block.dryingRack.DryingRackRecipe;
import shagejack.industrimania.content.primalAge.block.dryingRack.DryingRackRottenRecipe;
import shagejack.industrimania.foundation.item.ItemHelper;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.foundation.tileEntity.behaviour.filtering.FilteringBehaviour;
import shagejack.industrimania.foundation.utility.recipe.RecipeConditions;
import shagejack.industrimania.foundation.utility.recipe.RecipeFinder;
import shagejack.industrimania.registers.AllRecipeTypes;
import shagejack.industrimania.registers.AllTileEntities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemPlaceableBaseTileEntity extends SmartTileEntity {

    private final int MAX_STORAGE = 16;

    private static final Object itemPlaceableRecipesKey = new Object();

    ItemPlaceableInventory inventory;
    private boolean isBurning;
    private int recipeIndex;

    public ItemPlaceableBaseTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.item_placeable.get(), pos, state);
        inventory = new ItemPlaceableInventory(MAX_STORAGE);
        isBurning = false;
        recipeIndex = 0;
        inventory.remainingTime = -1;
    }

    @Override
    public void tick() {
        assert level != null;

        if (getBlockState().getValue(ItemPlaceableBaseBlock.AMOUNT) == 0) {
            level.setBlock(getBlockPos(), getBlockState().setValue(ItemPlaceableBaseBlock.AMOUNT, inventory.getTotalItemAmount()), 3);
            if (getBlockState().getValue(ItemPlaceableBaseBlock.AMOUNT) == 0) {
                level.removeBlock(getBlockPos(), true);
                this.onBreak(level);
            }
        }

        if (inventory.isEmpty()) {
            level.removeBlock(getBlockPos(), true);
            this.onBreak(level);
        }
        checkBurn();
        if (isBurning) {
            burnTick();
        } else {
            resetRecipe();
        }
    }

    public boolean addItem(ItemStack stack) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                inventory.insertItem(i, stack, false);
                resetRecipe();
                level.setBlock(getBlockPos(), getBlockState().setValue(ItemPlaceableBaseBlock.AMOUNT, inventory.getTotalItemAmount()), 3);
                sendData();
                return true;
            }
        }
        return false;
    }

    public ItemStack removeItem() {
        for (int i = inventory.getSlots() - 1; i >= 0; i--) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                inventory.setStackInSlot(i, ItemStack.EMPTY);
                resetRecipe();
                level.setBlock(getBlockPos(), getBlockState().setValue(ItemPlaceableBaseBlock.AMOUNT, inventory.getTotalItemAmount()), 3);
                sendData();
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
    }

    public void onBreak(Level level) {
        inventory.dropInventory(level, getBlockPos());
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        if (inventory.isEmpty()) {
            nbt.put("Inventory", inventory.serializeNBT());
        }

        nbt.putBoolean("IsBurning", isBurning);
        nbt.putInt("RecipeIndex", recipeIndex);
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        inventory.deserializeNBT(nbt);

        isBurning = nbt.getBoolean("IsBurning");
        recipeIndex = nbt.getInt("RecipeIndex");
    }

    public void checkBurn() {
        assert level != null;
        this.isBurning = level.getBlockState(getBlockPos().above()).is(Blocks.FIRE);
    }

    public boolean isBurning() {
        return isBurning;
    }

    public void resetRecipe() {
        recipeIndex = 0;
        inventory.recipeDuration = 20;
        inventory.remainingTime = -1;
        inventory.appliedRecipe = false;
        sendData();
    }

    public void burnTick() {
        if (inventory.isEmpty()) {
            inventory.remainingTime = -1;
            inventory.appliedRecipe = false;
            sendData();
            return;
        }

        if (inventory.remainingTime == -1) {
            start();
            sendData();
            return;
        }

        inventory.remainingTime -= 1;

        if (inventory.remainingTime < 5 && !inventory.appliedRecipe) {
            assert level != null;
            if (level.isClientSide && !isVirtual())
                return;

            if (inventory.isEmpty())
                return;

            applyRecipe();

            inventory.appliedRecipe = true;
            inventory.recipeDuration = 20;
            inventory.remainingTime = -1;
            sendData();
        }


    }

    public void start() {
        assert level != null;
        if (level.isClientSide && !isVirtual())
            return;

        if (inventory.isEmpty()) {
            inventory.remainingTime = -1;
            inventory.appliedRecipe = false;
            sendData();
            return;
        }

        List<? extends Recipe<?>> recipes = getRecipes();
        boolean valid = !recipes.isEmpty();
        int time;

        if (!valid) {
            inventory.remainingTime = -1;
            inventory.appliedRecipe = false;
            sendData();
            return;
        }

        recipeIndex++;
        if (recipeIndex >= recipes.size() || recipeIndex < 0)
            recipeIndex = 0;


        Recipe<?> recipe = recipes.get(recipeIndex);

        if (recipe instanceof ItemPlaceableBurnRecipe) {
            time = ((ItemPlaceableBurnRecipe) recipe).getProcessingDuration();
            inventory.remainingTime = time;
            inventory.recipeDuration = time;
            inventory.appliedRecipe = false;
        }

        sendData();
    }

    public void applyRecipe() {

        List<? extends Recipe<?>> recipes = getRecipes();
        if (recipes.isEmpty())
            return;

        if (recipeIndex >= recipes.size())
            recipeIndex = 0;

        Recipe<?> recipe = recipes.get(recipeIndex);

        inventory.clear();

        List<ItemStack> list = new ArrayList<>();
        List<ItemStack> results = new LinkedList<>();

        if (recipe instanceof ItemPlaceableBurnRecipe) {
            results = ((ItemPlaceableBurnRecipe) recipe).rollResults();
        }

        for (ItemStack stack : results) {
            ItemHelper.addToList(stack, list);
        }

        for (int slot = 0; slot < list.size() && slot < inventory.getSlots(); slot++) {
            inventory.setStackInSlot(slot, list.get(slot));
        }

        sendData();
    }



    private List<? extends Recipe<?>> getRecipes() {
        Predicate<Recipe<?>> types;

        types = RecipeConditions.isOfType(AllRecipeTypes.DRYING_RACK.getType());

        NonNullList<ItemStack> stacks = NonNullList.create();
        
        for (int i = 0; i < inventory.getSlots(); i++) {
            stacks.add(inventory.getStackInSlot(i));
        }

        List<Recipe<?>> startedSearch = RecipeFinder.get(itemPlaceableRecipesKey, level, types);
        return startedSearch.stream()
                .filter(RecipeConditions.ingredientsMatches(stacks))
                .filter(r -> !AllRecipeTypes.isManualRecipe(r))
                .collect(Collectors.toList());
    }

}
