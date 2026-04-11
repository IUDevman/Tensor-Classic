package dev.tensor.misc.gui.imp.elements.settings.buttons;

import dev.tensor.feature.hacks.ClickGUI;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.gui.imp.elements.ButtonSetting;
import dev.tensor.misc.gui.imp.elements.HackElement;
import dev.tensor.misc.gui.imp.elements.settings.buttons.object.objects.WaypointSettingObject;
import dev.tensor.misc.helpers.waypoint.Waypoint;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import dev.tensor.misc.imp.setting.settings.WaypointListSetting;
import net.minecraft.src.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hoosiers
 * @since 04-09-2026
 */

public final class WaypointListSettingButton implements ButtonSetting {

    public final TensorGUI tensorGUI;
    private final ClickGUI clickGUI;
    private final HackElement hackElement;
    private final WaypointListSetting waypointListSetting;

    private boolean searchingName = false;
    private boolean searchingX = false;
    private boolean searchingY = false;
    private boolean searchingZ = false;

    private String nameValue = "";
    private String xValue = "";
    private String yValue = "";
    private String zValue = "";
    private boolean enabledValue = true;

    private String prevName = "prev";
    private String prevX = "prev";
    private String prevY = "prev";
    private String prevZ = "prev";

    private final int maxCoordLength = 9;
    private final int maxNameLength = 16;

    private boolean hoveredOnAdd = false;
    private boolean hoveredOnBack = false;
    private boolean hoveredOnAddWaypoint = false;
    private boolean active;

    private Waypoint waypointToDelete = null;

    private boolean alreadyHasWaypointName = false;
    private String badWaypointName = "";

    private final ArrayList<WaypointSettingObject> waypointSettingObjects = new ArrayList<>();

    private final IntegerSetting scrollY = new IntegerSetting("WaypointList Scroll Y", 0, -1000, 1000);

    public WaypointListSettingButton(TensorGUI tensorGUI, ClickGUI clickGUI, HackElement hackElement, WaypointListSetting waypointListSetting) {
        this.tensorGUI = tensorGUI;
        this.clickGUI = clickGUI;
        this.hackElement = hackElement;
        this.waypointListSetting = waypointListSetting;

        AtomicInteger offsetY = new AtomicInteger(0);

        this.waypointListSetting.getValue().getWaypointsAll().forEach(waypoint -> {

            WaypointSettingObject object = new WaypointSettingObject(waypoint, this, this.clickGUI.x, this.clickGUI.y, this.scrollY, 0, this.tensorGUI.titleHeight + 57, offsetY.getAndAdd(1));
            this.waypointSettingObjects.add(object);
        });
    }

    @Override
    public int getWidth() {
        return this.tensorGUI.guiWidth;
    }

    @Override
    public int getHeight() {
        return this.tensorGUI.guiHeight.get();
    }

    @Override
    public int getPosX() {
        return this.clickGUI.x.getValue();
    }

    @Override
    public int getPosY() {
        return this.clickGUI.y.getValue();
    }

