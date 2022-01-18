package shagejack.industrimania.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import shagejack.industrimania.content.pollution.Pollution;
import shagejack.industrimania.content.pollution.PollutionDataHooks;

public class CommandGetChunkPollution {

    public CommandGetChunkPollution(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("chunkpollution").then(Commands.literal("get").executes(this::getChunkPollution)));
    }

    private int getChunkPollution(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = context.getSource().getPlayerOrException();
        Level level = context.getSource().getLevel();
        BlockPos playerPos = player.getOnPos();

        if (context.getSource().hasPermission(2)) {

            Pollution pollution = PollutionDataHooks.getPollution(level.dimensionType(), level.getChunk(playerPos).getPos());

            long amount = 0;

            if (pollution != null) {
                amount = pollution.getAmount();
            }

            context.getSource().sendSuccess(new TextComponent("Pollution in this chunk is " + amount), true);
            return 1;

        } else {
            context.getSource().sendFailure(new TextComponent("You don't have permission to run this command!"));
            return -1;
        }

    }

}
