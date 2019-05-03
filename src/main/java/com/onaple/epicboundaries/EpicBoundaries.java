package com.onaple.epicboundaries;


import com.onaple.epicboundaries.commands.ApparateCommand;
import com.onaple.epicboundaries.commands.ApparateGroupCommand;
import com.onaple.epicboundaries.commands.CreateInstanceCommand;
import com.onaple.epicboundaries.commands.CreateInstanceForGroupCommand;
import com.onaple.epicboundaries.data.access.InstanceDao;
import com.onaple.epicboundaries.event.CopyWorldEvent;
import com.onaple.epicboundaries.service.IInstanceService;
import com.onaple.epicboundaries.service.InstanceService;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;


@Plugin(id = "epicboundaries", name = "EpicBoundaries", version = "0.2",
        description = "Plugin managing world instances",
        authors = {"zessirb", "Selki"},
        dependencies = {@Dependency(id = "crowdbinding", optional = true)})
public class EpicBoundaries {
    private static Logger logger;
    @Inject
    private void setLogger(Logger logger) {
        EpicBoundaries.logger = logger;
    }
    public static Logger getLogger() {
        return logger;
    }

    private static PluginContainer pluginContainer;
    @Inject
    private void setPluginContainer(PluginContainer pluginContainer) {
        EpicBoundaries.pluginContainer = pluginContainer;
    }
    public static PluginContainer getPluginContainer() {
        return pluginContainer;
    }

    private static InstanceDao instanceDao;
    @Inject
    public void setInstanceDao(InstanceDao instanceDao) {
        EpicBoundaries.instanceDao = instanceDao;
    }
    public static InstanceDao getInstanceDao() {
        return instanceDao;
    }

    @Listener
    public void onServerStart(GameInitializationEvent event) {
        instanceDao.createTableIfNotExist();

        CommandSpec apparateSpec = CommandSpec.builder()
                .description(Text.of("Apparate player to another world"))
                .permission("epicboundaries.command.apparate")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("world"))),
                        GenericArguments.onlyOne(GenericArguments.vector3d(Text.of("position"))),
                        GenericArguments.optional(GenericArguments.player(Text.of("player"))))
                .executor(new ApparateCommand())
                .build();
        CommandSpec apparateGroupSpec = CommandSpec.builder()
                .description(Text.of("Apparate player's group to another world"))
                .permission("epicboundaries.command.apparategroup")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("world"))),
                        GenericArguments.onlyOne(GenericArguments.vector3d(Text.of("position"))),
                        GenericArguments.optional(GenericArguments.player(Text.of("player"))))
                .executor(new ApparateGroupCommand())
                .build();
        CommandSpec copyWorldSpec = CommandSpec.builder()
                .description(Text.of("Create an instance from an existing world for a player"))
                .permission("epicboundaries.command.createinstance")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("world"))),
                        GenericArguments.onlyOne(GenericArguments.vector3d(Text.of("position"))),
                        GenericArguments.optional(GenericArguments.player(Text.of("player"))))
                .executor(new CreateInstanceCommand())
                .build();
        CommandSpec copyWorldForGroupSpec = CommandSpec.builder()
                .description(Text.of("Create an instance from an existing world for a group"))
                .permission("epicboundaries.command.createinstanceforgroup")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("world"))),
                        GenericArguments.onlyOne(GenericArguments.vector3d(Text.of("position"))),
                        GenericArguments.optional(GenericArguments.player(Text.of("player"))))
                .executor(new CreateInstanceForGroupCommand())
                .build();

        Sponge.getCommandManager().register(this, apparateSpec, "apparate");
        Sponge.getCommandManager().register(this, apparateGroupSpec, "apparate-group");
        Sponge.getCommandManager().register(this, copyWorldSpec, "create-instance");
        Sponge.getCommandManager().register(this, copyWorldForGroupSpec, "create-instance-for-group");

        WorldAction worldAction = new WorldAction();
        Task.builder().execute(worldAction::removeDeprecatedInstances)
                .delay(5, TimeUnit.SECONDS).interval(60, TimeUnit.SECONDS)
                .name("Task deleting deprecated instances.").submit(this);

        logger.info("EPICBOUNDARIES initialized.");
    }

    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) {
        Sponge.getServiceManager().setProvider(this, IInstanceService.class, new InstanceService());
    }

    /**
     * Action occuring when a world has just been copied
     * @param event World copied event
     */
    @Listener
    public void onWorldCopy(CopyWorldEvent event) {
        String worldName = event.getWorldProperties().getWorldName();
        WorldAction worldAction = new WorldAction();
        worldAction.consumePlayerTransferQueue(worldName);
    }

    /**
     * Cancel every modify block events that "open" something
     * @param event Modify event
     */
    /*@Listener
    public void onModifyBlock(ChangeBlockEvent.Modify event, @First Player player) {
        if (!player.gameMode().equals(GameModes.CREATIVE)) {
            for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
                BlockSnapshot snapshot = transaction.getOriginal();
                snapshot.getState().getTrait("open").ifPresent(openProperty -> {
                    snapshot.getState().with(Keys.OPEN, true).ifPresent(blockState -> {
                        event.setCancelled(true);
                        player.sendBlockChange(snapshot.getPosition(), blockState);
                    });
                });
            }
        }
    }*/
}
