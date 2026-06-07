package dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects;

import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.settings.buttons.WaypointListSettingButton;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.ButtonSettingObject;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.helpers.waypoint.Waypoint;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-09-2026
 */

public final class WaypointSettingObject implements ButtonSettingObject {

    public final Waypoint waypoint;
    private final WaypointListSettingButton button;
    private final IntegerSetting x;
    private final IntegerSetting y;
    private final IntegerSetting scrollY;
    private final int offsetX;
    private final int offsetY;

    public int number = 0;

    private boolean hovered = false;
    private boolean hoveredEnabled = false;
    private boolean hoveredDelete = false;

    public WaypointSettingObject(Waypoint waypoint, WaypointListSettingButton button, IntegerSetting x, IntegerSetting y, IntegerSetting scrollY, int offsetX, int offsetY, int number) {
        this.waypoint = waypoint;
        this.button = button;
        this.x = x;
        this.y = y;
        this.scrollY = scrollY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.number = number;
    }

    @Override
    public int getWidth() {
        return this.button.tensorGUI.guiWidth - (2 * this.offsetX);
    }

    @Override
    public int getHeight() {
        return 55;
    }

    @Override
    public int getPosX() {
        return this.x.getValue() + this.offsetX;
    }

    @Override
    public int getPosY() {
        return this.y.getValue() + this.scrollY.getValue() + this.offsetY + (number * this.getHeight());
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {
        this.startFlatGL(false);
        this.prepRectangle(this.hovered ? selectedFillColor.getRGB() : fillColor.getRGB());

        this.genRectangleVertices(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 102, this.getPosY() + this.getHeight() - 2);

        this.swapRectangleColor(outLineColor.getRGB());
        this.genRectangleVertices(this.getPosX(), this.getPosY() + this.getHeight() - 2, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight());
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 102, this.getPosY(), this.getPosX() + this.getWidth() - 100, this.getPosY() + this.getHeight() - 2);
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 100, this.getPosY() + 25, this.getPosX() + this.getWidth(), this.getPosY() + 27);

        this.swapRectangleColor(this.waypoint.isEnabled() ? (this.hoveredEnabled ? new Color(0, 255, 0, fillColor.getAlpha()).darker().getRGB() : new Color(0, 255, 0, fillColor.getAlpha()).getRGB()) : (this.hoveredEnabled ? new Color(255, 0, 0, fillColor.getAlpha()).darker().getRGB() : new Color(255, 0, 0, fillColor.getAlpha()).getRGB()));
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + 25);

        this.swapRectangleColor(this.hoveredDelete ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.genRectangleVertices(this.getPosX() + this.getWidth() - 100, this.getPosY() + 27, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() - 2);

        this.finishRectangle();
        this.finishFlatGL(false);

        this.drawString("Name : " + ChatColor.LIGHT_GRAY.text() + this.waypoint.getName(), this.getPosX() + 3, this.getPosY() + 3, Color.WHITE.getRGB());
        this.drawString("Coordinates : X = " + ChatColor.YELLOW.text() + this.waypoint.getX() + ChatColor.DEFAULT.text() + ", Y = " + ChatColor.YELLOW.text() + this.waypoint.getY() + ChatColor.DEFAULT.text() + ", Z = " + ChatColor.YELLOW.text() + this.waypoint.getZ(), this.getPosX() + 3, this.getPosY() + 15, Color.WHITE.getRGB());
        this.drawString("Dimension : " + (!this.waypoint.isOverworld() ? ChatColor.RED.text() + "Nether" : ChatColor.GREEN.text() + "Overworld"), this.getPosX() + 3, this.getPosY() + 27, Color.WHITE.getRGB());
        this.drawString("Server : " + this.waypoint.getServer(), this.getPosX() + 3, this.getPosY() + 39, Color.WHITE.getRGB());

        this.drawCenteredString(this.waypoint.isEnabled() ? ">> Enabled <<" : ">> Disabled <<", this.getPosX() + this.getWidth() - (100 / 2), this.getPosY() + 8, Color.YELLOW.getRGB());
        this.drawCenteredString(">> Delete <<", this.getPosX() + this.getWidth() - (100 / 2), this.getPosY() + 35, Color.YELLOW.getRGB());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.hoveredEnabled) {
            this.waypoint.toggle();
            this.setHovered(false);
        } else if (this.hoveredDelete) {
            this.button.queueToRemoveWaypoint(this.waypoint);
        }
    }

    @Override
    public boolean hovered() {
        return this.hovered || this.hoveredEnabled || this.hoveredDelete;
    }

    @Override
    public void setHovered(boolean hovered) {
        if (!hovered) {
            this.hovered = false;
            this.hoveredEnabled = false;
            this.hoveredDelete = false;
        } else {
            this.hovered = true;
        }
    }

    @Override
    public void onHovered(int mouseX, int mouseY) {

        if (this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + 25, mouseX, mouseY)) {
            this.setHovered(false);
            this.hoveredEnabled = true;
            this.button.resetHover(this);
        } else if (this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY() + 27, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), mouseX, mouseY)) {
            this.setHovered(false);
            this.hoveredDelete = true;
            this.button.resetHover(this);
        } else if (this.isHovered(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 100, this.getPosY() + this.getHeight(), mouseX, mouseY)) {
            this.setHovered(false);
            this.hovered = true;
            this.button.resetHover(this);
        }
    }
}
