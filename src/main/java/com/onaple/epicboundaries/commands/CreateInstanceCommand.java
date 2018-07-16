package com.onaple.epicboundaries.commands;

import com.onaple.epicboundaries.WorldAction;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

public class CreateInstanceCommand implements CommandExecutor {
    /**
     * Copy world with given name with given suffix
     * @param src Origin of the command
     * @param args Arguments of the command
     * @return Command result state
     * @throws CommandException Command error
     */
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> worldNameOpt = args.<String>getOne("world");
        if (!worldNameOpt.isPresent() || worldNameOpt.get().equals(Sponge.getServer().getDefaultWorldName())) {
            src.sendMessage(Text.of("A world other than default one must be specified."));
            return CommandResult.empty();
        }
        String worldName = worldNameOpt.get();

        Optional<String> suffixOpt = args.<String>getOne("suffix");
        if (!suffixOpt.isPresent() || suffixOpt.get().equals("")) {
            src.sendMessage(Text.of("A name suffix must be specified."));
            return CommandResult.empty();
        }

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

        Optional<WorldProperties> worldProperties = Sponge.getServer().getWorldProperties(worldName);
        if (!worldProperties.isPresent()) {
            src.sendMessage(Text.of("The world " + worldName + " was not found and can therefore not be copied."));
            return CommandResult.empty();
        }

        String newWorldName = worldName + "-" + suffixOpt.get();
        WorldAction.copyWorldAndTransferPlayer(player, worldProperties.get(), newWorldName);

        return CommandResult.success();
    }
}
