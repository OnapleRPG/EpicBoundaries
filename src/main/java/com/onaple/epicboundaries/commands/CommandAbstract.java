package com.onaple.epicboundaries.commands;

import com.onaple.crowdbinding.service.GroupService;
import com.onaple.epicboundaries.EpicBoundaries;
import com.onaple.epicboundaries.WorldAction;

import cz.neumimto.core.ioc.IoC;
import cz.neumimto.rpg.players.CharacterService;
import cz.neumimto.rpg.players.IActiveCharacter;
import cz.neumimto.rpg.players.parties.Party;
import net.minecraft.command.server.CommandListPlayers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
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
public class CommandAbstract {

    protected World getWorldOtherThanDefault(CommandContext args) throws CommandException {
        Optional<World> worldNameOpt = args.<World>getOne("world");
        if (!worldNameOpt.isPresent() || worldNameOpt.get().getName().equals(Sponge.getServer().getDefaultWorldName())) {
          throw new CommandException(Text.of("A world other than default one must be specified."));
        }
     return worldNameOpt.get();
    }

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
    protected Vector3d getPosition(CommandContext args) throws CommandException{
        Optional<Vector3d> positionOpt = args.<Vector3d>getOne("position");
        if (!positionOpt.isPresent()) {
            throw new CommandException(Text.of("A position must be specified."));

        }
        return positionOpt.get();
    }

    protected Location<World> getLocation(String worldName, Vector3d position) throws CommandException {
        Optional<World> worldOpt = Sponge.getServer().loadWorld(worldName);
        if (!worldOpt.isPresent()) {
            throw new CommandException(Text.of("World " + worldName + " doesn't exist."));
        }
        return worldOpt.get().getLocation(position);
    }


}
