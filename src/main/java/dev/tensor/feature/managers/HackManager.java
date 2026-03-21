package dev.tensor.feature.managers;

import dev.tensor.backend.events.ClientTickEvent;
import dev.tensor.backend.events.KeyPressedEvent;
import dev.tensor.backend.events.Render2DEvent;
import dev.tensor.feature.hacks.*;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.Manager;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
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

    private final ArrayList<Hack> HACKS = new ArrayList<>();

    private final String hackPath = this.getConfigManager().getConfigPath() + "Hacks/";

    @Override
    public void load(Logger LOGGER) {
        LOGGER.info("Loaded: " + this.getName());

        this.addHack(new ClientMessages());
        this.addHack(new Commands());
        this.addHack(new FOV());
        this.addHack(new Fullbright());
        this.addHack(new GreenText());
        this.addHack(new KillAura());
        this.addHack(new NoHurtCam());
        this.addHack(new NoWeather());
        this.addHack(new TabGUI());

        this.postSortHacks();

        this.loadHacks(LOGGER);
    }

    private void loadHacks(Logger LOGGER) {
        try {
            if (!Files.exists(Paths.get(this.hackPath))) {
                Files.createDirectories(Paths.get(this.hackPath));
            }

            this.getHacks().forEach(hack -> {
                try {
                    hack.load(this.hackPath);
                } catch (Exception ignored) {
                    LOGGER.info("Failed to load hack: " + hack.NAME);
                }
            });

        } catch (Exception ignored) {
            LOGGER.info("Failed to load: " + this.getName());
        }
    }

    @Override
    public void save(Logger LOGGER) {
        LOGGER.info("Saved: " + this.getName());

        this.saveHacks(LOGGER);
    }

    private void saveHacks(Logger LOGGER) {
        this.getHacks().forEach(hack -> {
            try {
                hack.save(this.hackPath);
            } catch (Exception ingored) {
                LOGGER.info("Failed to save hack: " + hack.NAME);
            }
        });
    }

    public void addHack(Hack HACK) {
        this.HACKS.add(HACK);
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

    public boolean shouldSendMessagesGlobal() {
        return this.getHack(ClientMessages.class).isEnabled();
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onClientTickEvent(ClientTickEvent event) {
        if (this.failsNullCheck()) {
            return;
        }

        this.getEnabledHacks().forEach(Hack::onClientTick);
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onRender2DEvent(Render2DEvent event) {
        if (this.failsNullCheck()) {
            return;
        }

        this.getEnabledHacks().forEach(Hack::onRender2D);
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onKeyPressedEvent(KeyPressedEvent event) {
        this.getHacks().forEach(hack -> {
            if (hack.BIND == event.KEY_CODE) {
                hack.toggle();
            }
        });
    }
}
