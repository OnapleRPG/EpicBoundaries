package com.onaple.epicboundaries.event.context;

import org.spongepowered.api.event.cause.EventContextKey;


import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.List;

/**
 * @autor Hugo on 14/05/19.
 */
public class EpicKeys {

    public static EventContextKey<Location> APPARITION_LOCATION;
    public static EventContextKey<List> APPARATED_PLAYERS;
    public static EventContextKey<World> WORLD_ORIGIN;

    public EpicKeys(){
        APPARITION_LOCATION = EventContextKey.<Location>builder(Location.class)
                .id("apparitionLocation")
                .name("Apparition location")
                .build();
        APPARATED_PLAYERS = EventContextKey.<List>builder(List.class)
                .id("apparatedPlayers")
                .name("Apparated players")
                .build();
    }


}
