package shagejack.shagecraft.gui.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import shagejack.shagecraft.Shagecraft;

public class GuiElementLoader implements IGuiHandler {
    public static final int GUI_DEMO = 1;
    public  GuiElementLoader(){
        NetworkRegistry.INSTANCE.registerGuiHandler(Shagecraft.INSTANCE,this);
    }
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID)
        {
            case GUI_DEMO:
                return new ContainerDemo(player);
            default:
                return null;
        }
    }
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID)
        {
            case GUI_DEMO:
                return new guiContainerDemo(new ContainerDemo(player));
            default:
                return null;
        }
    }

}
