package shagejack.minecraftology.gui.FurnaceGui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import shagejack.minecraftology.Minecraftology;

import javax.annotation.Nullable;

public enum FurnaceGuiHandler implements IGuiHandler {
    INSTANCE;

    private static final int GUIID = 1;



    private FurnaceGuiHandler() {
        NetworkRegistry.INSTANCE.registerGuiHandler(Minecraftology.INSTANCE,this);
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int Id,EntityPlayer player,World world,int x,int y,int z) {
        return null;
    }
    @Nullable
    @Override
    public Object getClientGuiElement(int Id,EntityPlayer player,World world,int x,int y,int z) {
        return null;
        }
}
