package com.onaple.epicboundaries;


import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.BookView;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import javax.inject.Inject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Plugin(id = "epicboundaries", name = "EpicBoundaries", version = "0.1.1")
public class EpicBoundaries {
    private static EpicBoundaries instance;
    public static EpicBoundaries getInstance() {
        return instance;
    }

    @Inject
    @ConfigDir(sharedRoot=true)
    private Path configDir;

/*
    @Inject
    private ConfigurationHandler configurationHandler;

    public int loadConfig() throws ObjectMappingException {
          initDefaultConfig();
          return configurationHandler.readDialogsConfiguration(configurationHandler.loadConfiguration(configDir + "/epicboundaries/"));
    }

    private void initDefaultConfig(){
        if (Files.notExists(Paths.get(configDir+"/epicboundaries/"))){
            Optional<PluginContainer> pluginContainer = Sponge.getPluginManager().getPlugin("epicboundaries");
            if (pluginContainer.isPresent()) {
                Optional<Asset> dialogDefaultConfigFile = pluginContainer.get().getAsset("example.conf");
                getLogger().info("No dialogs files found in /config/storryteller, a default config will be loaded");
                if (dialogDefaultConfigFile.isPresent()) {
                    try {
                        dialogDefaultConfigFile.get().copyToDirectory(Paths.get(configDir+"/epicboundaries/"));
                    } catch (IOException e) {
                        logger.error("Error while initializing default config : ".concat(e.getMessage()));
                    }
                } else {
                    logger.warn("Default config not found.");
                }
            } else {
                logger.warn("Plugin was not able to reference itself !");
            }
        }
    }



    private static ObjectiveDao objectiveDao;
    @Inject
    private void setObjeExceptionctiveDao(ObjectiveDao objectiveDao) {
        EpicBoundaries.objectiveDao = objectiveDao;
    }
    public static ObjectiveDao getObjectiveDao() {
        return objectiveDao;
    }

    @Inject
    private KillCountDao killCountDao;

    private static BookGenerator bookGenerator;

    private static Logger logger;
    @Inject
    private void setLogger(Logger logger) {
        EpicBoundaries.logger = logger;
    }
    public static Logger getLogger() {
        return logger;
    }

    @Inject
    private void setBookGenerator(BookGenerator bookGenerator){ this.bookGenerator = bookGenerator; }
    public static BookGenerator getBookGenerator(){
        return bookGenerator;
    }
    @Inject
    private DialogAction dialogAction;*/

    @Listener
    public void onServerStart(GameInitializationEvent event) {
        instance = this;
        /*try {
          getLogger().info("Dialogs configuration loaded. " +loadConfig()+ " dialogs loaded");
        } catch (ObjectMappingException e) {
            logger.error("Error while loading dialogs configuration : " + e.getMessage());
        }

        CommandSpec dialogSpec = CommandSpec.builder()
                .description(Text.of("Open book command"))
                .permission("epicboundaries.command.read")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("dialog"))),
                        GenericArguments.optional(GenericArguments.player(Text.of("player"))))
                .executor(new OpenBookCommand())
                .build();
        CommandSpec reloadSpec = CommandSpec.builder()
                .description(Text.of("Reload epicboundaries configuration"))
                .permission("epicboundaries.command.reload")
                .executor(new ReloadCommand())
                .build();

        CommandSpec getObjectiveSpec = CommandSpec.builder()
                .description(Text.of("Get player's actual objectives state"))
                .permission("epicboundaries.command.objectives")
                .arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
                .executor(new GetObjectiveCommand())
                .build();

        CommandSpec setObjectiveSpec = CommandSpec.builder()
                .description(Text.of("set player's objectives state"))
                .permission("epicboundaries.command.objectives")
                .arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))
                        ,GenericArguments.onlyOne(GenericArguments.string(Text.of("objective")))
                        ,GenericArguments.onlyOne(GenericArguments.integer(Text.of("value"))))
                .executor(new SetObjectivesCommand())
                .build();

        Sponge.getCommandManager().register(this, dialogSpec, "dialog");
        Sponge.getCommandManager().register(this, reloadSpec, "reload-epicboundaries");
        Sponge.getCommandManager().register(this, getObjectiveSpec, "get-objectives");
        Sponge.getCommandManager().register(this, setObjectiveSpec, "set-objective");
        logger.info("STORYTELLER initialized.");*/
    }

    /**
     * Get the current world
     * @return the world
     *//*
    public static World getWorld(){
        Optional<World> worldOptional = Sponge.getServer().getWorld("world");
        if(worldOptional.isPresent()){
            return worldOptional.get();
        }
        return null;
    }*/
}
