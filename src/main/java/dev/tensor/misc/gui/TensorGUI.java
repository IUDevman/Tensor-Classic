package dev.tensor.misc.gui;

import dev.tensor.TensorClassic;
import dev.tensor.feature.hacks.ClickGUI;
import dev.tensor.misc.gui.elements.CategoryElement;
import dev.tensor.misc.gui.elements.HackElement;
import dev.tensor.misc.gui.elements.KeyPressElement;
import dev.tensor.misc.imp.Globals;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author IUDevman
 * @since 05-03-2021
 */

public final class TensorGUI extends GuiScreen implements Globals {

    private final ClickGUI clickGUI;

    public final ArrayList<CategoryElement> categoryElements = new ArrayList<>();

    public final AtomicInteger guiHeight = new AtomicInteger(0);

    public TensorGUI(ClickGUI clickGUI) {
        this.clickGUI = clickGUI;

        AtomicInteger offsetY = new AtomicInteger(0);

        Arrays.stream(Category.values()).forEach(category -> {
            CategoryElement categoryElement = new CategoryElement(this, category, this.clickGUI.x, this.clickGUI.y, 0, offsetY.get());
            this.categoryElements.add(categoryElement);
            offsetY.getAndAdd(categoryElement.getHeight());
            guiHeight.getAndAdd(categoryElement.getHeight());

            if (this.categoryElements.size() == 1) {
                categoryElement.setSelected(true);
            }
        });
    }

    public final int guiWidth = 388;
    public final int titleHeight = 20;

    @Override
    public void drawScreen(int i, int j, float f) {

        this.draw2DRectangle(clickGUI.x.getValue() + CategoryElement.width, clickGUI.y.getValue(), clickGUI.x.getValue() + CategoryElement.width + 2, clickGUI.y.getValue() + this.guiHeight.get(), clickGUI.outlineColor.getValue().getRGB());
        this.draw2DRectangle(clickGUI.x.getValue() + CategoryElement.width + HackElement.width + 2, clickGUI.y.getValue() + this.titleHeight + 2, clickGUI.x.getValue() + CategoryElement.width + HackElement.width + 4, clickGUI.y.getValue() + this.guiHeight.get() - 2, clickGUI.outlineColor.getValue().getRGB());
        this.draw2DRectangle(clickGUI.x.getValue() + CategoryElement.width + 2, clickGUI.y.getValue() + this.titleHeight, clickGUI.x.getValue() + this.guiWidth, clickGUI.y.getValue() + this.titleHeight + 2, clickGUI.outlineColor.getValue().getRGB());
        this.draw2DRectangle(clickGUI.x.getValue() - 2, clickGUI.y.getValue() - 2, clickGUI.x.getValue() + this.guiWidth, clickGUI.y.getValue(), clickGUI.outlineColor.getValue().getRGB());
        this.draw2DRectangle(clickGUI.x.getValue() - 2, clickGUI.y.getValue() + this.guiHeight.get(), clickGUI.x.getValue() + guiWidth + 2, clickGUI.y.getValue() + this.guiHeight.get() + 2, clickGUI.outlineColor.getValue().getRGB());
        this.draw2DRectangle(clickGUI.x.getValue() - 2, clickGUI.y.getValue(), clickGUI.x.getValue(), clickGUI.y.getValue() + guiHeight.get(), clickGUI.outlineColor.getValue().getRGB());
        this.draw2DRectangle(clickGUI.x.getValue() + this.guiWidth, clickGUI.y.getValue() - 2, clickGUI.x.getValue() + this.guiWidth + 2, clickGUI.y.getValue() + this.guiHeight.get(), clickGUI.outlineColor.getValue().getRGB());
        this.draw2DRectangle(clickGUI.x.getValue() + CategoryElement.width + 2, clickGUI.y.getValue() + this.guiHeight.get() - 2, clickGUI.x.getValue() + this.guiWidth, clickGUI.y.getValue() + this.guiHeight.get(), clickGUI.outlineColor.getValue().getRGB());
        this.draw2DRectangle(clickGUI.x.getValue() + CategoryElement.width + 2, clickGUI.y.getValue(), clickGUI.x.getValue() + this.guiWidth, clickGUI.y.getValue() + this.titleHeight, clickGUI.fillColor.getValue().getRGB());

        String name = TensorClassic.MOD_NAME + " (" + this.getColor(ChatColor.YELLOW) + TensorClassic.MOD_VERSION + this.getColor(ChatColor.DEFAULT) + ")";
        this.getInGameGUI().drawCenteredString(this.getFontRenderer(), name, clickGUI.x.getValue() + CategoryElement.width + 2 + ((this.guiWidth - CategoryElement.width + 2) / 2), clickGUI.y.getValue() + 2 + ((this.titleHeight - 12) / 2), clickGUI.textColor.getValue().getRGB());

        this.categoryElements.forEach(categoryElement -> categoryElement.render(this, this.clickGUI.fillColor.getValue(), this.clickGUI.selectedFillColor.getValue(), this.clickGUI.outlineColor.getValue(),  this.clickGUI.textColor.getValue(), this.clickGUI.enabledTextColor.getValue()));
    }

