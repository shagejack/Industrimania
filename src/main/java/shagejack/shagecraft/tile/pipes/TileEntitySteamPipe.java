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
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class TileEntitySteamPipe extends TileEntityPipe implements IFluidPipe {
    public static Random rand = new Random();
    protected SteamStorage storage;
    protected SteamPipeNetwork fluidPipeNetwork;
    protected int transferSpeed;
    TimeTracker t;

    private double durability;

    public TileEntitySteamPipe() {
        t = new TimeTracker();
        this.storage = new SteamStorage(100);
        this.transferSpeed = 10;
    }

    @Override
    public void update() {
        super.update();
        if (!world.isRemote) {
            if (storage.hasSteam()) {
                manageSteamStored();
                manageTransfer();
            } else {
                storage.setSteamState(0);
            }
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
        if (storage.hasSteam()) {
            //for (IFluidPipe pipe : getNetwork().getNodes()) {
            List<EnumFacing> faces = new ArrayList<>();
                for (EnumFacing direction : EnumFacing.VALUES) {
                    //TileEntity handler = pipe.getTile().getWorld().getTileEntity(pipe.getTile().getPos().offset(direction));
                    TileEntity handler = world.getTileEntity(getPos().offset(direction));
                    //if (handler != null && handler.hasCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()) && !(handler instanceof IFluidPipe))
                    if (handler != null && handler.hasCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite())) {
                        if (storage.getSteamMass() * storage.getSteamPressure() > handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).getSteamMass() * handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).getSteamPressure()) {
                            faces.add(direction);
                        }
                    }
                }
                for (EnumFacing direction : faces) {
                    TileEntity handler = world.getTileEntity(getPos().offset(direction));
                    extractSteam(storage.mergeProperties(), faces.size(), handler, direction);
                }
            //}
        }
    }

    public void extractSteam(double[] properties, int faces, TileEntity handler, EnumFacing direction) {

        if (properties[0] * storage.getSteamPressure() > handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).getSteamMass() * handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).getSteamPressure()){

            double[] remainProperties = properties;

            remainProperties[0] = remainProperties[0] / ( faces + 1 );

            /*
            if (properties[0] + handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).getSteamMass() > handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).getCapacity()) {
                properties[0] = handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).getCapacity() - properties[0] + handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).getSteamMass();
            }
            */

            //Transfer Steam
            double[] resultProperties = storage.mergeSteam(remainProperties, handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).mergeProperties(), false);

            remainProperties[0] = storage.getSteamMass() - remainProperties[0];

            storage.setProperties(remainProperties);
            handler.getCapability(ShagecraftCapabilities.STEAM_HANDLER, direction.getOpposite()).setProperties(resultProperties);

            if (storage.getSteamMass() <= 0) {
                storage.setSteamState(0);
            }

            if (!world.isRemote) Shagecraft.NETWORK.sendToAllAround(new PacketSteamUpdate(handler), handler, 64);

        }
    }

    public void manageSteamStored() {

        double[] properties = storage.mergeProperties();

        //TODO: Cooling
        if (properties[0] > 0 && properties[2] != 0) {
            if (properties[1] > 373.15) {
                properties[1] -= 0.01;
            } else {
                properties[0] = 0;
                properties[2] = 0;
            }
        }

        storage.setProperties(properties);

        if (storage.isExceededCapacity()){
            world.setBlockToAir(getPos());
            if (storage.getSteamPressure() < 10) {
                world.createExplosion(null, getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, (float) (2.0 * storage.getSteamPressure()), true);
            } else {
                world.createExplosion(null, getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, 20.0F, true);
            }
        }

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
            comp.setDouble("durability", durability);
        }
        super.writeCustomNBT(comp, categories, toDisk);
    }

    @Override
    public void readCustomNBT(NBTTagCompound comp, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.DATA)) {
            storage.readFromNBT(comp);
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
        durability = 100;
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
