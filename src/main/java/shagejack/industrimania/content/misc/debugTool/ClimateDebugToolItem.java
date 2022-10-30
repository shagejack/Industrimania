package shagejack.industrimania.content.misc.debugTool;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import shagejack.industrimania.content.climate.HeatSystem;
import shagejack.industrimania.foundation.utility.Vector3;

import java.util.ArrayList;
import java.util.List;

public class ClimateDebugToolItem extends Item {

    public ClimateDebugToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel() instanceof ServerLevel level) {
            showDebugInfo(level, context.getPlayer(), Vector3.of(context.getClickLocation()));
        }

        return super.useOn(context);
    }

    public static void showDebugInfo(ServerLevel level, Player player, Vector3 pos) {
        List<Component> components = new ArrayList<>();

        components.add(new TextComponent("========================================").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_GRAY));
        components.add(new TextComponent(" Pos: ").append(pos.toString()).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.GRAY));
        components.add(new TextComponent(" Temperature: " + (HeatSystem.getBlockTemp(level, pos)) + " C").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.GRAY));
        components.add(new TextComponent("========================================").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.DARK_GRAY));

        for (Component component : components) {
            player.sendMessage(component, Util.NIL_UUID);
        }
    }
}
