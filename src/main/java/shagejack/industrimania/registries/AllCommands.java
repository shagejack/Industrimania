package shagejack.industrimania.registries;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.command.ConfigCommand;
import shagejack.industrimania.commands.CommandGetChunkPollution;
import shagejack.industrimania.commands.CommandRemoveRocks;
import shagejack.industrimania.commands.CommandSetChunkPollution;

public class AllCommands {

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event)
    {
        new CommandRemoveRocks(event.getDispatcher());
        new CommandSetChunkPollution(event.getDispatcher());
        new CommandGetChunkPollution(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }
}
