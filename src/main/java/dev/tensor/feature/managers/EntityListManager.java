package dev.tensor.feature.managers;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.constant.ClientTickEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.event.imp.EventPriority;
import dev.tensor.misc.imp.Manager;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import org.slf4j.Logger;

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
    public void load(Logger LOGGER) {
        LOGGER.info("Loaded: " + this.getName());
    }

    @Override
    public void save(Logger LOGGER) {
        LOGGER.info("Saved: " + this.getName());
    }

    @SuppressWarnings("unused")
    @EventTarget(priority = EventPriority.Highest)
    public void onChangeWorldEvent(ChangeWorldEvent event) {
        this.FILTERED_LOADED_ENTITIES.clear();
    }

    @SuppressWarnings("unused")
    @EventTarget(priority = EventPriority.Highest)
    public void onClientTick(ClientTickEvent event) {
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
    }

    public ArrayList<Entity> getFilteredEntities() {
        return this.FILTERED_LOADED_ENTITIES;
    }
}
