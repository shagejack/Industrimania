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
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import shagejack.industrimania.content.pollution.PollutionDataHooks;

public class CommandSetChunkPollution {

    public CommandSetChunkPollution(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("chunkpollution").then(Commands.literal("set").then(Commands.argument("amount", LongArgumentType.longArg()).executes(this::setChunkPollution))));
    }

    private int setChunkPollution(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = context.getSource().getPlayerOrException();
        Level level = context.getSource().getLevel();
        BlockPos playerPos = player.getOnPos();

        if (context.getSource().hasPermission(2)) {

            long amount = LongArgumentType.getLong(context, "amount");
            DimensionType dim = level.dimensionType();
            ChunkPos chunkPos = level.getChunk(playerPos).getPos();

            PollutionDataHooks.getPollution(dim, chunkPos).setAmount(amount);

            context.getSource().sendSuccess(new TextComponent("successfully set chunk pollution amount to " + amount), true);
            return 1;

        } else {
            context.getSource().sendFailure(new TextComponent("You don't have permission to run this command!"));
            return -1;
        }

    }

}
