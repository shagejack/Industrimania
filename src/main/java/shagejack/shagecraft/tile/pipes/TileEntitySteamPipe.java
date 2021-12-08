package shagejack.shagecraft.tile.pipes;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.server.timings.TimeTracker;
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.api.transport.IGridNode;
import shagejack.shagecraft.data.SteamStorage;
import shagejack.shagecraft.data.transport.SteamPipeNetwork;
import shagejack.shagecraft.data.transport.IFluidPipe;
import shagejack.shagecraft.init.ShagecraftCapabilities;
import shagejack.shagecraft.machines.MachineNBTCategory;
import shagejack.shagecraft.network.packet.client.PacketSteamUpdate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class TileEntitySteamPipe extends TileEntityPipe implements IFluidPipe {
    public static Random rand = new Random();
    protected final SteamStorage storage;
    protected SteamPipeNetwork fluidPipeNetwork;
    protected int transferSpeed;
    TimeTracker t;

    private double durability;

    public TileEntitySteamPipe() {
        t = new TimeTracker();
        storage = new SteamStorage(1000);
        this.transferSpeed = 10;
    }

    @Override
    public void update() {
        super.update();
        if (!world.isRemote) {
            manageSteamStored();
            manageTransfer();
            manageNetwork();
        }
    }

    public void manageNetwork() {
        if (fluidPipeNetwork == null) {
            if (!tryConnectToNeighborNetworks(world)) {
                SteamPipeNetwork network = Shagecraft.STEAM_NETWORK_HANDLER.getNetwork(this);
                network.addNode(this);
            }
        }
    }

    public void manageTransfer() {
        if (storage.getSteamVolume() > 0 && getNetwork() != null) {
            for (IFluidPipe pipe : getNetwork().getNodes()) {
                for (EnumFacing direction : EnumFacing.VALUES) {
                    TileEntity handler = pipe.getTile().getWorld().getTileEntity(pipe.getTile().getPos().offset(direction));
                    if (handler != null && handler.hasCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()) && !(handler instanceof IFluidPipe)) {

                        double[] properties = storage.mergeProperties();

                        //Transfer Steam
                        double[] transferredProperties = storage.extractSteam(handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).mergeProperties(), handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).getSteamState(), false);

                        for (double prop : transferredProperties) {
                            if (prop != 0) {
                                Shagecraft.NETWORK.sendToAllAround(new PacketSteamUpdate(handler), handler, 64);
                                break;
                            }
                        }

                        if (storage.getSteamVolume() <= 0) {
                            return;
                        }

                    }
                }
            }
        }
    }

    public void manageSteamStored() {
        //TODO: Cooling
        double[] properties = storage.mergeProperties();
        if (properties[0] > 0) {
            if (properties[1] > 298.15) {
                properties[1] -= 0.01;
            }
        }

        storage.setProperties(properties);

    }

    public double getActualEnthalpy(double[] properties, int state){
        return 0;
    }

    public double[] managePropertiesFromBoilerHeat(double[] properties, int state){
        return properties;
    }

    @Override
    public boolean canConnectToPipe(TileEntity entity, EnumFacing direction) {
        if (entity != null) {
            if (entity instanceof TileEntitySteamPipe) {
                if (this.getBlockType() != entity.getBlockType()) {
                    return false;
                }
                return true;
            }
            return entity.hasCapability(ShagecraftCapabilities.STEAM_HANDLER, direction);
        }
        return false;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound comp, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        if (!world.isRemote && categories.contains(MachineNBTCategory.DATA) && toDisk) {
            storage.writeToNBT(comp);
        }
        super.writeCustomNBT(comp, categories, toDisk);
    }

    @Override
    public void readCustomNBT(NBTTagCompound comp, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.DATA)) {
            durability = comp.getDouble("durability");
        }
        super.readCustomNBT(comp, categories);
    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onAdded(World world, BlockPos pos, IBlockState state) {

    }

    public boolean tryConnectToNeighborNetworks(World world) {
        boolean hasConnected = false;
        for (EnumFacing side : EnumFacing.VALUES) {
            TileEntity neighborEntity = world.getTileEntity(pos.offset(side));
            if (neighborEntity instanceof TileEntitySteamPipe && this.getBlockType() == neighborEntity.getBlockType()) {
                    hasConnected = true;
            }
        }
        return hasConnected;
    }

    @Override
    public void onDestroyed(World worldIn, BlockPos pos, IBlockState state) {
        if (fluidPipeNetwork != null) {
            fluidPipeNetwork.onNodeDestroy(state, this);
        }
    }

    @Override
    public void onNeighborBlockChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock) {
        updateSides(true);
    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {

    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {

    }

    @Override
    public TileEntity getTile() {
        return this;
    }

    @Override
    public SteamPipeNetwork getNetwork() {
        return fluidPipeNetwork;
    }

    @Override
    public void setNetwork(SteamPipeNetwork network) {
        this.fluidPipeNetwork = network;
    }

    @Override
    public BlockPos getNodePos() {
        return getPos();
    }

    @Override
    public World getNodeWorld() {
        return getWorld();
    }

    @Override
    public boolean canConnectToNetworkNode(IBlockState blockState, IGridNode toNode, EnumFacing direction) {
        return toNode instanceof TileEntitySteamPipe;
    }

    public boolean canConnectFromSide(IBlockState blockState, EnumFacing side) {
        return true;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == ShagecraftCapabilities.STEAM_HANDLER) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == ShagecraftCapabilities.STEAM_HANDLER) {
            return (T) storage;
        }
        return super.getCapability(capability, facing);
    }

}
