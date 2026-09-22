package dev.tensor.feature.hacks.hud;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.feature.hacks.HephFreecam;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.EntityOtherPlayerMP;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 06-17-2026
 */

@Hack.Info(name = "TextRadar", category = Category.HUD, description = "Displays a list of nearby players.")
public final class TextRadar extends Hack implements HUDObject {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.HUD_Text;
    }

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 300, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 1.0, 0.5, 3.0, 1);
    public final EnumSetting direction = new EnumSetting("Direction", Direction.Left);
    public final EnumSetting add = new EnumSetting("Add", Add.Down);
    public final BooleanSetting title = new BooleanSetting("Title", true, "Enable to render the HUD object title.");
    public final BooleanSetting showCoords = new BooleanSetting("Show Coords", false, "Enable to render coordinates for nearby players.");
    public final BooleanSetting showDistance = new BooleanSetting("Show Distance", true, "Enable to render the distance to nearby players.");
    public final EnumSetting playerColor = new EnumSetting("Player Color", ChatColor.GOLD);
    public final EnumSetting friendColor = new EnumSetting("Friend Color", ChatColor.AQUA);
    public final EnumSetting textColor = new EnumSetting("Text Color", ChatColor.WHITE);

    @Override
    public IntegerSetting getMinX() {
        return this.x;
    }

    @Override
    public IntegerSetting getMinY() {
        return this.y;
    }

    @Override
    public DoubleSetting getScale() {
        return this.scale;
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getWidthScaled() {
        return (int) (this.getWidth() * this.getScale().getValue());
    }

    @Override
    public int getHeight() {
        return 200;
    }

    @Override
    public int getHeightScaled() {
        return (int) (this.getHeight() * this.getScale().getValue());
    }

    private final ArrayList<String> linesToRender = new ArrayList<>();

    @Override
    public void onEnable(boolean isNull) {
        this.linesToRender.clear();
    }

    @Override
    public void onClientTick() {
        this.linesToRender.clear();

        if (this.title.getValue()) {
            this.linesToRender.add(((ChatColor) this.textColor.getValue()).text() + (this.direction.getValue().equals(Direction.Right) ? ": TextRadar" : "TextRadar :"));
        }

        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);
        boolean shouldFreecam = freecam != null && freecam.isEnabled();

        final double x = shouldFreecam ? freecam.oldX : this.getPlayer().posX;
        final double y = shouldFreecam ? freecam.oldY : this.getPlayer().posY;
        final double z = shouldFreecam ? freecam.oldZ : this.getPlayer().posZ;

        this.getFilteredLoadedEntities().forEach(entity -> {

            if (entity instanceof EntityOtherPlayerMP) {
                EntityOtherPlayerMP otherPlayer = (EntityOtherPlayerMP) entity;

                String line = otherPlayer.username;

                if (this.getFriendManager().isFriend(line)) {
                    line = ((ChatColor) this.friendColor.getValue()).text() + line;

                } else {
                    line = ((ChatColor) this.playerColor.getValue()).text() + line;
                }

                if (this.showCoords.getValue()) {
                    int pX = (int) otherPlayer.posX;
                    int pY = (int) otherPlayer.posY;
                    int pZ = (int) otherPlayer.posZ;

                    line += ((ChatColor) this.textColor.getValue()).text() + " [" + pX + ", " + pY + ", " + pZ + "]";
                }

                if (this.showDistance.getValue()) {
                    int distance = (int) this.getDistance(otherPlayer.posX - x, otherPlayer.posY - y, otherPlayer.posZ - z);

                    line += ((ChatColor) this.textColor.getValue()).text() + " [" + distance + "]";
                }

                this.linesToRender.add(line);
            }
        });
    }

    @Override
    public void render() {
        if (this.linesToRender.isEmpty()) {
            return;
        }

        this.prepareScale();

        for (int i = 0; i < this.linesToRender.size(); i++) {
            String line = this.linesToRender.get(i);
            this.drawString(line, this.getDirectionOffset(line), this.getAddOffset(this.add.getValue().equals(Add.Up) ? this.linesToRender.size() - 1 - i : i), Color.WHITE.getRGB());
        }

        this.finishScale();
    }

    @SuppressWarnings("unused")
    EventAction<ChangeWorldEvent> changeWorldEventAction = this.addHackEventAction(new EventAction<>(ChangeWorldEvent.class, event -> this.linesToRender.clear()));

    private int getDirectionOffset(String value) {
        if (this.direction.getValue().equals(Direction.Left)) {
            return 1;
        } else {

            int valueWidth = this.getFontRenderer().getStringWidth(value);

            return (this.getWidth() - valueWidth - 1);

        }
    }

    private int getAddOffset(int i) {
        if (this.add.getValue().equals(Add.Down)) {
            return (i * 10) + 1;
        } else {
            return (this.getHeight() - 10 - 1 - (i * 10));
        }
    }

    private enum Direction {
        Left,
        Right
    }

    private enum Add {
        Up,
        Down
    }
}
