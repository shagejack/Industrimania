package shagejack.minecraftology.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;
import shagejack.minecraftology.container.ContainerFactory;
import shagejack.minecraftology.container.MCLBaseContainer;
import shagejack.minecraftology.gui.MCLGuiBase;
import shagejack.minecraftology.gui.MCLGuiMachine;
import shagejack.minecraftology.machines.MCLTileEntityMachine;
import shagejack.minecraftology.tile.MCLTileEntity;
import shagejack.minecraftology.util.LogMCL;
import shagejack.minecraftology.gui.*;
import shagejack.minecraftology.tile.*;
import shagejack.minecraftology.container.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class GuiHandler implements IGuiHandler {
    private Map<Class<? extends MCLTileEntity>, Class<? extends MCLGuiBase>> tileEntityGuiList;
    private Map<Class<? extends MCLTileEntity>, Class<? extends MCLBaseContainer>> tileEntityContainerList;

    public GuiHandler() {
        tileEntityGuiList = new HashMap<>();
        tileEntityContainerList = new HashMap<>();
    }

    public void register(Side side) {
        if (side == Side.SERVER) {
            //Container Registration
            registerContainer(TileEntityForgeFurnace.class, ContainerForgeFurnace.class);
        } else {
            //Gui Registration
            registerGuiAndContainer(TileEntityForgeFurnace.class, GuiForgeFurnace.class, ContainerForgeFurnace.class);
        }
    }

    public void registerContainer(Class<? extends MCLTileEntity> tileEntity, Class<? extends MCLBaseContainer> container) {
        tileEntityContainerList.put(tileEntity, container);
    }

    public void registerGuiAndContainer(Class<? extends MCLTileEntity> tileEntity, Class<? extends MCLGuiBase> gui, Class<? extends MCLBaseContainer> container) {
        tileEntityContainerList.put(tileEntity, container);
        tileEntityGuiList.put(tileEntity, gui);
    }

    public void registerGui(Class<? extends MCLTileEntity> tileEntity, Class<? extends MCLGuiBase> gui) {
        tileEntityGuiList.put(tileEntity, gui);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {

        TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));

        switch (ID) {
            default:
                if (entity != null && tileEntityContainerList.containsKey(entity.getClass())) {
                    try {
                        Class<? extends MCLBaseContainer> containerClass = tileEntityContainerList.get(entity.getClass());
                        Constructor[] constructors = containerClass.getDeclaredConstructors();
                        for (Constructor constructor : constructors) {
                            Class[] parameterTypes = constructor.getParameterTypes();
                            if (parameterTypes.length == 2) {
                                if (parameterTypes[0].isInstance(player.inventory) && parameterTypes[1].isInstance(entity)) {
                                    onContainerOpen(entity, Side.SERVER);
                                    return constructor.newInstance(player.inventory, entity);
                                }
                            }
                        }
                    } catch (InvocationTargetException e) {
                        LogMCL.log(Level.WARN, e, "Could not call TileEntity constructor in server GUI handler");
                    } catch (InstantiationException e) {
                        LogMCL.log(Level.WARN, e, "Could not instantiate TileEntity in server GUI handler");
                    } catch (IllegalAccessException e) {
                        LogMCL.log(Level.WARN, e, "Could not access TileEntity constructor in server GUI handler");
                    }
                } else if (entity instanceof MCLTileEntityMachine) {
                    return ContainerFactory.createMachineContainer((MCLTileEntityMachine) entity, player.inventory);
                }
        }
        return null;
    }

    private void onContainerOpen(TileEntity entity, Side side) {
        if (entity instanceof MCLTileEntityMachine) {
            ((MCLTileEntityMachine) entity).onContainerOpen(side);
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world,
                                      int x, int y, int z) {

        TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));

        switch (ID) {
            default:
                if (tileEntityGuiList.containsKey(entity.getClass())) {
                    try {

                        Class<? extends MCLGuiBase> containerClass = tileEntityGuiList.get(entity.getClass());
                        Constructor[] constructors = containerClass.getDeclaredConstructors();
                        for (Constructor constructor : constructors) {
                            Class[] parameterTypes = constructor.getParameterTypes();
                            if (parameterTypes.length == 2) {
                                if (parameterTypes[0].isInstance(player.inventory) && parameterTypes[1].isInstance(entity)) {
                                    onContainerOpen(entity, Side.CLIENT);
                                    return constructor.newInstance(player.inventory, entity);
                                }
                            }
                        }
                    } catch (InvocationTargetException e) {
                        LogMCL.log(Level.WARN, e, "Could not call TileEntity constructor in client GUI handler");
                    } catch (InstantiationException e) {
                        LogMCL.log(Level.WARN, e, "Could not instantiate the TileEntity in client GUI handler");
                    } catch (IllegalAccessException e) {
                        LogMCL.log(Level.WARN, e, "Could not access TileEntity constructor in client GUI handler");
                    }
                } else if (entity instanceof MCLTileEntityMachine) {
                    return new MCLGuiMachine(ContainerFactory.createMachineContainer((MCLTileEntityMachine) entity, player.inventory), (MCLTileEntityMachine) entity);
                }
        }
        return null;
    }
}

