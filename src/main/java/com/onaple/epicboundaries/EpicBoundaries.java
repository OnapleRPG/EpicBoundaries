package com.onaple.epicboundaries;


import com.onaple.epicboundaries.commands.ApparateCommand;
import com.onaple.epicboundaries.commands.ApparateGroupCommand;
import com.onaple.epicboundaries.commands.CreateInstanceCommand;
import com.onaple.epicboundaries.commands.CreateInstanceForGroupCommand;
import com.onaple.epicboundaries.data.access.InstanceDao;
import com.onaple.epicboundaries.event.ApparateEvent;
import com.onaple.epicboundaries.event.CopyWorldEvent;
import com.onaple.epicboundaries.event.context.EpicKeys;
import com.onaple.epicboundaries.service.IInstanceService;
import com.onaple.epicboundaries.service.InstanceService;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKey;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Plugin(id = "epicboundaries", name = "EpicBoundaries", version = "0.2",
        description = "Plugin managing world instances",
        authors = {"zessirb", "Selki"},
        dependencies = {@Dependency(id = "crowdbinding", optional = true), @Dependency(id = "nt-rpg", optional = true)})
public class EpicBoundaries {
    private static Logger logger;

    @Inject
    private void setLogger(Logger logger) {
        EpicBoundaries.logger = logger;
    }

    public static Logger getLogger() {
        return logger;
    }

    private static EpicBoundaries instance;
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

    public static EpicBoundaries getInstance() {
        return instance;
    }

    private static WorldAction worldAction;
    @Inject
    public void setWorldAction(WorldAction worldAction){
        EpicBoundaries.worldAction = worldAction;
    }
    public static WorldAction getWorldAction(){
        return worldAction;
    }

    private static GroupHandler groupHandler;
    public void setGroupHandler(GroupHandler groupHandler){
        EpicBoundaries.groupHandler = groupHandler;
    }
    public static GroupHandler getGroupHandler(){
        return EpicBoundaries.groupHandler;
    }

    @Listener
    public void onServerStart(GameInitializationEvent event) {
        instanceDao.createTableIfNotExist();
        instance = this;
        new EpicKeys();
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
     *
     * @param event World copied event
     */

    @Listener
    public void onWorldCopy(CopyWorldEvent event) {
        String worldName = event.getWorldProperties().getWorldName();
        WorldAction worldAction = new WorldAction();
        worldAction.consumePlayerTransferQueue(worldName);
    }

    @Listener
    public void onApparate(ApparateEvent event) {
        Optional<Location> locationOpt = event.getContext().get(EpicKeys.APPARITION_LOCATION);
        if (locationOpt.isPresent()) {
            event.getContext().get(EpicKeys.APPARATED_PLAYERS).get().forEach(
                    o ->  ((Player) o).sendMessage(Text.builder().color(TextColors.YELLOW)
                    .append(Text.builder("[Instance]").build())
                    .append(Text.builder(" you are in an instance of a world, it will be delete once you leave it").build())
                    .build()
                    )
            );
        }
    }
}