    @Override
    public void render(TensorGUI tensorGUI, Color fillColor, Color selectedFillColor, Color outLineColor) {
        this.drawRectangle(this.getPosX(), this.getPosY(), this.getPosX() + this.getWidth() - 102, this.getPosY() + this.tensorGUI.titleHeight, fillColor.getRGB());
        this.drawRectangle(this.getPosX() - 2, this.getPosY() - 2, this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth(), this.getPosY() - 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.tensorGUI.titleHeight + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() - 2, this.getPosX() + this.getWidth(), this.getPosY(), outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight + 2, outLineColor.getRGB());
        this.drawCenteredString(this.hackElement.hack.NAME + " : " + this.waypointListSetting.getName(), this.getPosX() + ((this.getWidth() - 102) / 2), this.getPosY() + 2 + ((this.tensorGUI.titleHeight - 12) / 2), Color.WHITE.getRGB());

        this.drawRectangle(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight, this.hoveredOnBack ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth() - 102, this.getPosY(), this.getPosX() + this.getWidth() - 100, this.getPosY() + this.tensorGUI.titleHeight, outLineColor.getRGB());
        this.drawCenteredString(">> Go Back <<", this.getPosX() + this.getWidth() - (100 / 2), this.getPosY() + 2 + ((this.tensorGUI.titleHeight - 12) / 2), Color.YELLOW.getRGB());

        this.drawRectangle(this.getPosX() - 2, this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX(), this.getPosY() + this.getHeight() + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + this.getWidth() + 2, this.getPosY() + this.getHeight() + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + this.getWidth(), this.tensorGUI.titleHeight + 2, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.getHeight(), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight() + 2, outLineColor.getRGB());

        this.drawRectangle(this.getPosX() + 57, this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight + 55, this.hoveredOnAddWaypoint ? selectedFillColor.getRGB() : fillColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 55, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight + 57, outLineColor.getRGB());
        this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + 55, this.getPosY() + this.tensorGUI.titleHeight + 55, this.alreadyHasWaypointName ? this.hoveredOnAdd ? new Color(255, 0, 0, fillColor.getAlpha()).darker().getRGB() : new Color(255, 0, 0, fillColor.getAlpha()).getRGB() : this.hoveredOnAdd ? new Color(0, 255, 0, fillColor.getAlpha()).darker().getRGB() : new Color(0, 255, 0, fillColor.getAlpha()).getRGB());
        this.drawRectangle(this.getPosX() + 55, this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + 57, this.getPosY() + this.tensorGUI.titleHeight + 55, outLineColor.getRGB());
        this.drawCenteredString(">> Add <<", this.getPosX() + 50 - (this.getFontRenderer().getStringWidth(">> Add <<") / 2), this.getPosY() + this.tensorGUI.titleHeight + 24, Color.YELLOW.getRGB());
        this.drawString("Waypoint Name : " + (this.alreadyHasWaypointName ? this.getColor(ChatColor.RED) : this.getColor(ChatColor.LIGHT_GRAY)) + (this.searchingName ? this.nameValue.isEmpty() ? "..." : this.nameValue : this.nameValue.isEmpty() ? "New Waypoint" : this.nameValue), this.getPosX() + 60, this.getPosY() + this.tensorGUI.titleHeight + 5, Color.YELLOW.getRGB());
        this.drawString("Dimension : " + (this.getWorld().worldProvider.isHellWorld ? this.getColor(ChatColor.RED) + "Nether" : this.getColor(ChatColor.GREEN) + "Overworld"), this.getPosX() + 60, this.getPosY() + this.tensorGUI.titleHeight + 17, Color.WHITE.getRGB());
        this.drawString("Server : " + (this.isMultiplayer() ? this.getServerIP() : "Singleplayer"), this.getPosX() + 60, this.getPosY() + this.tensorGUI.titleHeight + 29, Color.WHITE.getRGB());
        this.drawString("Enabled : " + (this.enabledValue ? this.getColor(ChatColor.GREEN) + "True" : this.getColor(ChatColor.RED) + "False"), this.getPosX() + 60, this.getPosY() + this.tensorGUI.titleHeight + 41, Color.WHITE.getRGB());

