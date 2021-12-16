package shagejack.shagecraft.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.server.timings.TimeTracker;
import shagejack.shagecraft.Shagecraft;
import shagejack.shagecraft.data.Inventory;
import shagejack.shagecraft.data.inventory.FuelSlot;
import shagejack.shagecraft.data.inventory.Slot;
import shagejack.shagecraft.data.tank.ShageFluidTank;
import shagejack.shagecraft.init.ShagecraftCapabilities;
import shagejack.shagecraft.machines.MachineNBTCategory;
import shagejack.shagecraft.machines.events.MachineEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class TileEntityMachineSteamForgeHammer extends ShageTileEntityMachineSteam {
    private static final double REQUIRED_POWER = 10; //Unit: KW
    private static final double REQUIRED_WORK = 200; //Unit: KJ
    private static final double EFFICIENCY = 0.5;
    private static final Random random = new Random();
    private final TimeTracker time;
    private final double STEAM_CAPACITY = 500;

    private int idle;
    private boolean working;

    public TileEntityMachineSteamForgeHammer() {
        super();

        this.steamStorage.setCapacity(STEAM_CAPACITY);
        this.steamStorage.setMaxReceive(STEAM_CAPACITY);
        this.steamStorage.setMaxExtract(STEAM_CAPACITY);

        time = new TimeTracker();
        playerSlotsMain = false;
        playerSlotsHotbar = false;
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
            world.setBlockToAir(getPos());
            if (steamStorage.getSteamPressure() < 10) {
                world.createExplosion(null, getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, (float) (5.0 * steamStorage.getSteamPressure()), true);
            } else {
                world.createExplosion(null, getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, 50.0F, true);
            }
        }

    }

    public void manageWork() {
        if (working) {
            if (idle > 0) {
                idle--;
            } else {
                //Work
                doWork();
                working = false;
            }
        }

        if (isSteamEnoughForWork() && !working) {
            //Release Steam
            idle = (int) ((REQUIRED_WORK / REQUIRED_POWER) / (steamStorage.getCurrentActualPower(0.15) / REQUIRED_POWER));
            steamStorage.releaseSteam();
            working = true;
        }
    }

    public boolean isSteamEnoughForWork() {
        if (steamStorage.getSteamState() != 0) {
            if (steamStorage.getCurrentActualPower(EFFICIENCY) > REQUIRED_POWER && steamStorage.getCurrentActualWork(EFFICIENCY) > REQUIRED_WORK) {
                return true;
            }
        }
        return false;
    }

    public void doWork() {
        List<Object[]> recipeList = new ArrayList<>();

        recipeList.add(new Object[]{Blocks.IRON_BLOCK, new ItemStack(Shagecraft.ITEMS.wrought_iron_big_plate)});

        Block block = world.getBlockState(getPos().down()).getBlock();
        for (Object[] recipe : recipeList) {
            if (block == recipe[0]) {
                world.setBlockToAir(getPos().down());
                ItemStack resultStack = (ItemStack) recipe[1];
                if (resultStack != null) {
                    spawnItemStack(world, pos.getX() + 0.5D, pos.getY() - 0.5D, pos.getZ() + 0.5D, resultStack);
                    playSound(world, SoundEvents.BLOCK_ANVIL_PLACE, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 5.0F, world.rand.nextFloat() * 0.1F + 0.9F);
                }
            }
        }

        playSound(world, SoundEvents.BLOCK_FIRE_EXTINGUISH, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 5.0F, world.rand.nextFloat() * 0.1F + 0.9F);
    }


    public static void spawnItemStack(World world, double x, double y, double z, ItemStack stack) {
        EntityItem entityitem = new EntityItem(world, x, y, z, stack);
        entityitem.motionX = 0D;
        entityitem.motionY = 0D;
        entityitem.motionZ = 0D;
        entityitem.setPickupDelay(20);
        world.spawnEntity(entityitem);
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
        nbt.setBoolean("working", working);
        super.writeCustomNBT(nbt, categories, toDisk);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.INVENTORY)) {
            inventory.readFromNBT(nbt);
        }


        idle = nbt.getInteger("idle");
        working = nbt.getBoolean("working");
        super.readCustomNBT(nbt, categories);
    }

}
