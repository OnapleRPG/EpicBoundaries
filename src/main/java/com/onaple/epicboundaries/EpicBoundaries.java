package com.onaple.epicboundaries;


import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;


@Plugin(id = "epicboundaries", name = "EpicBoundaries", version = "0.1.1")
public class EpicBoundaries {
    private static EpicBoundaries instance;
    public static EpicBoundaries getInstance() {
        return instance;
    }

    @Listener
    public void onServerStart(GameInitializationEvent event) {
        instance = this;
    }
}
