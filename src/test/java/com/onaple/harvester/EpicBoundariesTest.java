package com.onaple.harvester;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.onaple.epicboundaries.event.CopyWorldEvent;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.mctester.api.junit.MinecraftRunner;
import org.spongepowered.mctester.internal.BaseTest;
import org.spongepowered.mctester.internal.event.StandaloneEventListener;
import org.spongepowered.mctester.junit.TestUtils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(MinecraftRunner.class)
public class EpicBoundariesTest extends BaseTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    public EpicBoundariesTest(TestUtils testUtils) {
        super(testUtils);
    }

    static private String defaultWorldName = "world";

    /**
     * Command /create-instance should copy a world and teleport player into the newly created
     */
    @Test
    public void testCreateInstance() throws Throwable {
        this.testUtils.runOnMainThread(() -> {
            this.testUtils.getThePlayer().offer(Keys.GAME_MODE, GameModes.CREATIVE);
            defaultWorldName = this.testUtils.getThePlayer().getWorld().getName();
        });
        this.testUtils.listenOneShot(() -> {
            this.testUtils.waitForAll();
            this.testUtils.getClient().sendMessage("/create-instance DIM-1 100 100 100");
            this.testUtils.sleepTicks(20);
        }, new StandaloneEventListener<>(CopyWorldEvent.class, (CopyWorldEvent event) -> {

        }));
        this.testUtils.waitForAll();
        this.testUtils.runOnMainThread(() -> {
            Assert.assertEquals(this.testUtils.getThePlayer().getWorld().getName().length(),
                    UUID.randomUUID().toString().length());
        });
    }

    /**
     * Command /apparate should teleport the player into the given world at the given position
     */
    @Test
    public void testApparate() throws Throwable {
        int expectedPosition = 100;
        this.testUtils.listenOneShot(() -> {
            this.testUtils.getClient().sendMessage("/apparate " + defaultWorldName + " " + expectedPosition + " 100 " + expectedPosition);
            this.testUtils.sleepTicks(10);
        }, new StandaloneEventListener<>(MoveEntityEvent.Teleport.class, (MoveEntityEvent.Teleport event) -> {

        }));
        this.testUtils.runOnMainThread(() -> {
            Vector3d playerPosition = this.testUtils.getThePlayer().getPosition();
            boolean xPositionMatches = (playerPosition.getX() > expectedPosition - 1) && (playerPosition.getX() < expectedPosition + 1);
            boolean zPositionMatches = (playerPosition.getZ() > expectedPosition - 1) && (playerPosition.getZ() < expectedPosition + 1);
            Assert.assertTrue(xPositionMatches && zPositionMatches);
            Assert.assertEquals(this.testUtils.getThePlayer().getWorld().getName(), defaultWorldName);
        });
    }
}
