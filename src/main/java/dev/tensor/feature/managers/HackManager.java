package dev.tensor.feature.managers;

import dev.tensor.backend.events.ClientTickEvent;
import dev.tensor.backend.events.KeyPressedEvent;
import dev.tensor.backend.events.Render2DEvent;
import dev.tensor.backend.events.Render3DEvent;
import dev.tensor.feature.hacks.*;
import dev.tensor.feature.hacks.hud.*;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.Manager;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.Render3DObject;
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

        this.addHack(new AntiKnockback());
        this.addHack(new Armor());
        this.addHack(new AutoEat());
        this.addHack(new AutoLogin());
        this.addHack(new AutoRespawn());
        this.addHack(new AutoTool());
        this.addHack(new AutoWalk());
        this.addHack(new BackwardsChat());
        this.addHack(new ClickGUI());
        this.addHack(new ClientMessages());
        this.addHack(new CommandPreview());
        this.addHack(new Commands());
        this.addHack(new Coordinates());
        this.addHack(new CustomClouds());
        this.addHack(new CustomFog());
        this.addHack(new DeathCoords());
        this.addHack(new EntityESP());
        this.addHack(new FakePlayer());
        this.addHack(new FOV());
        this.addHack(new Fullbright());
        this.addHack(new GreenText());
        this.addHack(new Greeter());
        this.addHack(new HackList());
        this.addHack(new HUDEditor());
        this.addHack(new Inventory());
        this.addHack(new InventoryMove());
        this.addHack(new Jesus());
        this.addHack(new KillAura());
        this.addHack(new LiquidInteract());
        this.addHack(new ModInfo());
        this.addHack(new Nametags());
        this.addHack(new NoFall());
        this.addHack(new NoHurtCam());
        this.addHack(new NoItemRender());
        this.addHack(new NoOverlay());
        this.addHack(new NoViewBob());
        this.addHack(new NoWeather());
        this.addHack(new PortalInventory());
        this.addHack(new SafeWalk());
        this.addHack(new Search());
        this.addHack(new Slug());
        this.addHack(new Sneak());
        this.addHack(new Speedometer());
        this.addHack(new Step());
        this.addHack(new Suffix());
        this.addHack(new TabGUI());
        this.addHack(new Timer());
        this.addHack(new TimerMine());
        this.addHack(new Tracers());
        this.addHack(new Velocity());
        this.addHack(new Welcomer());
        this.addHack(new WolfOwner());
        this.addHack(new XCarry());
        this.addHack(new Xray());

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

        ClickGUI clickGUI = this.getHack(ClickGUI.class);

        if (clickGUI.isEnabled()) {
            clickGUI.toggle();
        }

        HUDEditor hudEditor = this.getHack(HUDEditor.class);

        if (hudEditor.isEnabled()) {
            hudEditor.toggle();
        }

        FakePlayer fakePlayer = this.getHack(FakePlayer.class);

        if (fakePlayer.isEnabled()) {
            fakePlayer.toggle();
        }

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

    public ArrayList<Hack> getEnabledRender3DObjectHacks() {
        return this.HACKS.stream().filter(Hack::isEnabled).filter(hack -> hack instanceof Render3DObject).sorted(Comparator.comparing(hack -> ((Render3DObject) hack).getRenderOrder()).reversed()).collect(Collectors.toCollection(ArrayList::new));
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

        ClickGUI clickGUI = this.getHack(ClickGUI.class);

        if (clickGUI.isEnabled() && !clickGUI.renderHUD.getValue()) {
            return;
        }

        this.getEnabledHacks().forEach(hack -> {
            if (hack instanceof HUDObject) {
                ((HUDObject) hack).render();
            }
        });
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onRender3DEvent(Render3DEvent event) {
        if (this.failsNullCheck()) {
            return;
        }

        this.getEnabledRender3DObjectHacks().forEach(hack -> ((Render3DObject) hack).renderIn3D());
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
