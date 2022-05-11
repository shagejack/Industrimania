package shagejack.industrimania.content.world.nature.mulberry.silkworm;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.foundation.tileEntity.SmartTileEntity;
import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;
import shagejack.industrimania.registers.AllTileEntities;

import java.util.List;

public class SilkwormRearingBoxTileEntity extends SmartTileEntity {

    SilkwormRearingBoxTank tank;
    SilkwormRearingBoxInventory inventory;

    private double humidity;
    private double temperature;
    private double dirtiness;
    private boolean lime;

    public SilkwormRearingBoxTileEntity(BlockPos pos, BlockState state) {
        super(AllTileEntities.silkworm_rearing_box.get(), pos, state);
        tank = new SilkwormRearingBoxTank(1000);
        inventory = new SilkwormRearingBoxInventory();
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tank.writeToNBT(tag);
        tag.put("Inventory", inventory.serializeNBT());

        tag.putDouble("Humidity", humidity);
        tag.putDouble("Temperature", temperature);
        tag.putDouble("Dirtiness", dirtiness);
        tag.putBoolean("Lime", lime);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        tank.readFromNBT(tag);
        inventory.deserializeNBT(tag.getCompound("Inventory"));

        humidity = tag.getDouble("Humidity");
        temperature = tag.getDouble("Temperature");
        dirtiness = tag.getDouble("Dirtiness");
        lime = tag.getBoolean("Lime");
    }
}
