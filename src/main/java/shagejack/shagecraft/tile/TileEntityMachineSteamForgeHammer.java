package shagejack.shagecraft.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.server.timings.TimeTracker;
import shagejack.shagecraft.data.Inventory;
import shagejack.shagecraft.data.inventory.FuelSlot;
import shagejack.shagecraft.data.inventory.Slot;
import shagejack.shagecraft.data.tank.ShageFluidTank;
import shagejack.shagecraft.machines.MachineNBTCategory;
import shagejack.shagecraft.machines.events.MachineEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class TileEntityMachineSteamForgeHammer extends ShageTileEntityMachineSteam implements ISidedInventory {
    private static final double REQUIRED_POWER = 3.2; //Unit: KW
    private static final double REQUIRED_WORK = 2; //Unit: KJ
    private static final Random random = new Random();
    private final TimeTracker time;
    private final double STEAM_CAPACITY = 500;

    private int idle;

    public TileEntityMachineSteamForgeHammer() {
        super();

        this.steamStorage.setCapacity(STEAM_CAPACITY);
        this.steamStorage.setMaxReceive(STEAM_CAPACITY);
        this.steamStorage.setMaxExtract(STEAM_CAPACITY);

        time = new TimeTracker();
        playerSlotsMain = true;
        playerSlotsHotbar = true;
    }

    @Override
    public void update() {
        super.update();
        manageSteamStored();
        manageWork();
    }

    public void manageSteamStored() {

        double[] properties = steamStorage.mergeProperties();

        //TODO: Cooling
        if (properties[0] > 0 && properties[2] != 0) {
            if (properties[1] > 373.15) {
                properties[1] -= 0.01;
            } else if (properties[0] > 0){
                properties[0] = 0;
            }
        }

        if (properties[0] <= 0) properties[2] = 0;

        steamStorage.setProperties(properties);

        if (steamStorage.isExceededCapacity()){
            world.createExplosion(null, getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, (float) (3.0 * steamStorage.getSteamPressure()), false);
            world.setBlockToAir(getPos());
        }

    }

    public void manageWork() {
        if(idle > 0) {
            idle --;
        } else {
            if (isSteamEnoughForWork()) {
                //Work
                doWork();
                //Release Steam
                steamStorage.releaseSteam();
                idle = 100;
            }
        }
    }

    public boolean isSteamEnoughForWork() {
        if (steamStorage.getSteamState() != 0) {
            if (steamStorage.getCurrentActualPower(0.15) > REQUIRED_POWER && steamStorage.getCurrentActualWork(0.15) > REQUIRED_WORK) {
                return true;
            }
        }
        return false;
    }

    public void doWork() {

    }

    @Override
    public SoundEvent getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    public float soundVolume() {
        return 0.3f;
    }

    public boolean isProducing() {
        return false;
    }

    @Override
    public boolean getServerActive() {
        return isProducing();
    }

    @Override
    protected void onMachineEvent(MachineEvent event) {

    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        if (categories.contains(MachineNBTCategory.INVENTORY) && toDisk) {
            inventory.writeToNBT(nbt, true);
        }


        nbt.setInteger("idle", idle);
        super.writeCustomNBT(nbt, categories, toDisk);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.INVENTORY)) {
            inventory.readFromNBT(nbt);
        }


        idle = nbt.getInteger("idle");
        super.readCustomNBT(nbt, categories);
    }

}
