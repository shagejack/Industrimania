package shagejack.industrimania.content.contraptions.multimeter;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class Multimeter extends Item {

    public Multimeter(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (!level.isClientSide())
            return super.useOn(context);

        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState state = level.getBlockState(pos);

        if (player != null && state.getBlock() instanceof IMultimeterInfo info) {
            List<Component> components = info.getInfo(level, pos);

            components.add(0, new TextComponent("========================================").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_GRAY));
            components.add(1, new TextComponent("                ").append(state.getBlock().getName()).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.GRAY));
            components.add(new TextComponent("========================================").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_GRAY));

            for (Component component : components) {
                player.sendMessage(component, Util.NIL_UUID);
            }
            return InteractionResult.PASS;
        }

        return super.useOn(context);
    }
}
