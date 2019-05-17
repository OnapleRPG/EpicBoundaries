package com.onaple.epicboundaries.event;




import com.onaple.epicboundaries.EpicBoundaries;
import com.onaple.epicboundaries.event.context.EpicKeys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.teleport.TeleportTypes;
import org.spongepowered.api.event.impl.AbstractEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collections;
import java.util.List;

/**
 * @autor Hugo on 14/05/19.
 */
public class ApparateEvent extends AbstractEvent implements Cancellable {

    private EventContext eventContext;
    private Object source;
    private boolean cancelled = false;


    public ApparateEvent(Player source, Location<World> location) {
        this.source = source;
        eventContext = EventContext.builder()
                .add(EventContextKeys.TELEPORT_TYPE, TeleportTypes.PLUGIN)
                .add(EpicKeys.APPARITION_LOCATION, location)
                .add(EpicKeys.APPARATED_PLAYERS, Collections.singletonList(source))
                .build();
    }

    public ApparateEvent(Object source, Location<World> location, List<Player> playerList) {
        this.source = source;
        eventContext = EventContext.builder()
                .add(EventContextKeys.TELEPORT_TYPE, TeleportTypes.PLUGIN)
                .add(EpicKeys.APPARITION_LOCATION, location)
                .add(EpicKeys.APPARATED_PLAYERS, playerList)
                .build();
    }

    @Override
    public Cause getCause() {
        return Cause.of(eventContext, EpicBoundaries.getInstance());
    }

    @Override
    public Object getSource() {
        return source;
    }

    @Override
    public EventContext getContext() {
        return eventContext;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled=true;
    }
}
