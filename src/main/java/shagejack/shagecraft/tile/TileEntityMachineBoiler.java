package shagejack.shagecraft.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.data.Inventory;
import shagejack.shagecraft.data.SteamStorage;
import shagejack.shagecraft.data.inventory.FuelSlot;
import shagejack.shagecraft.data.inventory.Slot;
import shagejack.shagecraft.data.tank.ShageFluidTank;
import shagejack.shagecraft.data.transport.IFluidPipe;
import shagejack.shagecraft.init.ShagecraftCapabilities;
import shagejack.shagecraft.machines.MachineNBTCategory;
import shagejack.shagecraft.machines.events.MachineEvent;
import shagejack.shagecraft.network.packet.client.PacketSteamUpdate;
import shagejack.shagecraft.util.TimeTracker;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class TileEntityMachineBoiler extends ShageTileEntityMachineSteam implements ISidedInventory {
    public static final int STEAM_EXTRACT_SPEED = 32;
    private static final Random random = new Random();
    private final TimeTracker time;
    private final double STEAM_CAPACITY = 100;

    private final ShageFluidTank tank;
    public int INPUT_SLOT_ID;
    public int OUTPUT_SLOT_ID;

    private double temperature;

    public TileEntityMachineBoiler() {
        super();

        this.steamStorage.setCapacity(STEAM_CAPACITY);
        this.steamStorage.setMaxReceive(STEAM_CAPACITY);
        this.steamStorage.setMaxExtract(STEAM_CAPACITY);

        this.tank = new ShageFluidTank(4000);
        this.tank.setTileEntity(this);
        time = new TimeTracker();
        playerSlotsMain = true;
        playerSlotsHotbar = true;
    }

    @Override
    protected void RegisterSlots(Inventory inventory) {
        INPUT_SLOT_ID = inventory.AddSlot(new FuelSlot(true));
        OUTPUT_SLOT_ID = inventory.AddSlot(new Slot(false));
        super.RegisterSlots(inventory);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        tank.onContentsChanged();
    }

    @Override
    public void onAdded(World world, BlockPos pos, IBlockState state) {
        temperature = 298.15;
        super.onAdded(world, pos, state);
    }

    @Override
    public void update() {
        super.update();
        //this.manageSteamStored();
        this.manageProduce();
        //this.manageExtract();
        if (!world.isRemote) {
            updateClientSteam();
        }
    }

    public void manageSteamStored() {

        if(!steamStorage.hasSteam()) return;

        double[] properties = steamStorage.mergeProperties();

        //TODO: Cooling
        if (temperature < properties[1]) {
            if (properties[0] > 0 && properties[2] != 0) {
                if (properties[1] > 373.15) {
                    properties[1] -= 0.01;
                } else if (properties[0] > 0) {
                    properties[0] = 0;
                }
            }
        }

        if (properties[0] <= 0) properties[2] = 0;

        steamStorage.setProperties(properties);

        if (steamStorage.isExceededCapacity()){
            world.createExplosion(null, getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, (float) (5.0 * steamStorage.getSteamPressure()), false);
            world.setBlockToAir(getPos());
        }

    }

    public void manageProduce() {
        double[] produce = new double[3];
        double heat = 0;

        List<FluidStack> waterStack = new ArrayList<>();

        waterStack.add(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME));

        if(FluidRegistry.getFluid("watersurfacecontaminated") != null) {
            waterStack.add(new FluidStack(FluidRegistry.getFluid("watersurfacecontaminated"), Fluid.BUCKET_VOLUME));
            waterStack.add(new FluidStack(FluidRegistry.getFluid("watermineralcontaminated"), Fluid.BUCKET_VOLUME));
            waterStack.add(new FluidStack(FluidRegistry.getFluid("wateroceancontaminated"), Fluid.BUCKET_VOLUME));
        }



        if (temperature > 393.15) {

            if (temperature > steamStorage.getSteamTemp()) {
                heat = temperature / 100;
                temperature -= temperature / 100;
            }

            if(tank.getFluid() != null) {
                boolean flag = false;
                for (int i = 0; i < waterStack.size(); i++) {
                    if (tank.getFluid().isFluidEqual(waterStack.get(i))) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    if (tank.getFluidAmount() >= 1) {
                        produce[0] = 0.001;
                        produce[1] = 373.15 + Math.pow(temperature - 373.15, 0.5);
                        produce[2] = 1;
                        temperature -= 0.01;
                        tank.drain(1, true);
                    }
                } else if (temperature / 10000 > Math.random()) {
                    world.createExplosion(null, getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, 5.0F, true);
                    world.setBlockToAir(getPos());
                }
            } else if (temperature / 10000 > Math.random()) {
                world.createExplosion(null, getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, 5.0F, true);
                world.setBlockToAir(getPos());
            }

        } else if (temperature > 298.15) {
            temperature -= 0.2;
        }

        //if (steamStorage.getSteamMass() * steamStorage.getSteamPressure() < steamStorage.getCapacity()) {
        this.steamStorage.setProperties(managePropertiesFromBoilerProduce(steamStorage.mergeProperties(), produce));
        //}

        this.steamStorage.setProperties(managePropertiesFromBoilerHeat(steamStorage.mergeProperties(), heat));

    }

    public void manageExtract() {
        if (!world.isRemote) {
            if (time.hasDelayPassed(world, STEAM_EXTRACT_SPEED)) {
                if (steamStorage.hasSteam()) {
                    List<EnumFacing> faces = new ArrayList<>();
                    for (EnumFacing dir : EnumFacing.VALUES) {
                        TileEntity e = world.getTileEntity(getPos().offset(dir));
                        EnumFacing opposite = dir.getOpposite();
                        if (e != null && e.hasCapability(ShagecraftCapabilities.STEAM_HANDLER, opposite)) {
                            if (steamStorage.getSteamMass() * steamStorage.getSteamPressure() < e.getCapability(ShagecraftCapabilities.STEAM_HANDLER, dir.getOpposite()).getSteamMass() * e.getCapability(ShagecraftCapabilities.STEAM_HANDLER, dir.getOpposite()).getSteamPressure()) {
                                faces.add(dir);
                            }
                        }
                    }
                    for (EnumFacing direction : faces) {
                        TileEntity handler = world.getTileEntity(getPos().offset(direction));
                        extractSteam(steamStorage.mergeProperties(), faces.size(), handler, direction);
                    }
                }
            }
        }

    }

    public void extractSteam(double[] properties, int faces, TileEntity handler, EnumFacing direction) {
        if (properties[0] * steamStorage.getSteamPressure() < handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).getSteamMass() * handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).getSteamPressure()){

            properties[0] = properties[0] / faces;

            /*
            if (properties[0] + handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).getSteamMass() > handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).getCapacity()) {
                properties[0] = handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).getCapacity() - properties[0] + handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).getSteamMass();
            }
            */

            //Transfer Steam
            double[] transferredProperties = steamStorage.mergeSteam(properties, handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).mergeProperties(), false);

            properties[0] = steamStorage.getSteamMass() - properties[0];

            steamStorage.setProperties(properties);
            handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).setProperties(transferredProperties);

            if (steamStorage.getSteamMass() <= 0) {
                steamStorage.setSteamState(0);
            }

            if (!world.isRemote) Shagecraft.NETWORK.sendToAllAround(new PacketSteamUpdate(handler), handler, 64);

        }
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

    public double[] managePropertiesFromBoilerProduce(double[] properties, double[] produce){
        if (properties.length == 3 && produce.length == 3) {
            return this.steamStorage.mergeSteam(properties, produce, false);
        }
        return properties;
    }

    public double[] managePropertiesFromBoilerHeat(double[] properties, double heat){
        if (properties.length == 3) {
            if (properties[2] == 1) {
                //Saturated Steam
                if(temperature - properties[1] > 0 && properties[1] < 873.15) {
                    properties[1] += 0.4 * heat / properties[0];
                }

                //TODO: Use timer instead of random number
                if(temperature - properties[1] > 100) {
                    if (heat > properties[1] / 2 && heat / (5000 * properties[0]) > Math.random()) {
                        properties[2] = 2;
                    }
                }

            } else if(properties[2] == 2) {
                //Overheated Steam
                if(temperature - properties[1] > 0 && properties[1] < 1473.15) {
                    properties[1] += 0.35 * heat / properties[0];
                }
                if(temperature - properties[1] < 20) {
                    if (heat < properties[1] / 3 && heat * properties[0] < Math.random() * 1.0e10) {
                        properties[2] = 1;
                    }
                }
            }
        }
        return properties;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[]{INPUT_SLOT_ID, OUTPUT_SLOT_ID};
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        if (categories.contains(MachineNBTCategory.INVENTORY) && toDisk) {
            inventory.writeToNBT(nbt, true);
        }

        tank.writeToNBT(nbt);

        nbt.setDouble("temperature", temperature);
        super.writeCustomNBT(nbt, categories, toDisk);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.INVENTORY)) {
            inventory.readFromNBT(nbt);
        }

        tank.readFromNBT(nbt);

        temperature = nbt.getDouble("temperature");
        super.readCustomNBT(nbt, categories);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? (T) tank : super.getCapability(capability, facing);
    }

}
