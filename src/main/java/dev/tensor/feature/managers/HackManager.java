package dev.tensor.feature.managers;

import dev.tensor.backend.events.ClientTickEvent;
import dev.tensor.feature.hacks.ClientMessages;
import dev.tensor.feature.hacks.Commands;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.Manager;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public final class HackManager implements Manager {

    @Override
    public String getName() {
        return "HackManager";
    }

    public final ArrayList<Hack> HACKS = new ArrayList<>();

    @Override
    public void load(Logger LOGGER) {
        LOGGER.info("Loaded: " + this.getName());

        this.addHack(new ClientMessages());
        this.addHack(new Commands());

        this.postSortHacks();
    }

    public void addHack(Hack HACK) {
        this.HACKS.add(HACK);

        HACK.launchStartUp();
    }

    public ArrayList<Hack> getHacks() {
        return this.HACKS;
    }

    public ArrayList<Hack> getHacksInCategory(Category CATEGORY) {
        return this.getHacks().stream().filter(hack -> hack.CATEGORY.equals(CATEGORY)).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Hack> getEnabledHacks() {
        return this.getHacks().stream().filter(Hack::isEnabled).collect(Collectors.toCollection(ArrayList::new));
    }

    @SuppressWarnings("unchecked")
    public <T extends Hack> T getHack(Class<T> aClass) {
        return (T) this.getHacks().stream().filter(hack -> hack.getClass().equals(aClass)).findFirst().orElse(null);
    }

    public Hack getHack(String NAME) {
        return this.getHacks().stream().filter(hack -> hack.NAME.equalsIgnoreCase(NAME)).findFirst().orElse(null);
    }

    private void postSortHacks() {
        this.HACKS.sort(Comparator.comparing(hack -> hack.NAME));
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onClientTickEvent(ClientTickEvent event) {
        if (this.failsNullCheck()) {
            return;
        }

        this.getEnabledHacks().forEach(Hack::onClientTick);
    }

    @Override
    public void save(Logger LOGGER) {
        LOGGER.info("Saved: " + this.getName());
    }

    public boolean shouldSendMessagesGlobal() {
        return this.getHack(ClientMessages.class).isEnabled();
    }
}
