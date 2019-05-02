package com.onaple.epicboundaries.commands;

import com.flowpowered.math.vector.Vector3d;
import com.onaple.crowdbinding.service.GroupService;
import com.onaple.epicboundaries.WorldAction;
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

public class ApparateGroupCommand implements CommandExecutor {
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

        Optional<GroupService> optionalGroupService;
        try {
            optionalGroupService = Sponge.getServiceManager().provide(GroupService.class);
            if (!optionalGroupService.isPresent()) {
                src.sendMessage(Text.of("You need the plugin CrowdBinding to teleport a group."));
                return CommandResult.empty();
            }
        } catch (NoClassDefFoundError e) {
            src.sendMessage(Text.of("You need the plugin CrowdBinding to teleport a group."));
            return CommandResult.empty();
        }
        GroupService groupService = optionalGroupService.get();

        List<Player> players = new ArrayList<>();
        groupService.getGroupId(player).ifPresent(gid -> players.addAll(groupService.getMembers(gid)));
        if (players.size() <= 1) {
            src.sendMessage(Text.of("The player is not within a group."));
            return CommandResult.empty();
        }

        Optional<World> worldOpt = Sponge.getServer().loadWorld(worldName);
        if (!worldOpt.isPresent()) {
            src.sendMessage(Text.of("World " + worldName + " doesn't exist."));
            return CommandResult.empty();
        }
        Location<World> location = worldOpt.get().getLocation(position);

        WorldAction worldAction = new WorldAction();
        worldAction.transferPlayersToWorld(players, location);
        return CommandResult.success();
    }
}
