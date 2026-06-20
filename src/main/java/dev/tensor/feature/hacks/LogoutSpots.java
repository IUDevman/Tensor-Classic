package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.variable.ChatReceivedEvent;
import dev.tensor.backend.events.variable.PlayerJoinLeaveWorldEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.helpers.block.BoxPos;
import dev.tensor.misc.helpers.logoutspot.LogoutSpot;
import dev.tensor.misc.helpers.logoutspot.LogoutSpotRenderer;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.*;
import net.minecraft.src.EntityOtherPlayerMP;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Hoosiers
 * @since 04-26-2026
 */

@Hack.Info(name = "LogoutSpots", category = Category.Combat)
public final class LogoutSpots extends Render3DHack {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.LogoutSpots;
    }

    @Override
    public boolean overridesBlockGL() {
        return true;
    }

    public final GeneralButtonSetting clearLogged = new GeneralButtonSetting("Clear Logged Players", GeneralButtonSetting.ButtonType.CLICK_ACTION) {

        @Override
        public void onChange() {
            LogoutSpots logoutSpots = this.getHackManager().getHack(LogoutSpots.class);

            if (logoutSpots != null) {
                logoutSpots.clearLogoutSpots();

                if (logoutSpots.isEnabled() && !this.failsNullCheck()) {
                    this.sendClientMessageWithHeader("LogoutSpots: Cleared logged players!");
                }
            }
        }
    };
    public final BooleanSetting title = new BooleanSetting("Title", true);
    public final BooleanSetting showCoords = new BooleanSetting("Show Coords", true);
    public final BooleanSetting showDistance = new BooleanSetting("Show Distance", false);
    public final BooleanSetting showTime = new BooleanSetting("Time", false);
    public final DoubleSetting tagScale = new DoubleSetting("Tag Scale", 1.00, 0.10, 2.00, 2);
    public final IntegerSetting tagDistanceMultiplier = new IntegerSetting("Tag Distance Multiplier", 8, 2, 10);
    public final BooleanSetting tagBackground = new BooleanSetting("Tag Background", false);
    public final ColorNoAlphaSetting tagTextColor = new ColorNoAlphaSetting("Text Color", LogoutSpotRenderer.defaultTagTextColor);
    public final ColorSetting tagBackgroundColor = new ColorSetting("Background Color", LogoutSpotRenderer.defaultBackgroundColor);
    public final EnumSetting renderType = new EnumSetting("Render Type", Type.Both);
    public final IntegerSetting boxLineWidth = new IntegerSetting("Box Line Width", LogoutSpotRenderer.defaultBoxLineWidth, 1, 5);
    public final IntegerSetting boxFillAlpha = new IntegerSetting("Box Fill Alpha", LogoutSpotRenderer.defaultBoxFillAlpha, 0, 255);
    public final ColorSetting boxColor = new ColorSetting("Box Color", LogoutSpotRenderer.defaultBoxColor);

    public final CopyOnWriteArrayList<LogoutSpot> playersLogged = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<LogoutSpot> playersLoggedQueue = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<String> loggedNames = new CopyOnWriteArrayList<>();

    @Override
    public void onDisable(boolean isNull) {
        this.clearLogoutSpots();
    }

    @Override
    public void onClientTick() {
        if (!this.loggedNames.isEmpty() && !this.playersLoggedQueue.isEmpty()) {
            this.playersLoggedQueue.parallelStream().forEach(logoutSpot -> {

                if (this.isLoggedName(logoutSpot.username) && !this.isLoggedPlayer(logoutSpot.username)) {
                    this.playersLogged.add(logoutSpot);
                    this.loggedNames.remove(logoutSpot.username);
                }
            });
        }

        if (this.playersLogged.isEmpty()) {
            return;
        }

        this.playersLogged.parallelStream().forEach(logoutSpot -> {

            if (!this.isPositionWithinXZRange(logoutSpot.coords.x, logoutSpot.coords.z, 256)) {
                this.playersLogged.remove(logoutSpot);
            }
        });
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        this.playersLogged.forEach(this::renderLogoutSpot);
    }

    @SuppressWarnings("unused")
    private final EventAction<ChangeWorldEvent> changeWorldEventAction = this.addEventAction(new EventAction<>(ChangeWorldEvent.class, event -> {
        this.clearLogoutSpots();
    }));

    @SuppressWarnings("unused")
    private final EventAction<PlayerJoinLeaveWorldEvent> playerJoinLeaveWorldEventAction = this.addEventAction(new EventAction<>(PlayerJoinLeaveWorldEvent.class, event -> {
        if (event.otherPlayerMP == null) {
            return;
        }

        EntityOtherPlayerMP otherPlayerMP = event.otherPlayerMP;

        String username = otherPlayerMP.username;

        if (event.type.equals(PlayerJoinLeaveWorldEvent.Type.JOIN)) {
            if (this.isLoggedPlayer(username)) {
                this.removeLoggedPlayer(username);
            }

            if (this.isQueuedPlayer(username)) {
                this.removeQueuedPlayer(username);
            }

            return;
        }

        if (event.type.equals(PlayerJoinLeaveWorldEvent.Type.KILL_OR_LEAVE)) {
            if (otherPlayerMP.isDead || otherPlayerMP.health <= 0) {
                return;
            }

            if (!this.isQueuedPlayer(username)) {
                BoxPos coords = new BoxPos((int) otherPlayerMP.posX, (int) otherPlayerMP.posY, (int) otherPlayerMP.posZ);
                LogoutSpot logoutSpot = new LogoutSpot(username, coords, System.currentTimeMillis());

                this.playersLoggedQueue.add(logoutSpot);
            }
        }
    }));

    @SuppressWarnings("unused")
    private final EventAction<ChatReceivedEvent> chatReceivedEventAction = this.addEventAction(new EventAction<>(ChatReceivedEvent.class, event -> {
        String message = event.message;

        if (message.toLowerCase().contains("joined the game")) {
            String[] args = message.split(" ");

            if (args.length < 4) {
                return;
            }

            String username = args[0];

            if (username.startsWith("§") && username.length() > 2) {
                username = username.substring(2);
            }

            if (this.isLoggedPlayer(username)) {
                this.removeLoggedPlayer(username);
            }

            if (this.isQueuedPlayer(username)) {
                this.removeQueuedPlayer(username);
            }

            this.loggedNames.remove(username);

            return;
        }

        if (message.toLowerCase().contains("left the game")) {
            String[] args = message.split(" ");

            if (args.length < 4) {
                return;
            }

            String username = args[0];

            if (username.startsWith("§") && username.length() > 2) {
                username = username.substring(2);
            }

            if (username.equalsIgnoreCase(this.getPlayer().username)) {
                return;
            }

            if (!this.isLoggedName(username)) {
                this.loggedNames.add(username);
            }
        }
    }));

    private boolean isLoggedName(String username) {
        return this.loggedNames.parallelStream().filter(name -> name.equalsIgnoreCase(username)).findFirst().orElse(null) != null;
    }

    private boolean isQueuedPlayer(String username) {
        return this.playersLoggedQueue.parallelStream().filter(logoutSpot -> logoutSpot.username.equalsIgnoreCase(username)).findFirst().orElse(null) != null;
    }

    private boolean isLoggedPlayer(String username) {
        return this.playersLogged.parallelStream().filter(logoutSpot -> logoutSpot.username.equalsIgnoreCase(username)).findFirst().orElse(null) != null;
    }

    private void removeQueuedPlayer(String username) {
        this.playersLoggedQueue.parallelStream().forEach(logoutSpot -> {
            if (logoutSpot.username.equalsIgnoreCase(username)) {
                this.playersLoggedQueue.remove(logoutSpot);
            }
        });
    }

    private void removeLoggedPlayer(String username) {
        this.playersLogged.parallelStream().forEach(logoutSpot -> {

            if (logoutSpot.username.equalsIgnoreCase(username)) {
                this.playersLogged.remove(logoutSpot);
            }
        });
    }

    private void renderLogoutSpot(LogoutSpot logoutSpot) {
        LogoutSpotRenderer.RenderType type = null;

        switch ((Type) this.renderType.getValue()) {
            case Fill:
                type = LogoutSpotRenderer.RenderType.Fill;
                break;
            case Outline:
                type = LogoutSpotRenderer.RenderType.Outline;
                break;
            case Both:
                type = LogoutSpotRenderer.RenderType.Both;
                break;
        }

        logoutSpot.getRender().render(type != null,
                this.title.getValue(),
                this.showCoords.getValue(),
                this.showDistance.getValue(),
                this.showTime.getValue(),
                this.tagBackground.getValue(),
                this.tagScale.getValue(),
                this.tagDistanceMultiplier.getValue(),
                this.tagTextColor.getValueRainbow(),
                this.tagBackgroundColor.getValueRainbow(),
                type,
                this.boxLineWidth.getValue(),
                this.boxColor.getValueRainbow(),
                this.boxFillAlpha.getValue());
    }

    public void clearLogoutSpots() {
        this.playersLogged.clear();
        this.playersLoggedQueue.clear();
        this.loggedNames.clear();
    }

    private enum Type {
        None,
        Outline,
        Fill,
        Both,
    }
}
