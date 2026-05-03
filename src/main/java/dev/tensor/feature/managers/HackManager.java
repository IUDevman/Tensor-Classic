package dev.tensor.feature.managers;

import dev.tensor.backend.events.constant.ClientTickEvent;
import dev.tensor.backend.events.constant.Render2DEvent;
import dev.tensor.backend.events.constant.Render3DEvent;
import dev.tensor.backend.events.variable.KeyPressedEvent;
import dev.tensor.backend.mixins.accessors.MinecraftAccessor;
import dev.tensor.feature.hacks.*;
import dev.tensor.feature.hacks.hud.*;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.Manager;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

    private final HashMap<Class<? extends Hack>, Hack> HACK_CALL = new HashMap<>();
    private final HashMap<String, Hack> HACK_CALL_STRING = new HashMap<>();

    private final ArrayList<Hack> ENABLED_HACKS = new ArrayList<>();
    private final ArrayList<Render3DHack> ENABLED_RENDER_3D_HACKS = new ArrayList<>();
    private final ArrayList<Hack> ENABLED_RENDER_HUD_HACKS = new ArrayList<>();

    private final String hackPath = this.getConfigManager().getConfigPath() + "Hacks/";

    @Override
    public void load(Logger LOGGER) {
        LOGGER.info("Loaded: " + this.getName());

        this.addHack(new AntiKnockback());
        this.addHack(new Armor());
        this.addHack(new AutoEat());
        this.addHack(new AutoGG());
        this.addHack(new AutoLogin());
        this.addHack(new AutoReconnect());
        this.addHack(new AutoRespawn());
        this.addHack(new AutoSign());
        this.addHack(new AutoSword());
        this.addHack(new AutoTool());
        this.addHack(new AutoWalk());
        this.addHack(new BackwardsChat());
        this.addHack(new BlockHighlight());
        this.addHack(new BreadCrumbs());
        this.addHack(new BreakProgress());
        this.addHack(new CameraClip());
        this.addHack(new Capes());
        this.addHack(new ChatTimestamp());
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
        this.addHack(new FastBow());
        this.addHack(new FOV());
        this.addHack(new FPS());
        this.addHack(new Fullbright());
        this.addHack(new GreenText());
        this.addHack(new Greeter());
        this.addHack(new HackList());
        this.addHack(new HephFreecam());
        this.addHack(new HUDEditor());
        this.addHack(new InfiniteDurability());
        this.addHack(new Inventory());
        this.addHack(new InventoryMove());
        this.addHack(new Jesus());
        this.addHack(new KillAura());
        this.addHack(new LiquidInteract());
        this.addHack(new LogoutSpots());
        this.addHack(new MiddleClickFriend());
        this.addHack(new ModInfo());
        this.addHack(new Nametags());
        this.addHack(new NoBreakDelay());
        this.addHack(new NoFall());
        this.addHack(new NoHurtCam());
        this.addHack(new NoItemRender());
        this.addHack(new NoOverlay());
        this.addHack(new NoParticles());
        this.addHack(new NoViewBob());
        this.addHack(new NoWeather());
        this.addHack(new Nuker());
        this.addHack(new PhysicsSpammer());
        this.addHack(new Platform());
        this.addHack(new PortalInventory());
        this.addHack(new Radar());
        this.addHack(new RoseFastPlace());
        this.addHack(new Rotations());
        this.addHack(new SafeWalk());
        this.addHack(new Scaffold());
        this.addHack(new Search());
        this.addHack(new Slug());
        this.addHack(new Sneak());
        this.addHack(new Speedometer());
        this.addHack(new Step());
        this.addHack(new Suffix());
        this.addHack(new TabGUI());
        this.addHack(new TCPNoDelay());
        this.addHack(new Time());
        this.addHack(new Timer());
        this.addHack(new TimerMine());
        this.addHack(new Tracers());
        this.addHack(new Velocity());
        this.addHack(new VisualRange());
        this.addHack(new Waypoints());
        this.addHack(new Welcomer());
        this.addHack(new WolfOwner());
        this.addHack(new XCarry());
        this.addHack(new Xray());
        this.addHack(new YawLock());

        this.postSortHacks();
    }

    @Override
    public void loadPost(Logger LOGGER) {
        this.postLoadHacks(LOGGER);
    }

    //Do not run this until after the entire client is loaded
    public void postLoadHacks(Logger LOGGER) {
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

        this.getHacks().forEach(hack -> {
            if (hack.shouldDisableOnDisconnect() && hack.isEnabled()) {
                hack.toggle();
            }

            try {
                hack.save(this.hackPath);
            } catch (Exception ingored) {
                LOGGER.info("Failed to save hack: " + hack.NAME);
            }
        });
    }

    private void postSortHacks() {
        this.HACKS.sort(Comparator.comparing(hack -> hack.NAME));

        this.ENABLED_RENDER_3D_HACKS.sort(Comparator.comparing(hack -> ((Render3DHack) hack).getRenderOrder().value()).reversed());

        this.ENABLED_RENDER_HUD_HACKS.sort(Comparator.comparing(hack -> ((HUDObject) hack).getRenderOrder().value()).reversed());
    }

    public void onHackToggled(Hack hack, boolean enabled) {
        if (enabled) {
            if (!this.ENABLED_HACKS.contains(hack)) {
                this.ENABLED_HACKS.add(hack);
            }

            if (hack instanceof Render3DHack && !this.ENABLED_RENDER_3D_HACKS.contains((Render3DHack) hack)) {
                this.ENABLED_RENDER_3D_HACKS.add((Render3DHack) hack);
                this.ENABLED_RENDER_3D_HACKS.sort(Comparator.comparing(hack1 -> ((Render3DHack) hack1).getRenderOrder().value()).reversed());
            }

            if (hack instanceof HUDObject && !this.ENABLED_RENDER_HUD_HACKS.contains(hack)) {
                this.ENABLED_RENDER_HUD_HACKS.add(hack);
                this.ENABLED_RENDER_HUD_HACKS.sort(Comparator.comparing(hack1 -> ((HUDObject) hack1).getRenderOrder().value()).reversed());
            }

            return;
        }

        this.ENABLED_HACKS.remove(hack);

        if (hack instanceof Render3DHack) {
            this.ENABLED_RENDER_3D_HACKS.remove(hack);
        }

        if (hack instanceof HUDObject) {
            this.ENABLED_RENDER_HUD_HACKS.remove(hack);
        }
    }

    public void addHack(Hack HACK) {
        this.HACKS.add(HACK);
        this.HACK_CALL.put(HACK.getClass(), HACK);
        this.HACK_CALL_STRING.put(HACK.NAME.toLowerCase(), HACK);
    }

    public ArrayList<Hack> getHacks() {
        return this.HACKS;
    }

    public ArrayList<Hack> getHacksInCategory(Category CATEGORY) {
        return this.getHacks().stream().filter(hack -> hack.CATEGORY.equals(CATEGORY)).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Hack> getEnabledHacks() {
        return this.ENABLED_HACKS;
    }

    public ArrayList<Hack> getEnabledHudObjects() {
        return this.ENABLED_RENDER_HUD_HACKS;
    }

    public ArrayList<Render3DHack> getEnabledRender3DObjectHacks() {
        return this.ENABLED_RENDER_3D_HACKS;
    }

    @SuppressWarnings("unchecked")
    public <T extends Hack> T getHack(Class<T> aClass) {
        return (T) this.HACK_CALL.get(aClass);
    }

    public Hack getHack(String NAME) {
        return this.HACK_CALL_STRING.get(NAME.toLowerCase());
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

        this.getEnabledHudObjects().forEach(hack -> ((HUDObject) hack).render());
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onRender3DEvent(Render3DEvent event) {
        if (this.failsNullCheck()) {
            return;
        }

        float renderPartialTicks = ((MinecraftAccessor) this.getMinecraft()).tensor_getTimer().renderPartialTicks;

        this.getEnabledRender3DObjectHacks().forEach(hack -> hack.renderIn3D(renderPartialTicks));
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
