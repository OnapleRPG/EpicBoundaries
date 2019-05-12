package com.onaple.epicboundaries.commands;

import com.onaple.crowdbinding.service.GroupService;
import com.onaple.epicboundaries.EpicBoundaries;
import com.onaple.epicboundaries.WorldAction;

import cz.neumimto.core.ioc.IoC;
import cz.neumimto.rpg.players.CharacterService;
import cz.neumimto.rpg.players.IActiveCharacter;
import cz.neumimto.rpg.players.parties.Party;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.world.storage.WorldProperties;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @autor Hugo on 12/05/19.
 */
class CommandAbstract {
    protected Player getPlayer(CommandSource src, CommandContext args) throws CommandException {
        Optional<Player> playerOpt = args.getOne("player");
        if (!playerOpt.isPresent()) {
            if (src instanceof Player) {
                return (Player) src;
            } else {
                throw new CommandException(Text.of("There must be a player target."));
            }
        } else {
            return playerOpt.get();
        }
    }

    protected List<Player> getGroup(Player player) throws CommandException {
        List<Player> players = new ArrayList<>();
        Optional<GroupService> optionalGroupService;
        try {
            optionalGroupService = Sponge.getServiceManager().provide(GroupService.class);
            if (optionalGroupService.isPresent()) {
                GroupService groupService = optionalGroupService.get();
                groupService.getGroupId(player).ifPresent(gid -> players.addAll(groupService.getMembers(gid)));

            }
        } catch (NoClassDefFoundError e) {
            EpicBoundaries.getLogger().warn("error while get group members", e);
          //  throw new CommandException(Text.of("You need either the plugin CrowdBinding to teleport a group."));
        }
        try {
            if (Sponge.getPluginManager().isLoaded("nt-rpg")) {
                EpicBoundaries.getLogger().info("nt-rpg loaded, check for group members [{}] ",
                        getGroupMember(player));
                players.addAll(getGroupMember(player));
            } else {
                throw new CommandException(Text.of("You need the plugin nt-rpg to teleport a group."));
            }
        } catch (NoClassDefFoundError e) {
            EpicBoundaries.getLogger().warn("error while get group members", e);
          //  throw new CommandException(Text.of("You need either the plugin nt-rpg to teleport a group."));
        }

        return players;
    }

    protected void teleport(String worldName, Vector3d position, Player player) throws CommandException {
        Location<World> location = getLocation(worldName, position);
        WorldAction worldAction = new WorldAction();
        worldAction.transferPlayerToWorld(player, location);
    }

    protected void teleport(String worldName, Vector3d position, List<Player> players) throws CommandException {
        Location<World> location = getLocation(worldName, position);
        WorldAction worldAction = new WorldAction();
        worldAction.transferPlayersToWorld(players, location);
    }

    private Location<World> getLocation(String worldName, Vector3d position) throws CommandException {
        Optional<World> worldOpt = Sponge.getServer().loadWorld(worldName);
        if (!worldOpt.isPresent()) {
            throw new CommandException(Text.of("World " + worldName + " doesn't exist."));
        }
        return worldOpt.get().getLocation(position);
    }

    protected String newWolrdInstance(String worldName) throws CommandException {
        Optional<WorldProperties> worldProperties = Sponge.getServer().getWorldProperties(worldName);
        if (!worldProperties.isPresent()) {
            throw new CommandException(Text.of("The world " + worldName + " was not found and can therefore not be copied."));
        }

        String newWorldName;
        do {
            newWorldName = java.util.UUID.randomUUID().toString();
        } while (Sponge.getServer().getWorldProperties(newWorldName).isPresent());
        WorldAction worldAction = new WorldAction();
        worldAction.copyWorld(worldProperties.get(), newWorldName);
        return newWorldName;
    }

    private List<Player> getGroupMember(Player player) {
        CharacterService characterService = IoC.get().build(CharacterService.class);
        IActiveCharacter activeCharacter = characterService.getCharacter(player.getUniqueId());
        Party party = activeCharacter.getParty();
        return party.getPlayers().stream().map(iActiveCharacter -> iActiveCharacter.getPlayer()).collect(Collectors.toList());
    }
}
