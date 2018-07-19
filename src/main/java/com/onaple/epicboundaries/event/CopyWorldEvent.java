package com.onaple.epicboundaries.event;

import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;
import org.spongepowered.api.event.world.TargetWorldEvent;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

public class CopyWorldEvent extends AbstractEvent {
    private final Cause cause;
    private final WorldProperties worldProperties;

    public CopyWorldEvent(Cause cause, WorldProperties worldProperties) {
        this.cause = cause;
        this.worldProperties = worldProperties;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    public WorldProperties getWorldProperties() {
        return worldProperties;
    }
}
