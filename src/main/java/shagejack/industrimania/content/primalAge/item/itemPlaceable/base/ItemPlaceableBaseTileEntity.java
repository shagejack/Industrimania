package shagejack.industrimania.content.primalAge.item.itemPlaceable.base;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.registers.AllTileEntities;

import java.util.List;

public class ItemPlaceableBaseTileEntity extends SmartTileEntity {

    public final int MAX_STORAGE = 4;

    String[] items = new String[MAX_STORAGE];


    public ItemPlaceableBaseTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.item_placeable.get(), pos, state);
    }

    public ItemPlaceableBaseTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {}

    public void onBreak(Level level) {

    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {

        for (int i = 0; i < MAX_STORAGE; i++) {
            nbt.putString("item" + i, items[i]);
        }

    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {

        for (int i = 0; i < MAX_STORAGE; i++) {
            items[i] = nbt.getString("item" + i);
        }

    }

    public boolean addItem(String name) {
        for (String item : items) {
            if (item.isEmpty()) {
                item = name;
                return true;
            }
        }
        return false;
    }

    public String removeItem() {
        for (int i = MAX_STORAGE - 1; i >= 0; i--) {
            if (!items[i].isEmpty()) {
                String temp = items[i];
                items[i] = "";
                return temp;
            }
        }
        return "";
    }

    //TODO: render based on storage
}
