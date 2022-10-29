package shagejack.industrimania.content.primalAge.block.simpleCraftingTable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec2;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.registries.AllTileEntities;

import java.util.List;
import java.util.Optional;

public class SimpleCraftingTableTileEntity extends SmartTileEntity {

    public final int REQUIRED_PROGRESSION = 16;

    public final int REQUIRED_CRAFTING_TICK = 80;

    public float[] itemRotation = {0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F};
    public int[] itemJump = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    public int[] itemJumpPrev = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    public boolean hit;
    public int craftingTick;

    public int idle;

    public SimpleCraftingTableInventory inventory;
    public CraftingContainer craftingMatrix;

    public SimpleCraftingTableTileEntity(BlockPos pos, BlockState state) {
        this(AllTileEntities.simple_crafting_table.get(), pos, state);
    }

    public SimpleCraftingTableTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inventory = new SimpleCraftingTableInventory();
        craftingMatrix = new CraftingContainer(new SimpleCraftingTableMenu(), 3, 3);
        idle = 0;
        craftingTick = -1;
    }

    @Override
    public void tick() {
        super.tick();
        if (idle > 0)
            idle --;

        if (level.isClientSide())
            itemJump = itemJumpPrev;

        if (isHit()) {
            if (level.isClientSide()) {
                for(int i = 0; i <= 8; i++) {
                    itemJump[i] = 1 + level.getRandom().nextInt(5);
                    itemRotation[i] = (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 15F;
                }
            }
            if (!level.isClientSide())
                level.playSound((Player) null, getBlockPos(), SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 1F, 0.75F);
            this.hit = false;
        }

        if (!isHit()) {
            if (level.isClientSide()) {
                for (int i = 0; i <= 8; i++) {
                    if (itemJump[i] > 0)
                        itemJump[i] --;
                }
            }
        }

        if (craftingTick == -1)
            return;

        ItemStack result = craftResult();
        if (result.isEmpty() && !level.isClientSide()) {
            resetProgression();
            return;
        }

        if (craftingTick > 0)
            craftingTick --;

        if (craftingTick == 0) {
            craft(result);
        }

    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.put("Inventory", inventory.serializeNBT());
        tag.putInt("Idle", idle);
        tag.putBoolean("Hit", hit);
        tag.putInt("CraftingTick", craftingTick);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        idle = tag.getInt("Idle");
        hit = tag.getBoolean("Hit");
        craftingTick = tag.getInt("CraftingTick");
    }

    public void resetProgression() {
        this.craftingTick = -1;
        this.inventory.progression = 0;
        this.idle = 0;
        sendData();
    }

    public void addProgression() {

        if (craftingTick != -1)
            return;

        ItemStack result = craftResult();
        if (result.isEmpty()) {
            resetProgression();
            return;
        }

        if (this.inventory.progression < REQUIRED_PROGRESSION) {
            this.inventory.progression += 1;
        } else {
            resetProgression();
            setCrafting();
        }

        setIdle(10);

        sendData();
    }

    public ItemStack craftResult() {

        for (int i = 0; i < inventory.getSlots(); i++) {
            craftingMatrix.setItem(i, inventory.getStackInSlot(i));
            craftingMatrix.setChanged();
        }

        if (hasLevel() && level.getServer() != null) {
            Optional<CraftingRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingMatrix, level);

            if (optional.isPresent()) {
                CraftingRecipe craftingrecipe = optional.get();
                return craftingrecipe.getResultItem().copy();
            }
        }

        return ItemStack.EMPTY;
    }

    public void setCrafting() {
        this.craftingTick = REQUIRED_CRAFTING_TICK;
        sendData();
    }

    public void craft(ItemStack stack) {
        resetProgression();
        inventory.clear();
        dropItem(stack);
        sendData();
    }

    public void setIdle(int idle){
        this.idle = idle;
        sendData();
    }

    public boolean isIdling() {
        return idle > 0;
    }

    public void hit() {
        if (!isIdling() && craftingTick == -1) {
            this.hit = true;
            addProgression();
            idle = 5;
        }
    }

    public boolean isHit() {
        return hit;
    }

    public void dropItem(ItemStack stack) {
        assert level != null;
        float f = EntityType.ITEM.getHeight() / 2.0F;
        double d0 = (double)((float)getBlockPos().getX() + 0.5F) + Mth.nextDouble(level.random, -0.25D, 0.25D);
        double d1 = (double)((float)getBlockPos().getY() + 1.2F) + Mth.nextDouble(level.random, -0.25D, 0.25D) - (double)f;
        double d2 = (double)((float)getBlockPos().getZ() + 0.5F) + Mth.nextDouble(level.random, -0.25D, 0.25D);
        ItemEntity item = new ItemEntity(level, d0, d1, d2, stack);
        item.setDefaultPickUpDelay();
        level.addFreshEntity(item);
    }

    public Direction getDirection() {
        return getBlockState().isAir() ? null : getBlockState().getValue(BlockStateProperties.FACING);
    }

    public Vec2 getItemRenderPos(int slot) {
        float x = 0;
        float z = 0;

        if (slot == 4) {
            x = 0.5f;
            z = 0.5f;
            return new Vec2(x, z);
        }

        Direction direction = getDirection();

        if (direction == Direction.NORTH)
            switch (slot) {
                case 0 -> {x = 0.7725f; z = 0.7725f;}
                case 1 -> {x = 0.5f; z = 0.7725f;}
                case 2 -> {x = 0.2275f; z = 0.7725f;}
                case 3 -> {x = 0.7725f; z = 0.5f;}
                case 5 -> {x = 0.2275f; z = 0.5f;}
                case 6 -> {x = 0.7725f; z = 0.2275f;}
                case 7 -> {x = 0.5f; z = 0.2275f;}
                case 8 -> {x = 0.2275f; z = 0.2275f;}
            }

        if (direction == Direction.EAST)
            switch (slot) {
                case 0 -> {x = 0.2275f; z = 0.7725f;}
                case 1 -> {x = 0.2275f; z = 0.5f;}
                case 2 -> {x = 0.2275f; z = 0.2275f;}
                case 3 -> {x = 0.5f; z = 0.7725f;}
                case 5 -> {x = 0.5f; z = 0.2275f;}
                case 6 -> {x = 0.7725f; z = 0.7725f;}
                case 7 -> {x = 0.7725f; z = 0.5f;}
                case 8 -> {x = 0.7725f; z = 0.2275f;}
            }

        if (direction == Direction.WEST)
            switch (slot) {
                case 0 -> {x = 0.7725f; z = 0.2275f;}
                case 1 -> {x = 0.7725f; z = 0.5f;}
                case 2 -> {x = 0.7725f; z = 0.7725f;}
                case 3 -> {x = 0.5f; z = 0.2275f;}
                case 5 -> {x = 0.5f; z = 0.7725f;}
                case 6 -> {x = 0.2275f; z = 0.2275f;}
                case 7 -> {x = 0.2275f; z = 0.5f;}
                case 8 -> {x = 0.2275f; z = 0.7725f;}
            }

        if (direction == Direction.SOUTH)
            switch (slot) {
                case 0 -> {x = 0.2275f; z = 0.2275f;}
                case 1 -> {x = 0.5f; z = 0.2275f;}
                case 2 -> {x = 0.7725f; z = 0.2275f;}
                case 3 -> {x = 0.2275f; z = 0.5f;}
                case 5 -> {x = 0.7725f; z = 0.5f;}
                case 6 -> {x = 0.2275f; z = 0.7725f;}
                case 7 -> {x = 0.5f; z = 0.7725f;}
                case 8 -> {x = 0.7725f; z = 0.7725f;}
            }

        return new Vec2(x, z);
    }

}
