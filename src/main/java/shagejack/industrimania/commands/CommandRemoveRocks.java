package shagejack.industrimania.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import shagejack.industrimania.registers.block.grouped.AllRocks;

import java.util.List;

public class CommandRemoveRocks {

    public CommandRemoveRocks(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("removerocks").then(Commands.argument("range", IntegerArgumentType.integer()).executes(this::removeRocks)));
    }

    private int removeRocks(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = context.getSource().getPlayerOrException();
        Level level = context.getSource().getLevel();
        BlockPos playerPos = player.getOnPos();

        final List<Block> igneousStones = AllRocks.igneousStones.stream().map((rock) -> rock.block().get()).toList();
        final List<Block> metamorphicStones = AllRocks.metamorphicStones.stream().map((rock) -> rock.block().get()).toList();
        final List<Block> sedimentaryStones = AllRocks.sedimentaryStones.stream().map((rock) -> rock.block().get()).toList();

        if (context.getSource().hasPermission(2)) {

            int range = IntegerArgumentType.getInteger(context, "range");

            if (range < 128) {

                int counter = 0;

                for (int x = -range; x < range; x++) {
                    for (int y = -range; y < range; y++) {
                        for (int z = -range; z < range; z++) {
                            BlockPos nPos = playerPos.offset(x, y, z);
                            Block block = level.getBlockState(nPos).getBlock();
                            if (igneousStones.contains(block) || metamorphicStones.contains(block) || sedimentaryStones.contains(block)) {
                                level.removeBlock(nPos, true);
                                counter++;
                            }
                        }
                    }
                }

                context.getSource().sendSuccess(new TextComponent("successfully removed " + counter + " rocks."), true);
                return 1;

            } else {
                context.getSource().sendFailure(new TextComponent(range + " is too big (>128)!"));
                return -1;
            }
        } else {
            context.getSource().sendFailure(new TextComponent("You don't have permission to run this command!"));
            return -1;
        }

    }

}
