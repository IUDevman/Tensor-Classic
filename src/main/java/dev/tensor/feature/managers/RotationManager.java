package dev.tensor.feature.managers;

import dev.tensor.backend.events.variable.Packet10FlyingEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.helpers.rotation.Rotation;
import dev.tensor.misc.imp.Manager;
import net.minecraft.src.Packet12PlayerLook;
import org.slf4j.Logger;

import java.util.Comparator;
import java.util.HashMap;

/**
 * @author Hoosiers
 * @since 04-22-2026
 */

public final class RotationManager implements Manager {

    private final HashMap<Integer, Rotation> rotationQueue = new HashMap<>();

    private Mode rotationMode = Mode.None;

    @Override
    public String getName() {
        return "RotationManager";
    }

    @Override
    public void load(Logger LOGGER) {
        LOGGER.info("Loaded: " + this.getName());
        this.rotationQueue.clear();
    }

    @Override
    public void save(Logger LOGGER) {
        LOGGER.info("Saved: " + this.getName());
        this.rotationQueue.clear();
    }

    public void setRotationMode(Mode mode) {
        if (!mode.equals(Mode.Intercept)) {
            this.rotationQueue.clear();
        }

        this.rotationMode = mode;
    }

    public void addToRotationQueue(Rotation rotation, int priority) {
        switch (this.rotationMode) {
            case None:
                break;
            case Intercept:
                this.rotationQueue.put(priority, rotation);
                break;
            case Packet:
                this.sendPacket(new Packet12PlayerLook(rotation.yaw, rotation.pitch, true));
                break;
        }
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onPacket10FlyingEvent(Packet10FlyingEvent event) {
        if (this.rotationQueue.isEmpty()) {
            return;
        }

        int maxPriority = this.rotationQueue.keySet().stream().max(Comparator.comparing(integer -> integer)).get();

        Rotation maxPriorityRotation = this.rotationQueue.get(maxPriority);

        if (maxPriorityRotation != null) {
            if (event.isCancelled()) {
                this.sendPacket(new Packet12PlayerLook(maxPriorityRotation.yaw, maxPriorityRotation.pitch, true));

            } else {
                event.packet.pitch = maxPriorityRotation.pitch;
                event.packet.yaw = maxPriorityRotation.yaw;
            }

            this.rotationQueue.clear();
        }
    }

    public enum Mode {
        None,
        Intercept,
        Packet
    }
}
