package com.onaple.epicboundaries.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class ApparateCommand implements CommandExecutor {
    /**
     * Apparate a player in a given world
     * @param src Origin of the command
     * @param args Arguments of the command
     * @return Command result state
     * @throws CommandException Command error
     */
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String worldName = args.<String>getOne("world").orElse("world");

        Optional<Player> playerOpt = args.getOne("player");
        Player player = null;
        if (!playerOpt.isPresent()) {
            if (src instanceof Player) {
                player = (Player) src;
            } else {
                src.sendMessage(Text.of("There must be a player target."));
                return CommandResult.empty();
            }
        } else {
            player = playerOpt.get();
        }

        Optional<World> worldOpt = Sponge.getServer().loadWorld(worldName);
        if (!worldOpt.isPresent()) {
            src.sendMessage(Text.of("World " + worldName + " doesn't exist."));
            return CommandResult.empty();
        }
        World world = worldOpt.get();

        player.transferToWorld(world, world.getSpawnLocation().getPosition());
        return CommandResult.success();
    }
}
