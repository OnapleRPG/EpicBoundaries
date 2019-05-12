package com.onaple.epicboundaries.commands;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class ApparateCommand extends CommandAbstract implements CommandExecutor {
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

        Optional<Vector3d> positionOpt = args.<Vector3d>getOne("position");
        if (!positionOpt.isPresent()) {
            src.sendMessage(Text.of("A position must be specified."));
            return CommandResult.empty();
        }
        Vector3d position = positionOpt.get();

        Player player = getPlayer(src,args);

        teleport(worldName,position,player);

        return CommandResult.success();
    }
}
