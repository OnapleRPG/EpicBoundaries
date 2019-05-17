package com.onaple.epicboundaries.commands;

import com.flowpowered.math.vector.Vector3d;
import com.onaple.crowdbinding.service.GroupService;
import com.onaple.epicboundaries.EpicBoundaries;
import com.onaple.epicboundaries.GroupHandler;
import com.onaple.epicboundaries.WorldAction;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApparateGroupCommand extends CommandAbstract implements CommandExecutor {
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

        List<Player> players =EpicBoundaries.getGroupHandler().getGroup(player);

        if (players.size() <= 1) {
            src.sendMessage(Text.of("The player is not within a group."));
            return CommandResult.empty();
        }

        Location<World> location = getLocation(worldName,position);
        ApparateEvent apparateEvent = new ApparateEvent(player,location,players);
        Sponge.getEventManager().post(apparateEvent);
        if(!apparateEvent.isCancelled()){
            EpicBoundaries.getWorldAction().transferPlayerToWorld(player,location);
        }

        return CommandResult.success();
    }
}
