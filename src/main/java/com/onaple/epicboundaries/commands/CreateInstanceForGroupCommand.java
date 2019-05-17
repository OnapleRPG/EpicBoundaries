package com.onaple.epicboundaries.commands;

import com.flowpowered.math.vector.Vector3d;
import com.onaple.epicboundaries.EpicBoundaries;
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

import java.util.List;
import java.util.Optional;

public class CreateInstanceForGroupCommand extends CommandAbstract implements CommandExecutor {
    /**
     * Copy world with given name with given suffix
     * @param src Origin of the command
     * @param args Arguments of the command
     * @return Command result state
     * @throws CommandException Command error
     */
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        World world = getWorldOtherThanDefault(args);

        Optional<Vector3d> positionOpt = args.<Vector3d>getOne("position");
        if (!positionOpt.isPresent()) {
            src.sendMessage(Text.of("A position must be specified."));
            return CommandResult.empty();
        }
        Vector3d position = positionOpt.get();

        Player player = getPlayer(src,args);

        List<Player> players = EpicBoundaries.getGroupHandler().getGroup(player);

        String newWorldInstance = EpicBoundaries.getWorldAction().newWorldInstance(world);


        WorldAction worldAction = new WorldAction();
        Location location = world.getLocation(position);
        ApparateEvent apparateEvent = new ApparateEvent(player,location,players);
        Sponge.getEventManager().post(apparateEvent);
        if(!apparateEvent.isCancelled()){
            worldAction.addPlayersToTransferQueue(players,newWorldInstance,position);
        }


        return CommandResult.success();
    }
}
