package shagejack.industrimania.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import shagejack.industrimania.registers.block.grouped.AllRocks;

import java.util.List;

public class CommandRemoveRocks {

    public CommandRemoveRocks(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("removerocks").requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("range", IntegerArgumentType.integer()).executes(cs -> removeRocks(cs, false))
                .then(Commands.argument("rockexcepted", BlockStateArgument.block()).executes(cs -> removeRocks(cs, true))))
        );
    }

    private int removeRocks(CommandContext<CommandSourceStack> context, boolean hasRockExcepted) throws CommandSyntaxException {
        Player player = context.getSource().getPlayerOrException();
        Level level = context.getSource().getLevel();
        BlockPos playerPos = player.getOnPos();

        final List<Block> igneousStones = AllRocks.igneousStones.stream().map((rock) -> rock.block().get()).toList();
        final List<Block> metamorphicStones = AllRocks.metamorphicStones.stream().map((rock) -> rock.block().get()).toList();
        final List<Block> sedimentaryStones = AllRocks.sedimentaryStones.stream().map((rock) -> rock.block().get()).toList();

        int range = IntegerArgumentType.getInteger(context, "range");
        BlockInput rockExceptedBlock = null;

        if (hasRockExcepted) {
            rockExceptedBlock = BlockStateArgument.getBlock(context, "rockexcepted");
        }

        if (range < 128) {

            int counter = 0;

            for (int x = -range; x < range; x++) {
                for (int y = -range; y < range; y++) {
                    for (int z = -range; z < range; z++) {
                        BlockPos nPos = playerPos.offset(x, y, z);
                        BlockState state = level.getBlockState(nPos);
                        Block block = state.getBlock();
                        if (!hasRockExcepted || rockExceptedBlock == null || !rockExceptedBlock.getState().is(block)) {
                            if (!state.getFluidState().isEmpty() || block instanceof FallingBlock || block instanceof LiquidBlock || igneousStones.contains(block) || metamorphicStones.contains(block) || sedimentaryStones.contains(block)) {
                                level.setBlockAndUpdate(nPos, Blocks.AIR.defaultBlockState());
                                counter++;
                            }
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

    }

}
