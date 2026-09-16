package dev.tensor.feature.managers;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.constant.ClientTickEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.EventPriority;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.imp.Manager;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hoosiers
 * @since 05-04-2026
 */

public final class EntityListManager implements Manager {

    @Override
    public String getName() {
        return "EntityListManager";
    }

    private final ArrayList<Entity> FILTERED_LOADED_ENTITIES = new ArrayList<>();

    @Override
    public void load(TensorLogger LOGGER) {
        LOGGER.log("Loaded: " + this.getName(), TensorLogger.Level.INFO);
    }

    @Override
    public void save(TensorLogger LOGGER) {
        LOGGER.log("Saved: " + this.getName(), TensorLogger.Level.INFO);
    }

    @SuppressWarnings("unused")
    private final EventAction<ChangeWorldEvent> changeWorldEventAction = this.addEventAction(this, new EventAction<>(ChangeWorldEvent.class, EventPriority.Highest, event -> this.FILTERED_LOADED_ENTITIES.clear()));

    @SuppressWarnings("unused")
    private final EventAction<ClientTickEvent> clientTickEventAction = this.addEventAction(this, new EventAction<>(ClientTickEvent.class, EventPriority.Highest, event -> {
        this.FILTERED_LOADED_ENTITIES.clear();

        if (this.failsNullCheck()) {
            return;
        }

        List entities = this.getWorld().loadedEntityList;

        for (Object objectEntity : entities) {
            Entity entity = (Entity) objectEntity;

            if (entity == null) {
                continue;
            }

            if (entity instanceof EntityLiving) {
                if (entity.ticksExisted < 1) {
                    continue;
                }

                if (!entity.isEntityAlive()) {
                    continue;
                }
            }

            this.FILTERED_LOADED_ENTITIES.add(entity);
        }
    }));

    public ArrayList<Entity> getFilteredEntities() {
        return this.FILTERED_LOADED_ENTITIES;
    }
}
