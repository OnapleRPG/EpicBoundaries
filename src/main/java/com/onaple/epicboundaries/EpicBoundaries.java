package com.onaple.epicboundaries;


import com.onaple.epicboundaries.commands.ApparateCommand;
import com.onaple.epicboundaries.commands.CreateInstanceCommand;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;


@Plugin(id = "epicboundaries", name = "EpicBoundaries", version = "0.1.1")
public class EpicBoundaries {
    private static Logger logger;
    @Inject
    private void setLogger(Logger logger) {
        EpicBoundaries.logger = logger;
    }
    public static Logger getLogger() {
        return logger;
    }

    @Listener
    public void onServerStart(GameInitializationEvent event) {
        CommandSpec apparateSpec = CommandSpec.builder()
                .description(Text.of("Apparate player to another world"))
                .permission("epicboundaries.command.apparate")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("world"))),
                        GenericArguments.optional(GenericArguments.player(Text.of("player"))))
                .executor(new ApparateCommand())
                .build();
        CommandSpec copyWorldSpec = CommandSpec.builder()
                .description(Text.of("Create an instance from an existing world"))
                .permission("epicboundaries.command.createinstance")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("world"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("suffix"))))
                .executor(new CreateInstanceCommand())
                .build();

        Sponge.getCommandManager().register(this, apparateSpec, "apparate");
        Sponge.getCommandManager().register(this, copyWorldSpec, "create-instance");

        logger.info("EPICBOUNDARIES initialized.");
    }
}