    @Override
    public void keyTyped(char c, int i) {
        if (i == Keyboard.KEY_ESCAPE) {
            super.keyTyped(c, i);
            return;
        }

        this.categoryElements.forEach(categoryElement -> {

            if (categoryElement.isSelected()) {

                categoryElement.hackElements.forEach(hackElement -> {

                    if (hackElement.isViewed()) {

                        hackElement.settingElements.forEach(settingElement -> {

                            if (settingElement instanceof KeyPressElement) {

                                KeyPressElement element = (KeyPressElement) settingElement;

                                if (element.isSearching()) {
                                    element.onKeyPressed(i);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void mouseClicked(int i, int j, int k) {
        if (k != 0) {
            return;
        }

        this.categoryElements.forEach(categoryElement -> {

            if (this.isHovered(categoryElement, i, j) && !categoryElement.isSelected()) {
                categoryElement.onClick(i, j);
                return;
            }

            if (!categoryElement.isSelected()) {
                return;
            }

            categoryElement.hackElements.forEach(hackElement -> {

                if (this.isHovered(hackElement, i, j)) {
                    hackElement.onClick(i, j);
                    return;
                }

                if (!hackElement.isViewed()) {
                    return;
                }

                if (this.isHovered(hackElement.propertyElement, i, j)) {
                    hackElement.propertyElement.onClick(i, j);
                    return;
                }

                hackElement.settingElements.forEach(settingElement -> {

                    if (this.isHovered(settingElement, i, j)) {
                        settingElement.onClick(i, j);
                        return;
                    }
                });

            });

        });
    }

    private int scroll = 0;

    @Override
    public void handleMouseInput() {

        if (!Mouse.getEventButtonState()) {

            int dWheel = Mouse.getDWheel();

            //pan up
            if (dWheel > 0) {
                scroll = -10;
                //pan down
            } else if (dWheel < 0) {
                scroll = 10;
            } else {
                scroll = 0;
            }
        }

        super.handleMouseInput();
    }

    @Override
    protected void mouseMovedOrUp(int i, int j, int k) {

        if (this.isHovered(this.clickGUI.x.getValue() + CategoryElement.width + HackElement.width + 4, this.clickGUI.y.getValue()+ this.titleHeight + 2, this.clickGUI.x.getValue() + this.guiWidth - 2, this.clickGUI.y.getValue() + this.guiHeight.get() - 2, i, j)) {
            this.handleScroll(Scroll.Setting);
        }
    }

    private void handleScroll(Scroll scroll) {
        if (this.scroll == 0) {
            return;
        }

        if (scroll == Scroll.Hack) {

            this.categoryElements.forEach(categoryElement -> {

                if (categoryElement.isSelected()) {

                    IntegerSetting hackScrollY = categoryElement.mScrollY;
                }
            });

        } else if (scroll == Scroll.Setting) {

            this.categoryElements.forEach(categoryElement -> {

                if (categoryElement.isSelected()) {

                    categoryElement.hackElements.forEach(hackElement -> {

                        if (hackElement.isViewed()) {

                            IntegerSetting settingScrollY = hackElement.sScrollY;

                            AtomicInteger settingHeight = new AtomicInteger(22);

                            settingHeight.getAndAdd(hackElement.propertyElement.getHeight());

                            hackElement.settingElements.forEach(settingElement -> settingHeight.getAndAdd(settingElement.getHeight()));

                            if (settingScrollY.getValue() > -10 && this.scroll > 0) {
                                return;
                            }

                            //todo: add cap for bottom

                            settingScrollY.setValue(settingScrollY.getValue() + this.scroll);
                        }
                    });
                }
            });

        }
    }

    private enum Scroll {
        Hack,
        Setting
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        this.categoryElements.forEach(categoryElement -> {
            this.scroll = 0;

            categoryElement.mScrollY.reset();
            categoryElement.hackElements.forEach(hackElement -> hackElement.sScrollY.reset());

            if (categoryElement.isSelected())  {
                categoryElement.hackElements.forEach(hackElement -> {

                    if (hackElement.isViewed()) {
                        hackElement.settingElements.forEach(settingElement -> {

                            if (settingElement instanceof KeyPressElement) {
                                KeyPressElement element = (KeyPressElement) settingElement;

                                element.setSearching(false);
                            }
                        });
                    }
                });
            }
        });

        this.clickGUI.setEnabled(false);
    }

    private boolean isHovered(Element element, double mouseX, double mouseY) {
        return this.isHovered(element.getPosX(), element.getPosY(), element.getPosX() + element.getWidth(), element.getPosY() + element.getHeight(), mouseX, mouseY);
    }

    private boolean isHovered(int minX, int minY, int maxX, int maxY, double mouseX, double mouseY) {
        if (mouseX < minX || mouseX > maxX) {
            return false;
        }

        else return !(mouseY < minY) && !(mouseY > maxY);
    }
}