        this.drawString("X : " + this.getColor(ChatColor.LIGHT_GRAY) + "[" + (this.searchingX ? this.xValue.equalsIgnoreCase("") ? "..." : this.xValue : this.xValue.equalsIgnoreCase("") ? "Click" : this.xValue) + "]", this.getPosX() + this.getWidth() - 97, this.getPosY() + this.tensorGUI.titleHeight + 5, Color.YELLOW.getRGB());
        this.drawString("Y : " + this.getColor(ChatColor.LIGHT_GRAY) + "[" + (this.searchingY ? this.yValue.equalsIgnoreCase("") ? "..." : this.yValue : this.yValue.equalsIgnoreCase("") ? "Click" : this.yValue) + "]", this.getPosX() + this.getWidth() - 97, this.getPosY() + this.tensorGUI.titleHeight + 17, Color.YELLOW.getRGB());
        this.drawString("Z : " + this.getColor(ChatColor.LIGHT_GRAY) + "[" + (this.searchingZ ? this.zValue.equalsIgnoreCase("") ? "..." : this.zValue : this.zValue.equalsIgnoreCase("") ? "Click" : this.zValue) + "]", this.getPosX() + this.getWidth() - 97, this.getPosY() + this.tensorGUI.titleHeight + 29, Color.YELLOW.getRGB());
        this.drawString("Current Position", this.getPosX() + this.getWidth() - 97, this.getPosY() + this.tensorGUI.titleHeight + 41, Color.YELLOW.getRGB());

        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        int posX = this.getPosX();
        int posY = this.getPosY() + this.tensorGUI.titleHeight + 57;
        int width1 = this.getWidth();
        int height1 = this.getHeight() - this.tensorGUI.titleHeight - 57;

        int windowHeight = this.getMinecraft().displayHeight;
        double scaleX = (double) this.getMinecraft().displayWidth / (double) this.getMinecraft().currentScreen.width;
        double scaleY = (double) this.getMinecraft().displayHeight / (double) this.getMinecraft().currentScreen.height;
        int widthScaled = (int) (scaleX * width1);
        int heightScaled = (int) (scaleY * height1);
        int xScaled = (int) (posX * scaleX);
        int yScaled = (int) (posY * scaleY);

        GL11.glScissor(xScaled, windowHeight - yScaled - heightScaled, widthScaled, heightScaled);

        this.waypointSettingObjects.forEach(waypointSettingObject -> waypointSettingObject.render(tensorGUI, fillColor, selectedFillColor, outLineColor));

