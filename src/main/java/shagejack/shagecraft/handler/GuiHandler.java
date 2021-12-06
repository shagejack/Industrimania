package shagejack.shagecraft.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;
import shagejack.shagecraft.container.ContainerFactory;
import shagejack.shagecraft.container.ShageBaseContainer;
import shagejack.shagecraft.gui.ShageGuiBase;
import shagejack.shagecraft.gui.ShageGuiMachine;
import shagejack.shagecraft.machines.ShageTileEntityMachine;
import shagejack.shagecraft.tile.ShageTileEntity;
import shagejack.shagecraft.util.LogShage;
import shagejack.shagecraft.gui.*;
import shagejack.shagecraft.tile.*;
import shagejack.shagecraft.container.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class GuiHandler implements IGuiHandler {
    private Map<Class<? extends ShageTileEntity>, Class<? extends ShageGuiBase>> tileEntityGuiList;
    private Map<Class<? extends ShageTileEntity>, Class<? extends ShageBaseContainer>> tileEntityContainerList;

    public GuiHandler() {
        tileEntityGuiList = new HashMap<>();
        tileEntityContainerList = new HashMap<>();
    }

    public void register(Side side) {
        if (side == Side.SERVER) {
            //Container Registration
            registerContainer(TileEntityForgeFurnace.class, ContainerForgeFurnace.class);
            registerContainer(TileEntityGlassMeltingFurnaceInput.class, ContainerGlassMeltingFurnaceInput.class);
        } else {
            //Gui Registration
            registerGuiAndContainer(TileEntityForgeFurnace.class, GuiForgeFurnace.class, ContainerForgeFurnace.class);
            registerGuiAndContainer(TileEntityGlassMeltingFurnaceInput.class, GuiGlassMeltingFurnaceInput.class, ContainerGlassMeltingFurnaceInput.class);
        }
    }

    public void registerContainer(Class<? extends ShageTileEntity> tileEntity, Class<? extends ShageBaseContainer> container) {
        tileEntityContainerList.put(tileEntity, container);
    }

    public void registerGuiAndContainer(Class<? extends ShageTileEntity> tileEntity, Class<? extends ShageGuiBase> gui, Class<? extends ShageBaseContainer> container) {
        tileEntityContainerList.put(tileEntity, container);
        tileEntityGuiList.put(tileEntity, gui);
    }

    public void registerGui(Class<? extends ShageTileEntity> tileEntity, Class<? extends ShageGuiBase> gui) {
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
                        Class<? extends ShageBaseContainer> containerClass = tileEntityContainerList.get(entity.getClass());
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
                        LogShage.log(Level.WARN, e, "Could not call TileEntity constructor in server GUI handler");
                    } catch (InstantiationException e) {
                        LogShage.log(Level.WARN, e, "Could not instantiate TileEntity in server GUI handler");
                    } catch (IllegalAccessException e) {
                        LogShage.log(Level.WARN, e, "Could not access TileEntity constructor in server GUI handler");
                    }
                } else if (entity instanceof ShageTileEntityMachine) {
                    return ContainerFactory.createMachineContainer((ShageTileEntityMachine) entity, player.inventory);
                }
        }
        return null;
    }

    private void onContainerOpen(TileEntity entity, Side side) {
        if (entity instanceof ShageTileEntityMachine) {
            ((ShageTileEntityMachine) entity).onContainerOpen(side);
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

                        Class<? extends ShageGuiBase> containerClass = tileEntityGuiList.get(entity.getClass());
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
                        LogShage.log(Level.WARN, e, "Could not call TileEntity constructor in client GUI handler");
                    } catch (InstantiationException e) {
                        LogShage.log(Level.WARN, e, "Could not instantiate the TileEntity in client GUI handler");
                    } catch (IllegalAccessException e) {
                        LogShage.log(Level.WARN, e, "Could not access TileEntity constructor in client GUI handler");
                    }
                } else if (entity instanceof ShageTileEntityMachine) {
                    return new ShageGuiMachine(ContainerFactory.createMachineContainer((ShageTileEntityMachine) entity, player.inventory), (ShageTileEntityMachine) entity);
                }
        }
        return null;
    }
}

