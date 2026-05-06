package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.variable.ChatReceivedEvent;
import dev.tensor.backend.events.variable.PlayerJoinLeaveWorldEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.helpers.block.BlockPos;
import dev.tensor.misc.helpers.logoutspot.LogoutSpot;
import dev.tensor.misc.helpers.logoutspot.LogoutSpotRenderer;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.*;
import net.minecraft.src.EntityOtherPlayerMP;

import java.util.ArrayList;

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

    public final BooleanSetting title = new BooleanSetting("Title", true);
    public final BooleanSetting showCoords = new BooleanSetting("Show Coords", true);
    public final BooleanSetting showDistance = new BooleanSetting("Show Distance", false);
    public final BooleanSetting showTime = new BooleanSetting("Time", false);
    public final DoubleSetting tagScale = new DoubleSetting("Tag Scale", 1.00, 0.10, 2.00, 2);
    public final IntegerSetting tagDistanceMultiplier = new IntegerSetting("Tag Distance Multiplier", 8, 2, 10);
    public final BooleanSetting tagBackground = new BooleanSetting("Tag Background", false);
    public final ColorSettingNoAlpha tagTextColor = new ColorSettingNoAlpha("Text Color", LogoutSpotRenderer.defaultTagTextColor);
    public final ColorSetting tagBackgroundColor = new ColorSetting("Background Color", LogoutSpotRenderer.defaultBackgroundColor);
    public final EnumSetting renderType = new EnumSetting("Render Type", Type.Both);
    public final IntegerSetting boxLineWidth = new IntegerSetting("Box Line Width", LogoutSpotRenderer.defaultBoxLineWidth, 1, 5);
    public final IntegerSetting boxFillAlpha = new IntegerSetting("Box Fill Alpha", LogoutSpotRenderer.defaultBoxFillAlpha, 0, 255);
    public final ColorSetting boxColor = new ColorSetting("Box Color", LogoutSpotRenderer.defaultBoxColor);

    public final ArrayList<LogoutSpot> playersLogged = new ArrayList<>();
    private final ArrayList<LogoutSpot> playersLoggedQueue = new ArrayList<>();
    private final ArrayList<String> loggedNames = new ArrayList<>();

    @Override
    public void onDisable(boolean isNull) {
        this.playersLogged.clear();
        this.playersLoggedQueue.clear();
        this.loggedNames.clear();
    }

    @Override
    public void onClientTick() {
        if (!this.loggedNames.isEmpty() && !this.playersLoggedQueue.isEmpty()) {
            ArrayList<String> toRemove = new ArrayList<>();

            this.playersLoggedQueue.forEach(logoutSpot -> {

                if (this.isLoggedName(logoutSpot.username) && !this.isLoggedPlayer(logoutSpot.username)) {
                    this.playersLogged.add(logoutSpot);
                    toRemove.add(logoutSpot.username);
                }
            });

            this.loggedNames.removeAll(toRemove);
        }

        if (this.playersLogged.isEmpty()) {
            return;
        }

        ArrayList<LogoutSpot> toRemove = new ArrayList<>();

        this.playersLogged.forEach(logoutSpot -> {

            if (!this.isWithinRenderDistance(logoutSpot)) {
                toRemove.add(logoutSpot);
            }
        });

        this.playersLogged.removeAll(toRemove);
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        if (this.playersLogged.isEmpty()) {
            return;
        }

        this.playersLogged.forEach(this::renderLogoutSpot);
    }

    @SuppressWarnings("unused")
    EventAction<ChangeWorldEvent> changeWorldEventAction = this.addEventAction(new EventAction<>(ChangeWorldEvent.class, event -> {
        this.playersLogged.clear();
        this.playersLoggedQueue.clear();
        this.loggedNames.clear();
    }));

    @SuppressWarnings("unused")
    EventAction<PlayerJoinLeaveWorldEvent> playerJoinLeaveWorldEventAction = this.addEventAction(new EventAction<>(PlayerJoinLeaveWorldEvent.class, event -> {
        if (event.otherPlayerMP == null) {
            return;
        }

        EntityOtherPlayerMP otherPlayerMP = event.otherPlayerMP;

        String username = otherPlayerMP.username;

        if (event.type.equals(PlayerJoinLeaveWorldEvent.Type.Join)) {
            if (this.isLoggedPlayer(username)) {
                this.removeLoggedPlayer(username);
            }

            if (this.isQueuedPlayer(username)) {
                this.removeQueuedPlayer(username);
            }

            return;
        }

        if (event.type.equals(PlayerJoinLeaveWorldEvent.Type.Kill_or_Leave)) {
            if (otherPlayerMP.isDead || otherPlayerMP.health <= 0) {
                return;
            }

            if (!this.isQueuedPlayer(username)) {
                BlockPos coords = new BlockPos((int) otherPlayerMP.posX, (int) otherPlayerMP.posY, (int) otherPlayerMP.posZ);
                LogoutSpot logoutSpot = new LogoutSpot(username, coords, System.currentTimeMillis());

                this.playersLoggedQueue.add(logoutSpot);
            }
        }
    }));

    @SuppressWarnings("unused")
    EventAction<ChatReceivedEvent> chatReceivedEventAction = this.addEventAction(new EventAction<>(ChatReceivedEvent.class, event -> {
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
        return this.loggedNames.stream().filter(name -> name.equalsIgnoreCase(username)).findFirst().orElse(null) != null;
    }

    private boolean isQueuedPlayer(String username) {
        return this.playersLoggedQueue.stream().filter(logoutSpot -> logoutSpot.username.equalsIgnoreCase(username)).findFirst().orElse(null) != null;
    }

    private boolean isLoggedPlayer(String username) {
        return this.playersLogged.stream().filter(logoutSpot -> logoutSpot.username.equalsIgnoreCase(username)).findFirst().orElse(null) != null;
    }

    private void removeQueuedPlayer(String username) {
        ArrayList<LogoutSpot> toRemove = new ArrayList<>();

        this.playersLoggedQueue.forEach(logoutSpot -> {

            if (logoutSpot.username.equalsIgnoreCase(username)) {
                toRemove.add(logoutSpot);
            }
        });

        this.playersLoggedQueue.removeAll(toRemove);
    }

    private void removeLoggedPlayer(String username) {
        ArrayList<LogoutSpot> toRemove = new ArrayList<>();

        this.playersLogged.forEach(logoutSpot -> {

            if (logoutSpot.username.equalsIgnoreCase(username)) {
                toRemove.add(logoutSpot);
            }
        });

        this.playersLogged.removeAll(toRemove);
    }

    private boolean isWithinRenderDistance(LogoutSpot logoutSpot) {
        return this.getDistanceSqWithFreecam(logoutSpot.coords.x, logoutSpot.coords.y, logoutSpot.coords.z) < 65536;
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
                this.tagTextColor.getValue(),
                this.tagBackgroundColor.getValue(),
                type,
                this.boxLineWidth.getValue(),
                this.boxColor.getValue(),
                this.boxFillAlpha.getValue());
    }

    private enum Type {
        None,
        Outline,
        Fill,
        Both,
    }
}