        if (this.waypointSettingObjects.size() * 55 < this.getHeight() - -this.tensorGUI.titleHeight - 57) {
            this.drawRectangle(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 57 + (this.waypointSettingObjects.size() * 55), this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), fillColor.getRGB());
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight, mouseX, mouseY)) {
            this.close();
        }

        if (this.isHovered(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight + 55, mouseX, mouseY)) {
            if (this.isHovered(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + 58, this.getPosY() + this.tensorGUI.titleHeight + 55, mouseX, mouseY)) {
                this.addNewWaypoint();
            }

            if (this.isHovered(this.getPosX() + 60, this.getPosY() + this.tensorGUI.titleHeight + 41, this.getPosX() + 60 + this.getFontRenderer().getStringWidth("Enabled : " + (this.enabledValue ? "True" : "False")), this.getPosY() + this.tensorGUI.titleHeight + 51, mouseX, mouseY)) {
                this.enabledValue = !this.enabledValue;
            }

            if (this.isHovered(this.getPosX() + 60, this.getPosY() + this.tensorGUI.titleHeight + 4, this.getPosX() + 60 + this.getFontRenderer().getStringWidth("Waypoint Name : " + (this.searchingName ? this.nameValue.isEmpty() ? "..." : this.nameValue : this.nameValue.isEmpty() ? "New Waypoint" : this.nameValue)), this.getPosY() + this.tensorGUI.titleHeight + 12, mouseX, mouseY)) {
                this.searchingName = !this.searchingName;
                this.searchingX = false;
                this.searchingY = false;
                this.searchingZ = false;
                this.setBackSearchValues();

                if (!this.searchingName && !this.nameValue.equalsIgnoreCase(this.badWaypointName)) {
                    this.alreadyHasWaypointName = false;
                    this.badWaypointName = "";
                }

            } else if (this.isHovered(this.getPosX() + this.getWidth() - 97, this.getPosY() + this.tensorGUI.titleHeight + 4, this.getPosX() + this.getWidth() - 97 + this.getFontRenderer().getStringWidth("X : " + "[" + (this.searchingX ? this.xValue.equalsIgnoreCase("") ? "..." : this.xValue : this.xValue.equalsIgnoreCase("") ? "Click" : this.xValue) + "]"), this.getPosY() + this.tensorGUI.titleHeight + 12, mouseX, mouseY)) {
                this.searchingName = false;
                this.searchingX = !this.searchingX;
                this.searchingY = false;
                this.searchingZ = false;
                this.setBackSearchValues();

            } else if (this.isHovered(this.getPosX() + this.getWidth() - 97, this.getPosY() + this.tensorGUI.titleHeight + 16, this.getPosX() + this.getWidth() - 97 + this.getFontRenderer().getStringWidth("Y : " + "[" + (this.searchingY ? this.yValue.equalsIgnoreCase("") ? "..." : this.yValue : this.yValue.equalsIgnoreCase("") ? "Click" : this.yValue) + "]"), this.getPosY() + this.tensorGUI.titleHeight + 24, mouseX, mouseY)) {
                this.searchingName = false;
                this.searchingX = false;
                this.searchingY = !this.searchingY;
                this.searchingZ = false;
                this.setBackSearchValues();

            } else if (this.isHovered(this.getPosX() + this.getWidth() - 97, this.getPosY() + this.tensorGUI.titleHeight + 28, this.getPosX() + this.getWidth() - 97 + this.getFontRenderer().getStringWidth("Z : " + "[" + (this.searchingZ ? this.zValue.equalsIgnoreCase("") ? "..." : this.zValue : this.zValue.equalsIgnoreCase("") ? "Click" : this.zValue) + "]"), this.getPosY() + this.tensorGUI.titleHeight + 36, mouseX, mouseY)) {
                this.searchingName = false;
                this.searchingX = false;
                this.searchingY = false;
                this.searchingZ = !this.searchingZ;
                this.setBackSearchValues();

            } else if (this.isHovered(this.getPosX() + this.getWidth() - 97, this.getPosY() + this.tensorGUI.titleHeight + 40, this.getPosX() + this.getWidth() - 97 + this.getFontRenderer().getStringWidth("Current Position"), this.getPosY() + this.tensorGUI.titleHeight + 48, mouseX, mouseY)) {
                this.xValue = String.valueOf((int) this.getPlayer().posX);
                this.yValue = String.valueOf((int) this.getPlayer().posY);
                this.zValue = String.valueOf((int) this.getPlayer().posZ);
                this.clearSearching();

            } else {
                this.clearSearching();
                this.setBackSearchValues();
            }

        } else if (this.isHovered(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 57, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), mouseX, mouseY)) {

            AtomicBoolean hasDispatched = new AtomicBoolean(false);

            this.waypointSettingObjects.forEach(waypointSettingObject -> {

                if (!hasDispatched.get() && this.isHovered(waypointSettingObject.getPosX(), waypointSettingObject.getPosY(), waypointSettingObject.getPosX() + waypointSettingObject.getWidth(), waypointSettingObject.getPosY() + waypointSettingObject.getHeight(), mouseX, mouseY)) {
                    hasDispatched.set(true);
                    waypointSettingObject.onClick(mouseX, mouseY);
                }
            });

            if (this.waypointToDelete != null) {
                this.removeWaypoint(this.waypointToDelete);
            }

            this.clearSearching();

        } else {
            this.clearSearching();
        }
    }

    @Override
    public void onHovered(int mouseX, int mouseY) {
        this.hoveredOnBack = this.isHovered(this.getPosX() + this.getWidth() - 100, this.getPosY(), this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight, mouseX, mouseY);

        this.hoveredOnAdd = this.isHovered(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + 55, this.getPosY() + this.tensorGUI.titleHeight + 55, mouseX, mouseY);

        this.hoveredOnAddWaypoint = this.isHovered(this.getPosX() + 57, this.getPosY() + this.tensorGUI.titleHeight + 2, this.getPosX() + this.getWidth(), this.getPosY() + this.tensorGUI.titleHeight + 55, mouseX, mouseY);

        if (!this.hoveredOnBack && !this.hoveredOnAdd && !this.hoveredOnAddWaypoint && this.isHovered(this.getPosX(), this.getPosY() + this.tensorGUI.titleHeight + 57, this.getPosX() + this.getWidth(), this.getPosY() + this.getHeight(), mouseX, mouseY)) {
            this.waypointSettingObjects.forEach(object -> object.onHovered(mouseX, mouseY));
        } else {
            this.waypointSettingObjects.forEach(object -> object.setHovered(false));
        }
    }

    @Override
    public void handleScroll(int scroll) {
        if (scroll == 0) {
            return;
        }

        int scrollAdjust = scroll;

        if (scrollY.getValue() > -10 && scroll > 0) {

            scrollAdjust = -scrollY.getValue();
        }

        int maxHeight = this.getHeight() - this.tensorGUI.titleHeight - 57;

        if ((this.waypointSettingObjects.size() * 55) + (scroll < 0 ? this.scrollY.getValue() - 10 : 0) <= maxHeight) {
            int test = (((this.waypointSettingObjects.size() * 55) + (scroll < 0 ? this.scrollY.getValue() : 0)) - maxHeight);

            if (test >= scrollAdjust) {
                scrollAdjust = -test;
            } else {
                return;
            }
        }

        this.scrollY.setValue(this.scrollY.getValue() + scrollAdjust);
    }

    @Override
    public boolean isSearching() {
        if (this.searchingName) {
            return true;
        } else if (this.searchingX) {
            return true;
        } else if (this.searchingY) {
            return true;
        } else {
            return this.searchingZ;
        }
    }

    @Override
    public void setSearching(boolean searching) {
        if (!searching) {
            this.clearSearching();
        }
    }

    public void clearSearching() {

        this.searchingName = false;
        this.searchingX = false;
        this.searchingY = false;
        this.searchingZ = false;
    }

    private void setBackSearchValues() {
        if (this.searchingName && !this.nameValue.equalsIgnoreCase("")) {
            this.prevName = this.nameValue;
            this.nameValue = "";
        }

        if (!this.searchingName && !this.prevName.equalsIgnoreCase("prev")) {
            this.nameValue = this.prevName;
            this.prevName = "prev";
        }

        if (this.searchingX && !this.xValue.equalsIgnoreCase("")) {
            this.prevX = this.xValue;
            this.xValue = "";
        }

        if (!this.searchingX && !this.prevX.equalsIgnoreCase("prev")) {
            this.xValue = this.prevX;
            this.prevX = "prev";
        }

        if (this.searchingY && !this.yValue.equalsIgnoreCase("")) {
            this.prevY = this.yValue;
            this.yValue = "";
        }

        if (!this.searchingY && !this.prevY.equalsIgnoreCase("prev")) {
            this.yValue = this.prevY;
            this.prevY = "prev";
        }

        if (this.searchingZ && !this.zValue.equalsIgnoreCase("")) {
            this.prevZ = this.zValue;
            this.zValue = "";
        }

        if (!this.searchingZ && !this.prevZ.equalsIgnoreCase("prev")) {
            this.zValue = this.prevZ;
            this.prevZ = "prev";
        }
    }

    private void resetSearchValues() {
        this.nameValue = "";
        this.enabledValue = true;
        this.xValue = "";
        this.yValue = "";
        this.zValue = "";
        this.prevName = "prev";
        this.prevX = "prev";
        this.prevY = "prev";
        this.prevZ = "prev";
    }

    @Override
    public void onKeyPressed(char letter, int key) {
        if (key == Keyboard.KEY_DELETE) {

            if (this.searchingName) {
                this.nameValue = "";
            } else if (this.searchingX) {
                this.xValue = "";
            } else if (this.searchingY) {
                this.yValue = "";
            } else if (this.searchingZ) {
                this.zValue = "";
            }

            this.setSearching(false);
            return;
        }

        if (key == Keyboard.KEY_RETURN) {
            this.setSearching(false);
            return;
        }

        if (key == Keyboard.KEY_BACK) {

            if (this.searchingName && !this.nameValue.isEmpty()) {
                this.nameValue = this.nameValue.substring(0, this.nameValue.length() - 1);
            } else if (this.searchingX && !this.xValue.isEmpty()) {
                this.xValue = this.xValue.substring(0, this.xValue.length() - 1);
            } else if (this.searchingY && !this.yValue.isEmpty()) {
                this.yValue = this.yValue.substring(0, this.yValue.length() - 1);
            } else if (this.searchingZ && !this.zValue.isEmpty()) {
                this.zValue = this.zValue.substring(0, this.zValue.length() - 1);
            }

            return;
        }

        if (this.searchingName) {

            if (Arrays.stream(this.badKeys_Name()).noneMatch(value -> value == key) && ChatAllowedCharacters.allowedCharacters.contains(String.valueOf(letter))) {
                String newValue = this.nameValue + letter;

                if (newValue.length() <= this.maxNameLength) {
                    this.nameValue = this.nameValue + letter;
                }
            }

            return;
        }

        if (Arrays.stream(this.acceptedKeys_Coords()).noneMatch(value -> value == key)) {
            return;
        }

        String name = Keyboard.getKeyName(key);

        if (name.equalsIgnoreCase("MINUS")) {
            name = "-";
        }

        String prevX = this.xValue;
        String prevY = this.yValue;
        String prevZ = this.zValue;

        try {
            if (this.searchingX && this.xValue.length() <= this.maxCoordLength) {
                if ((this.xValue + name).equalsIgnoreCase("-")) {
                    this.xValue = "-";
                    return;
                }

                int valueX = this.clampCoord(Integer.parseInt(this.xValue + name), false);
                this.xValue = String.valueOf(valueX);

            } else if (this.searchingY && this.yValue.length() <= this.maxCoordLength) {
                if ((this.yValue + name).equalsIgnoreCase("-")) {
                    this.yValue = "-";
                    return;
                }

                int valueY = this.clampCoord(Integer.parseInt(this.yValue + name), true);
                this.yValue = String.valueOf(valueY);

            } else if (this.searchingZ && this.zValue.length() <= this.maxCoordLength) {
                if ((this.zValue + name).equalsIgnoreCase("-")) {
                    this.zValue = "-";
                    return;
                }

                int valueZ = this.clampCoord(Integer.parseInt(this.zValue + name), false);
                this.zValue = String.valueOf(valueZ);

            }
        } catch (Exception ignored) {
            this.xValue = prevX;
            this.yValue = prevY;
            this.zValue = prevZ;
        }
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void close() {
        this.setActive(false);
        this.setSearching(false);
        this.resetSearchValues();
        this.hoveredOnBack = false;
        this.hoveredOnAddWaypoint = false;
        this.tensorGUI.closeButton();
    }

    public void resetHover(WaypointSettingObject waypointObject) {

        this.hoveredOnBack = false;

        this.waypointSettingObjects.forEach(waypointSettingObject -> {

            if (waypointSettingObject.hovered() && waypointSettingObject != waypointObject) {
                waypointSettingObject.setHovered(false);
            }
        });
    }

    private int[] acceptedKeys_Coords() {
        return new int[]{
                Keyboard.KEY_MINUS,
                Keyboard.KEY_0,
                Keyboard.KEY_1,
                Keyboard.KEY_2,
                Keyboard.KEY_3,
                Keyboard.KEY_4,
                Keyboard.KEY_5,
                Keyboard.KEY_6,
                Keyboard.KEY_7,
                Keyboard.KEY_8,
                Keyboard.KEY_9
        };
    }

    private int[] badKeys_Name() {
        return new int[]{
                0x00, 0x01, 0x0E, 0x0F, 0x1C, 0x1D, 0x2A, 0x36,
                0x37, 0x38, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F, 0x40,
                0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x53, 0x57, 0x58,
                0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x70, 0x71, 0x79,
                0x7B, 0x7D, 0x8D, 0x90, 0x91, 0x93, 0x94, 0x95, 0x96,
                0x97, 0x9C, 0x9D, 0xA7, 0xB7, 0xB8, 0xC4, 0xC5, 0xC7,
                0xC9, 0xCF, 0xD1, 0xD2, 0xD3, 0xDA, 0xDB, 0xDC, 0xDD,
                0xDE, 0xDF
        };
    }

    private int clampCoord(int value, boolean yValue) {
        if (yValue) {
            if (value > 128) {
                return 128;
            } else if (value < 0) {
                return 0;
            } else return value;
        }

        if (value > 30000000) {
            return 30000000;
        } else if (value < -30000000) {
            return -30000000;
        } else return value;
    }

    private void addNewWaypoint() {

        if (this.xValue.equalsIgnoreCase("") || this.yValue.equalsIgnoreCase("") || this.zValue.equalsIgnoreCase("")) {
            return;
        }

        if (this.nameValue.equalsIgnoreCase("")) {

            this.nameValue = "New Waypoint";
        }

        Waypoint waypointFound = this.waypointListSetting.getValue().getWaypointsFor(!this.getWorld().worldProvider.isHellWorld, this.isMultiplayer() ? this.getServerIP() : "Singleplayer").stream().filter(waypoint1 -> waypoint1.getName().equalsIgnoreCase(this.nameValue)).findFirst().orElse(null);

        if (waypointFound != null) {
            this.alreadyHasWaypointName = true;
            this.badWaypointName = waypointFound.getName();
            return;
        }

        int x = Integer.parseInt(this.xValue);
        int y = Integer.parseInt(this.yValue);
        int z = Integer.parseInt(this.zValue);

        Waypoint waypoint = new Waypoint(this.nameValue, x, y, z, !this.getWorld().worldProvider.isHellWorld, this.isMultiplayer() ? this.getServerIP() : "Singleplayer");
        waypoint.setEnabled(enabledValue);

        int size = this.waypointSettingObjects.size();

        this.waypointListSetting.getValue().addToWaypointList(waypoint);
        this.waypointSettingObjects.add(new WaypointSettingObject(waypoint, this, this.clickGUI.x, this.clickGUI.y, this.scrollY, 0, this.tensorGUI.titleHeight + 57, size));

        this.clearSearching();
        this.resetSearchValues();
    }

    private void removeWaypoint(Waypoint waypoint) {

        WaypointSettingObject object = this.waypointSettingObjects.stream().filter(waypointSettingObject -> waypointSettingObject.waypoint.equals(waypoint)).findFirst().orElse(null);

        if (object != null) {
            if (this.alreadyHasWaypointName && object.waypoint.getName().equalsIgnoreCase(this.badWaypointName)) {
                this.alreadyHasWaypointName = false;
                this.badWaypointName = "";
            }

            this.waypointSettingObjects.remove(object);
            this.waypointListSetting.getValue().removeFromWaypointsList(waypoint);

            for (int i = 0; i < this.waypointSettingObjects.size(); i++) {

                this.waypointSettingObjects.get(i).number = i;
            }

            if (this.scrollY.getValue() < 0) {

                int value = this.scrollY.getValue() + 55;

                if (value < 0) {
                    this.scrollY.setValue(value);
                } else {
                    this.scrollY.setValue(0);
                }
            }
        }
    }

    public void queueToRemoveWaypoint(Waypoint waypoint) {
        if (this.waypointToDelete != null) {
            this.removeWaypoint(this.waypointToDelete);
        }

        this.waypointToDelete = waypoint;
    }
}
