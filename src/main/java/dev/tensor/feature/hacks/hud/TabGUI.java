package dev.tensor.feature.hacks.hud;

import dev.tensor.TensorClassic;
import dev.tensor.backend.events.variable.KeyPressedEvent;
import dev.tensor.feature.hacks.*;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.gui.imp.TabGUIEntry;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.ColorSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 03-15-2026
 */

@Hack.Info(name = "TabGUI", category = Category.HUD)
public final class TabGUI extends Hack implements HUDObject {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.HUD_Shading_and_Images;
    }

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 654, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 1.0, 0.5, 3.0, 1);
    public final IntegerSetting page = new IntegerSetting("Page", 1, 1, 1000);
    public final ColorSetting fillColor = new ColorSetting("Fill Color", new Color(0, 0, 0, 150));
    public final ColorSetting outlineColor = new ColorSetting("Outline Color", new Color(62, 11, 10, 200));

    private final ArrayList<Class<? extends Hack>> BLACKLISTED = new ArrayList<>();

    private final int minPage = 1;
    private int maxPage = 1;

    private int getPage() {
        int page = this.page.getValue();

        if (page > this.maxPage) {
            this.page.setValue(this.maxPage);
        } else if (page < this.minPage) {
            this.page.setValue(this.minPage);
        }

        return this.page.getValue();
    }

    private void setPage(int page) {
        if (page > this.maxPage) {
            page = this.minPage;
        } else if (page < this.minPage) {
            page = this.maxPage;
        }

        this.page.setValue(page);
    }

    private final String clientMessage = TensorClassic.MOD_NAME + " " + ChatColor.YELLOW.text() + TensorClassic.MOD_VERSION + ChatColor.DEFAULT.text() + " >>> By Hoosiers :)";

    private final ArrayList<TabGUIEntry> tabGUIEntries = new ArrayList<>();


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
        return this.getFontRenderer().getStringWidth(clientMessage) + 4;
    }

    @Override
    public int getHeight() {
        return 66;
    }

    @Override
    public int getWidthScaled() {
        return (int) (this.getWidth() * this.scale.getValue());
    }

    @Override
    public int getHeightScaled() {
        return (int) (this.getHeight() * this.scale.getValue());
    }

    @Override
    public void render() {
        int maxX = this.getFontRenderer().getStringWidth(clientMessage) + 3;
        int maxY = 65;

        this.prepareScale();

        this.drawOutlinedRectangle(0, 0, maxX + 1, maxY + 1, 1, this.fillColor.getValueRainbow().getRGB(), this.outlineColor.getValueRainbow().getRGB(), true);

        this.drawString(clientMessage, 2, 2, Color.WHITE.getRGB());

        if (tabGUIEntries.isEmpty()) {
            this.setUpTabGUI();
        }

        double maxPage = this.tabGUIEntries.size() / 5d;

        this.maxPage = (int) Math.ceil(maxPage);

        String pageString = "(" + this.getPage() + "/" + this.maxPage + ")";

        int pageX = this.getFontRenderer().getStringWidth(this.clientMessage) - this.getFontRenderer().getStringWidth(pageString);

        this.drawString(pageString, pageX + 2, maxY - 9, Color.WHITE.getRGB());

        for (int i = 0; i <= 4; i++) {

            String dumbassWayOfDoingThis;

            if (i == 0 || i == 2 || i == 4) {
                dumbassWayOfDoingThis = "> ";
            } else {
                dumbassWayOfDoingThis = ">> ";
            }

            int pageI = i + ((this.getPage() - 1) * 5);

            if (this.tabGUIEntries.size() - 1 >= pageI) {
                this.drawString(dumbassWayOfDoingThis + this.tabGUIEntries.get(pageI).entry, 2, 4 + (10 * (i + 1)), this.tabGUIEntries.get(pageI).color.getRGB());
            }
        }

        this.finishScale();
    }

    private void setUpTabGUI() {
        this.tabGUIEntries.add(new TabGUIEntry("[" + this.getHackManager().getHack(Commands.class).prefix.getValue() + "Commands] List", Color.CYAN));

        this.getHackManager().getHacks().forEach(hack -> {

            if (!this.shouldIgnoreHack(hack)) {
                this.tabGUIEntries.add(new TabGUIEntry(hack.NAME + " [" + Keyboard.getKeyName(hack.BIND) + "]", hack.isEnabled() ? Color.GREEN : Color.RED));
            }
        });
    }

    private boolean shouldIgnoreHack(Hack hack) {
        if (this.BLACKLISTED.isEmpty()) {
            this.BLACKLISTED.add(ClientMessages.class);
            this.BLACKLISTED.add(Commands.class);
            this.BLACKLISTED.add(TabGUI.class);
            this.BLACKLISTED.add(CommandPreview.class);
            this.BLACKLISTED.add(DiscordRPC.class);
            this.BLACKLISTED.add(Rotations.class);
            this.BLACKLISTED.add(TCPNoDelay.class);
        }

        return this.BLACKLISTED.contains(hack.getClass());
    }

    @SuppressWarnings("unused")
    EventAction<KeyPressedEvent> keyPressedAction = this.addHackEventAction(new EventAction<>(KeyPressedEvent.class, event -> {
        if (event.KEY_CODE == Keyboard.KEY_LEFT) {
            this.setPage(this.getPage() - 1);
        } else if (event.KEY_CODE == Keyboard.KEY_RIGHT) {
            this.setPage(this.getPage() + 1);
        }
    }));
}
