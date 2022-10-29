package shagejack.industrimania.content.primalAge.item.itemPlaceable.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.foundation.utility.TileEntityUtils;
import shagejack.industrimania.foundation.utility.recipe.RecipeConditions;
import shagejack.industrimania.foundation.utility.recipe.RecipeFinder;
import shagejack.industrimania.registries.AllRecipeTypes;
import shagejack.industrimania.registries.tags.AllTags;
import shagejack.industrimania.registries.AllTileEntities;
import shagejack.industrimania.registries.block.AllBlocks;
import shagejack.industrimania.registries.item.AllItems;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ItemPlaceableBaseTileEntity extends SmartTileEntity {

    private static final Object itemPlaceableBurnRecipesKey = new Object();
    private static final int MAX_STORAGE = 16;

    ItemPlaceableInventory inventory;
    private boolean isBurning;

    private int remainingTick;
    private boolean isBurningCoal = false;
    private boolean isBurningBlock = false;

    public ItemPlaceableBaseTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.item_placeable.get(), pos, state);
        inventory = new ItemPlaceableInventory(MAX_STORAGE, this);
        inventory.whenContentsChanged($ -> this.onContentsChanged());
        isBurning = false;
        remainingTick = -1;
    }

    @Override
    public void tick() {
        if (level.isClientSide())
            return;

        if (getBlockState().getValue(ItemPlaceableBaseBlock.AMOUNT) == 0) {
            level.setBlock(getBlockPos(), getBlockState().setValue(ItemPlaceableBaseBlock.AMOUNT, inventory.getTotalItemAmount()), 3);
            if (getBlockState().getValue(ItemPlaceableBaseBlock.AMOUNT) == 0) {
                level.removeBlock(getBlockPos(), false);
                this.onBreak(level);
            }
        }

        if (inventory.isEmpty()) {
            level.removeBlock(getBlockPos(), false);
            this.onBreak(level);
        }

        checkBurn();
        if (isBurning()) {
            burnTick();
        } else {
            resetRecipe();
        }
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
    }

    public void onBreak(Level level) {
        inventory.dropInventory(level, getBlockPos());
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        nbt.put("Inventory", inventory.serializeNBT());
        nbt.putBoolean("IsBurning", isBurning);
        nbt.putBoolean("IsBurningCoal", isBurningCoal);
        nbt.putBoolean("IsBurningBlock", isBurningBlock);
        nbt.putInt("remainingTick", remainingTick);
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        inventory.deserializeNBT(nbt.getCompound("Inventory"));

        isBurning = nbt.getBoolean("IsBurning");
        isBurningCoal = nbt.getBoolean("IsBurningCoal");
        isBurningBlock = nbt.getBoolean("IsBurningBlock");
        remainingTick = nbt.getInt("remainingTick");
    }

    public void checkBurn() {
        if (!isBurning()) {
            this.isBurning = Stream.of(Direction.values()).anyMatch(direction -> {
                if (level.getBlockState(getBlockPos().relative(direction)).getBlock() instanceof ItemPlaceableBaseBlock) {
                    if (level.getBlockEntity(getBlockPos().relative(direction)) instanceof ItemPlaceableBaseTileEntity te) {
                        return te.isBurning;
                    }
                }
                return false;
            }) || level.getBlockState(getBlockPos().above()).is(BlockTags.FIRE);
        }
    }

    public boolean isBurning() {
        return isBurning;
    }

    public void burnTick() {
        if (inventory.isEmpty()) {
            sendData();
            return;
        }

        if (level.getBlockState(getBlockPos().above()).isAir()) {
            level.setBlock(getBlockPos().above(), Blocks.FIRE.defaultBlockState(), 0);
        }

        if (remainingTick == -1 && !isBurningBlock && !isBurningCoal) {
            if(checkBurningCoal()) {
                remainingTick = 18000;
                isBurningCoal = true;
            } else {
                Optional<ItemPlaceableBurnRecipe> recipe = getRecipe();
                if(recipe.isPresent()) {
                    remainingTick = recipe.get().getProcessingDuration();
                    isBurningBlock = true;
                } else {
                    return;
                }
            }
        }

        if (isBurningCoal) {
            if (remainingTick == 0) {
                finishBurningCoal();
            } else {
                if (isInContactWithAir() || !isCorrectBlockOnTop()) {
                    resetRecipe();
                    this.inventory.clear();
                    this.setRemoved();
                    if (isCorrectBlockOnTop()) {
                        Optional<ItemPlaceableBaseTileEntity> te = getItemsTileEntityAbove();
                        if (te.isPresent()) {
                            te.get().replaceItem(AllItems.hay.get(), AllItems.dust.get(), 1.0);
                            te.get().replaceItem(AllItems.mud.get(), AllItems.dust.get(), 0.5);
                        }
                    }
                    level.setBlock(getBlockPos(), AllBlocks.gravity_dust.block().get().defaultBlockState(), 0);
                    return;
                }
                remainingTick--;
            }
        }

        if (isBurningBlock) {
            if (remainingTick == 0) {
                finishBurningBlock();
            } else {
                remainingTick--;
            }
        }


    }

    public void resetRecipe() {
        remainingTick = -1;
        isBurningCoal = false;
        isBurningBlock = false;
        sendData();
    }

    @Override
    public void notifyUpdate() {

        super.notifyUpdate();
    }

    public void finishBurningCoal() {
        TagKey<Item> ingredient = AllTags.modItemTag(AllTags.IndustrimaniaTags.charcoalIngredient);
        replaceItem(ingredient, AllItems.dust.get(), 0.4);
        IntStream.range(0, MAX_STORAGE).forEach(index -> {
            ItemStack stack = inventory.getStackInSlot(index);
            if(stack.is(ingredient))
                inventory.setStackInSlot(index, new ItemStack(getCharcoalFromLog(stack.getItem())));
        });

        resetRecipe();
        sendData();
    }

    public void finishBurningBlock() {
        if (getRecipe().isPresent()) {
            if (getRecipe().get().getResultItem().getItem() instanceof BlockItem blockItem) {
                Block resultBlock = blockItem.getBlock();
                this.inventory.clear();
                this.setRemoved();
                level.setBlock(getBlockPos(), resultBlock.defaultBlockState(), 0);
            }
        }
        resetRecipe();
        sendData();
    }

    public boolean checkBurningCoal() {
        return IntStream.range(0, MAX_STORAGE).allMatch(index -> inventory.getStackInSlot(index).is(AllTags.modItemTag(AllTags.IndustrimaniaTags.charcoalIngredient)));
    }

    public Optional<ItemPlaceableBurnRecipe> getRecipe() {
        Predicate<Recipe<?>> types;

        types = RecipeConditions.isOfType(AllRecipeTypes.ITEM_PLACEABLE_BURN.getType());

        List<Recipe<?>> recipes = RecipeFinder.get(itemPlaceableBurnRecipesKey, level, types).stream()
                .filter(r -> r instanceof ItemPlaceableBurnRecipe burnRecipe && burnRecipe.matches(inventory, level))
                .toList();

        return Optional.ofNullable((ItemPlaceableBurnRecipe) recipes.get(0));
    }

    public Vec2 getItemRenderPos(int slot) {

        int layer = slot / 4 + 1;
        int row = slot % 4 + 1;

        float x = row * 0.25f + 0.25f;
        float y = layer * 0.25f + 0.25f;

        return new Vec2(x, y);
    }

    public void replaceItem(Item from, Item to, double chance) {
        IntStream.range(0, MAX_STORAGE).forEach(index -> {
            if(inventory.getStackInSlot(index).is(from) && level.getRandom().nextDouble() < chance)
                inventory.setStackInSlot(index, new ItemStack(to));
        });
    }

    public void replaceItem(TagKey<Item> from, Item to, double chance) {
        IntStream.range(0, MAX_STORAGE).forEach(index -> {
            if(inventory.getStackInSlot(index).is(from) && level.getRandom().nextDouble() < chance)
                inventory.setStackInSlot(index, new ItemStack(to));
        });
    }

    public boolean isLayerFullOf(Item item, int layer) {
        return IntStream.range(layer * 4 - 4, layer * 4).allMatch(index -> inventory.getStackInSlot(index).is(item));
    }

    public boolean isLayerFullOf(TagKey<Item> tag, int layer) {
        return IntStream.range(layer * 4 - 4, layer * 4).allMatch(index -> inventory.getStackInSlot(index).is(tag));
    }

    public Optional<ItemPlaceableBaseTileEntity> getItemsTileEntityAbove() {
        return TileEntityUtils.get(ItemPlaceableBaseTileEntity.class, level, worldPosition.above());
    }

    public Optional<ItemPlaceableInventory> getItemsAbove() {
        return getItemsTileEntityAbove().flatMap(te -> Optional.of(te.inventory));
    }

    public boolean isInContactWithAir() {
        return Stream.of(Direction.values()).anyMatch(direction -> {
            var state = level.getBlockState(worldPosition.relative(direction));
            return state.isAir() || state.is(AllBlocks.gravity_dust.block().get());
        });
    }

    public boolean isCorrectBlockOnTop() {
        return level.getBlockState(worldPosition.above()).getBlock() instanceof ItemPlaceableBaseBlock && getItemsTileEntityAbove().isPresent() && getItemsTileEntityAbove().get().isCharcoalCover();
    }

    public boolean isCharcoalCover() {
        return isLayerFullOf(AllItems.hay.get(), 1) && isLayerFullOf(AllItems.hay.get(), 2) && isLayerFullOf(AllItems.mud.get(), 3) && isLayerFullOf(AllTags.modItemTag(AllTags.IndustrimaniaTags.rockPiece), 4);
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

    public void onContentsChanged() {
        resetRecipe();
    }

    private static Item getCharcoalFromLog(Item log) {
        if (log == AllItems.logOak.get())
            return AllItems.charcoalMedium.get();
        if (log == AllItems.logBirch.get())
            return Items.CHARCOAL;
        if (log == AllItems.logSpruce.get())
            return AllItems.charcoalGood.get();
        if (log == AllItems.logJungle.get())
            return AllItems.charcoalGood.get();
        if (log == AllItems.logAcacia.get())
            return AllItems.charcoalGood.get();
        if (log == AllItems.logDarkOak.get())
            return AllItems.charcoalMedium.get();
        if (log == AllItems.logMulberry.get())
            return Items.CHARCOAL;
        if (log == AllItems.logRubber.get())
            return AllItems.charcoalGood.get();
        if (log == AllItems.woodOak.get())
            return AllItems.charcoalMedium.get();
        if (log == AllItems.woodBirch.get())
            return Items.CHARCOAL;
        if (log == AllItems.woodSpruce.get())
            return AllItems.charcoalGood.get();
        if (log == AllItems.woodJungle.get())
            return AllItems.charcoalExcellent.get();
        if (log == AllItems.woodAcacia.get())
            return AllItems.charcoalGood.get();
        if (log == AllItems.woodDarkOak.get())
            return AllItems.charcoalMedium.get();
        if (log == AllItems.woodMulberry.get())
            return Items.CHARCOAL;
        if (log == AllItems.woodRubber.get())
            return AllItems.charcoalGood.get();
        return Items.CHARCOAL;
    }


}
