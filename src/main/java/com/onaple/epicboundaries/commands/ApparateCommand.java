package com.onaple.epicboundaries.commands;

import com.flowpowered.math.vector.Vector3d;
import com.onaple.epicboundaries.EpicBoundaries;
import com.onaple.epicboundaries.event.ApparateEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

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

        Vector3d position = getPosition(args);

        Player player = getPlayer(src,args);

        Location<World> location = getLocation(worldName,position);

        ApparateEvent apparateEvent = new ApparateEvent(player,location);
        Sponge.getEventManager().post(apparateEvent);
        if(!apparateEvent.isCancelled()){
             EpicBoundaries.getWorldAction().transferPlayerToWorld(player,location);
        }

        return CommandResult.success();
    }
}
