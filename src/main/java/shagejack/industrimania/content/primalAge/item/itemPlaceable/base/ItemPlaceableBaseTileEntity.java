package shagejack.industrimania.content.primalAge.item.itemPlaceable.base;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.registers.AllTileEntities;

import java.util.Arrays;
import java.util.List;

public class ItemPlaceableBaseTileEntity extends SmartTileEntity {

    public final int MAX_STORAGE = 4;

    String[] items = new String[MAX_STORAGE];

    @Override
    public void tick() {
        if (items == null || Arrays.stream(items).allMatch((item) -> item == null || item.isEmpty())) {
            level.removeBlock(getBlockPos(), true);
            this.onBreak(level);
        }
    }

    public ItemPlaceableBaseTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.item_placeable.get(), pos, state);
    }

    public ItemPlaceableBaseTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {}

    public void onBreak(Level level) {
        for (int i = 0; i < MAX_STORAGE; i++) {
            ItemStack stack = getItemStackFromRegistryName(removeItem());
            if (stack != null) {
                dropItem(level, getBlockPos(), stack);
            }
        }
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {

        for (int i = 0; i < MAX_STORAGE; i++) {
            if (items[i] != null) {
                nbt.putString("item" + i, items[i]);
            }
        }

    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {

        for (int i = 0; i < MAX_STORAGE; i++) {
            items[i] = nbt.getString("item" + i);
        }

    }

    public void dropItem(Level level, BlockPos pos, ItemStack stack) {
        if (!level.isClientSide && !stack.isEmpty() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && !level.restoringBlockSnapshots) {
            float f = EntityType.ITEM.getHeight() / 2.0F;
            double d0 = (double)((float)pos.getX() + 0.5F) + Mth.nextDouble(level.getRandom(), -0.25D, 0.25D);
            double d1 = (double)((float)pos.getY() + 0.5F) + Mth.nextDouble(level.getRandom(), -0.25D, 0.25D) - (double)f;
            double d2 = (double)((float)pos.getZ() + 0.5F) + Mth.nextDouble(level.getRandom(), -0.25D, 0.25D);
            ItemEntity itemEntity = new ItemEntity(level, d0, d1, d2, stack);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }
    }

    public boolean addItem(String name) {
        if (name != null && !name.isEmpty()) {
            for (int i = 0; i < items.length; i++) {
                if (items[i] == null || items[i].isEmpty()) {
                    items[i] = name;
                    return true;
                }
            }
        }
        return false;
    }

    public String removeItem() {
        for (int i = MAX_STORAGE - 1; i >= 0; i--) {
            if (items[i] != null && !items[i].isEmpty()) {
                String temp = items[i];
                items[i] = "";
                return temp;
            }
        }
        return "";
    }

    public ItemStack getItemStackFromRegistryName(String name) {
        if (name != null && !name.isEmpty()) {
            String[] temp = name.split(":");

            //Normally, this case will never not happen
            if (temp.length > 2) {
                for (int i = 2; i < temp.length; i++) {
                    temp[1] += temp[i];
                }
            }

            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(temp[0], temp[1]));
            ItemStack stack = new ItemStack(item);
            return stack;
        }
        return null;
    }

    //TODO: render based on storage

    //TODO: burn
    public void burn() {

    }

}
