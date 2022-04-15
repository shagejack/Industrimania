package shagejack.industrimania.content.contraptions.multimeter;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.List;

public interface IMultimeterInfo {

    List<Component> getInfo(Level level, BlockPos pos);

}
