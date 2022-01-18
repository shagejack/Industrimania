package shagejack.industrimania.registers;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.command.ConfigCommand;
import shagejack.industrimania.commands.CommandRemoveRocks;
import shagejack.industrimania.commands.CommandSetChunkPollution;

public class AllCommands {

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event)
    {
        new CommandRemoveRocks(event.getDispatcher());
        new CommandSetChunkPollution(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }
}
