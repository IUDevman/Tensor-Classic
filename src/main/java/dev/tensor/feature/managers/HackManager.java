package dev.tensor.feature.managers;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.constant.ClientTickEvent;
import dev.tensor.backend.events.constant.Render2DEvent;
import dev.tensor.backend.events.constant.Render3DEvent;
import dev.tensor.backend.events.variable.KeyPressedEvent;
import dev.tensor.backend.mixins.accessors.MinecraftAccessor;
import dev.tensor.feature.hacks.*;
import dev.tensor.feature.hacks.hud.*;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.EventPriority;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.imp.Manager;
import dev.tensor.misc.imp.ManagerPostOrder;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import net.minecraft.src.GuiGameOver;

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
    public void onLoad(TensorLogger LOGGER) {
        this.addHack(new AltAccounts());
        this.addHack(new AntiKnockback());
        this.addHack(new Armor());
        this.addHack(new AutoArmor());
        this.addHack(new AutoEat());
        this.addHack(new AutoGG());
        this.addHack(new AutoLogin());
        this.addHack(new AutoNine());
        this.addHack(new AutoReconnect());
        this.addHack(new AutoRespawn());
        this.addHack(new AutoSign());
        this.addHack(new AutoSword());
        this.addHack(new AutoTool());
        this.addHack(new AutoWalk());
        this.addHack(new BackwardsChat());
        this.addHack(new BlatantFly());
        this.addHack(new BlockHighlight());
        this.addHack(new BreadCrumbs());
        this.addHack(new BreakProgress());
        this.addHack(new CameraClip());
        this.addHack(new CancelPackets());
        this.addHack(new Capes());
        this.addHack(new Chams());
        this.addHack(new ChatTimestamp());
        this.addHack(new ClickGUI());
        this.addHack(new ClientMessages());
        this.addHack(new CombatLog());
        this.addHack(new CommandPreview());
        this.addHack(new Commands());
        this.addHack(new Coordinates());
        this.addHack(new CustomClouds());
        this.addHack(new CustomFog());
        this.addHack(new DeathCoords());
        this.addHack(new DiscordRPC());
        this.addHack(new EntityESP());
        this.addHack(new EpsteinESP());
        this.addHack(new FakePlayer());
        this.addHack(new FastBow());
        this.addHack(new FOV());
        this.addHack(new FPS());
        this.addHack(new FriendlyFire());
        this.addHack(new Fullbright());
        this.addHack(new GhastWarning());
        this.addHack(new GreenText());
        this.addHack(new Greeter());
        this.addHack(new HackList());
        this.addHack(new HephFreecam());
        this.addHack(new HotbarRefill());
        this.addHack(new HUDEditor());
        //this.addHack(new InfiniteDurability());
        this.addHack(new Inventory());
        this.addHack(new InventoryMove());
        this.addHack(new Jesus());
        this.addHack(new KillAura());
        this.addHack(new LiquidInteract());
        this.addHack(new LogoutSpots());
        this.addHack(new MainMenu());
        this.addHack(new MiddleClickFriend());
        this.addHack(new MiddleClickZoom());
        this.addHack(new ModInfo());
        this.addHack(new Nametags());
        this.addHack(new NewChunks());
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
        this.addHack(new Rainbow());
        this.addHack(new RoseFastPlace());
        this.addHack(new Rotations());
        this.addHack(new SafeWalk());
        this.addHack(new Scaffold());
        this.addHack(new Search());
        this.addHack(new SignWalker());
        this.addHack(new SlimeChunks());
        this.addHack(new Slug());
        this.addHack(new Sneak());
        this.addHack(new Spammer());
        this.addHack(new SPChat());
        this.addHack(new Speedometer());
        this.addHack(new Step());
        this.addHack(new Suffix());
        this.addHack(new TabGUI());
        this.addHack(new TCPNoDelay());
        this.addHack(new TextRadar());
        this.addHack(new Time());
        this.addHack(new Timer());
        this.addHack(new TimerMine());
        this.addHack(new Tracers());
        this.addHack(new Translate());
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
    public void onLoadPost(TensorLogger LOGGER) {
        this.postLoadHacks(LOGGER);
    }

    @Override
    public int getPostOrder() {
        return ManagerPostOrder.HACK_MANAGER.getValue();
    }

    @Override
    public void onSave(TensorLogger LOGGER) {
        this.getHacks().forEach(hack -> {
            if (hack.shouldDisableOnDisconnect() && hack.isEnabled()) {
                hack.toggle();
            }

            try {
                hack.save(this.hackPath);
            } catch (Exception ingored) {
                LOGGER.log("Failed to save config for hack: " + hack.NAME, TensorLogger.Level.ERROR);
            }
        });
    }

    //Do not run this until after the entire client is loaded
    public void postLoadHacks(TensorLogger LOGGER) {
        try {
            if (!Files.exists(Paths.get(this.hackPath))) {
                Files.createDirectories(Paths.get(this.hackPath));
            }

            this.getHacks().forEach(hack -> {
                try {
                    hack.load(this.hackPath);
                } catch (Exception ignored) {
                    LOGGER.log("Failed to load hack: " + hack.NAME, TensorLogger.Level.WARN);
                }
            });

        } catch (Exception ignored) {
            LOGGER.log("Failed to load config for: " + this.getName(), TensorLogger.Level.ERROR);
        }
    }

    public void postSortHacks() {
        this.HACKS.sort(Comparator.comparing(hack -> hack.NAME));

        this.ENABLED_RENDER_3D_HACKS.sort(Comparator.comparing(hack -> ((Render3DHack) hack).getRenderOrderInt()).reversed());

        this.ENABLED_RENDER_HUD_HACKS.sort(Comparator.comparing(hack -> ((HUDObject) hack).getRenderOrderInt()).reversed());
    }

    public void onHackToggled(Hack hack, boolean enabled) {
        if (enabled) {
            if (!this.ENABLED_HACKS.contains(hack)) {
                this.ENABLED_HACKS.add(hack);
            }

            if (hack instanceof Render3DHack && !this.ENABLED_RENDER_3D_HACKS.contains((Render3DHack) hack)) {
                this.ENABLED_RENDER_3D_HACKS.add((Render3DHack) hack);
                this.ENABLED_RENDER_3D_HACKS.sort(Comparator.comparing(hack1 -> ((Render3DHack) hack1).getRenderOrderInt()).reversed());
            }

            if (hack instanceof HUDObject && !this.ENABLED_RENDER_HUD_HACKS.contains(hack)) {
                this.ENABLED_RENDER_HUD_HACKS.add(hack);
                this.ENABLED_RENDER_HUD_HACKS.sort(Comparator.comparing(hack1 -> ((HUDObject) hack1).getRenderOrderInt()).reversed());
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
    public final EventAction<ClientTickEvent> clientTickEventAction = this.addEventAction(this, new EventAction<>(ClientTickEvent.class, EventPriority.High, event -> {
        if (this.failsNullCheck()) {
            return;
        }

        if (this.getMinecraft().currentScreen instanceof GuiGameOver) {
            ArrayList<Hack> toDisable = new ArrayList<>();

            this.getEnabledHacks().forEach(hack -> {
                if (hack.shouldDisableOnDeath()) {
                    toDisable.add(hack);
                }
            });

            toDisable.forEach(hack -> hack.setEnabled(false));
        }

        this.getEnabledHacks().forEach(hack -> {
            this.clientTickEventAction.setDeclaringClass(hack.NAME);
            hack.onClientTick();
        });

        this.clientTickEventAction.setDeclaringClass(this.getClass());
    }));

    @SuppressWarnings("unused")
    public final EventAction<Render2DEvent> render2DEventAction = this.addEventAction(this, new EventAction<>(Render2DEvent.class, event -> {
        if (this.failsNullCheck()) {
            return;
        }

        ClickGUI clickGUI = this.getHack(ClickGUI.class);

        if (clickGUI.isEnabled() && !clickGUI.renderHUD.getValue()) {
            return;
        }

        HUDEditor hudEditor = this.getHack(HUDEditor.class);

        if (hudEditor.isRenderingInEditor()) {
            return;
        }

        this.getEnabledHudObjects().forEach(hack -> {
            this.render2DEventAction.setDeclaringClass(hack.NAME);
            ((HUDObject) hack).render();
        });

        this.render2DEventAction.setDeclaringClass(this.getClass());
    }));

    @SuppressWarnings("unused")
    public final EventAction<Render3DEvent> render3DEventAction = this.addEventAction(this, new EventAction<>(Render3DEvent.class, event -> {
        if (this.failsNullCheck()) {
            return;
        }

        float renderPartialTicks = ((MinecraftAccessor) this.getMinecraft()).tensor_getTimer().renderPartialTicks;

        this.getEnabledRender3DObjectHacks().forEach(hack -> {
            this.render3DEventAction.setDeclaringClass(hack.NAME);
            hack.renderIn3D(renderPartialTicks);
        });

        this.render3DEventAction.setDeclaringClass(this.getClass());
    }));

    @SuppressWarnings("unused")
    public final EventAction<KeyPressedEvent> keyPressedEventAction = this.addEventAction(this, new EventAction<>(KeyPressedEvent.class, event -> {
        this.getHacks().forEach(hack -> {
            if (hack.BIND == event.KEY_CODE) {
                this.keyPressedEventAction.setDeclaringClass(hack.NAME);
                hack.toggle();
            }
        });

        this.keyPressedEventAction.setDeclaringClass(this.getClass());
    }));

    @SuppressWarnings("unused")
    private final EventAction<ChangeWorldEvent> changeWorldEventAction = this.addEventAction(this, new EventAction<>(ChangeWorldEvent.class, EventPriority.Highest, event -> {
        ArrayList<Hack> toDisable = new ArrayList<>();

        this.getEnabledHacks().forEach(hack -> {
            if (hack.shouldDisableOnWorldChange()) {
                toDisable.add(hack);
            }
        });

        toDisable.forEach(hack -> hack.setEnabled(false));
    }));
}
