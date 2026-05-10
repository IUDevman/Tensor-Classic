package dev.tensor.misc.gui;

import dev.tensor.TensorClassic;
import dev.tensor.feature.hacks.ClickGUI;
import dev.tensor.misc.gui.imp.Element;
import dev.tensor.misc.gui.imp.Hoverable;
import dev.tensor.misc.gui.imp.elements.ButtonSetting;
import dev.tensor.misc.gui.imp.elements.CategoryElement;
import dev.tensor.misc.gui.imp.elements.HackElement;
import dev.tensor.misc.gui.imp.elements.KeyPressElement;
import dev.tensor.misc.gui.imp.elements.settings.ButtonSettingElement;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.render.Render2D;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author IUDevman
 * @since 05-03-2021
 * <p>
 * A big thanks to qe7 for Hephastus-Babric. The source really helped troubleshoot parts that didn't port over well.
 * https://github.com/qe7/Hephaestus-Babric
 */

public final class TensorGUI extends GuiScreen implements Render2D, Hoverable {

    public final ClickGUI clickGUI;

    public final ArrayList<CategoryElement> categoryElements = new ArrayList<>();

    public final int guiWidth = 388;
    public final AtomicInteger guiHeight = new AtomicInteger(0);

    public final int titleBoxHeight = 20;

    private int scrollingSpeed = 0;

    private boolean isDragging = false;

    private int dragOffsetX;
    private int dragOffsetY;

    private int dragXToSet;
    private int dragYToSet;

    private final AtomicBoolean isButtonActive = new AtomicBoolean(false);
    private ButtonSetting activeButton = null;

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

    @Override
    public void drawScreen(int i, int j, float f) {

        this.categoryElements.forEach(categoryElement -> {

            if (categoryElement.isSelected()) {

                categoryElement.hackElements.forEach(hackElement -> {

                    if (hackElement.isViewed()) {
                        hackElement.settingElements.forEach(settingElement -> {

                            if (settingElement instanceof ButtonSettingElement) {

                                ButtonSettingElement element = (ButtonSettingElement) settingElement;

                                if (element.button.isActive()) {
                                    element.button.render(this, this.clickGUI.fillColor.getValue(), this.clickGUI.selectedFillColor.getValue(), this.clickGUI.outlineColor.getValue());
                                    this.activeButton = element.button;
                                    this.isButtonActive.set(true);
                                }
                            }
                        });
                    }
                });
            }
        });

        if (this.isButtonActive.get()) {
            return;
        }

        if (this.isDragging) {
            this.dragXToSet = i + dragOffsetX;
            this.dragYToSet = j + dragOffsetY;

            if (this.dragXToSet + this.guiWidth + 2 <= this.getMinecraft().currentScreen.width && this.dragOffsetX != 0) {
                this.clickGUI.x.setValue(dragXToSet);
            } else {
                this.dragXToSet = this.clickGUI.x.getValue();
            }

            if (this.dragYToSet + this.guiHeight.get() + 2 <= this.getMinecraft().currentScreen.height && this.dragOffsetY != 0) {
                this.clickGUI.y.setValue(dragYToSet);
            } else {
                this.dragYToSet = this.clickGUI.y.getValue();
            }
        }

        this.drawRectangle(clickGUI.x.getValue() + CategoryElement.width, clickGUI.y.getValue(), clickGUI.x.getValue() + CategoryElement.width + 2, clickGUI.y.getValue() + this.guiHeight.get(), clickGUI.outlineColor.getValue().getRGB());
        this.drawRectangle(clickGUI.x.getValue() + CategoryElement.width + HackElement.width + 2, clickGUI.y.getValue() + this.titleBoxHeight + 2, clickGUI.x.getValue() + CategoryElement.width + HackElement.width + 4, clickGUI.y.getValue() + this.guiHeight.get() - 2, clickGUI.outlineColor.getValue().getRGB());
        this.drawRectangle(clickGUI.x.getValue() + CategoryElement.width + 2, clickGUI.y.getValue() + this.titleBoxHeight, clickGUI.x.getValue() + this.guiWidth, clickGUI.y.getValue() + this.titleBoxHeight + 2, clickGUI.outlineColor.getValue().getRGB());
        this.drawRectangle(clickGUI.x.getValue() - 2, clickGUI.y.getValue() - 2, clickGUI.x.getValue() + this.guiWidth, clickGUI.y.getValue(), clickGUI.outlineColor.getValue().getRGB());
        this.drawRectangle(clickGUI.x.getValue() - 2, clickGUI.y.getValue() + this.guiHeight.get(), clickGUI.x.getValue() + guiWidth + 2, clickGUI.y.getValue() + this.guiHeight.get() + 2, clickGUI.outlineColor.getValue().getRGB());
        this.drawRectangle(clickGUI.x.getValue() - 2, clickGUI.y.getValue(), clickGUI.x.getValue(), clickGUI.y.getValue() + guiHeight.get(), clickGUI.outlineColor.getValue().getRGB());
        this.drawRectangle(clickGUI.x.getValue() + this.guiWidth, clickGUI.y.getValue() - 2, clickGUI.x.getValue() + this.guiWidth + 2, clickGUI.y.getValue() + this.guiHeight.get(), clickGUI.outlineColor.getValue().getRGB());
        this.drawRectangle(clickGUI.x.getValue() + CategoryElement.width + 2, clickGUI.y.getValue() + this.guiHeight.get() - 2, clickGUI.x.getValue() + this.guiWidth, clickGUI.y.getValue() + this.guiHeight.get(), clickGUI.outlineColor.getValue().getRGB());
        this.drawRectangle(clickGUI.x.getValue() + this.guiWidth - 1, clickGUI.y.getValue(), clickGUI.x.getValue() + this.guiWidth, clickGUI.y.getValue() + this.titleBoxHeight, clickGUI.outlineColor.getValue().getRGB());
        this.drawRectangle(clickGUI.x.getValue() + CategoryElement.width + 2, clickGUI.y.getValue(), clickGUI.x.getValue() + CategoryElement.width + 3, clickGUI.y.getValue() + this.titleBoxHeight, clickGUI.outlineColor.getValue().getRGB());
        this.drawRectangle(clickGUI.x.getValue() + CategoryElement.width + 3, clickGUI.y.getValue(), clickGUI.x.getValue() + this.guiWidth - 1, clickGUI.y.getValue() + 1, clickGUI.outlineColor.getValue().getRGB());
        this.drawRectangle(clickGUI.x.getValue() + CategoryElement.width + 3, clickGUI.y.getValue() + 1, clickGUI.x.getValue() + this.guiWidth - 1, clickGUI.y.getValue() + this.titleBoxHeight, clickGUI.fillColor.getValue().getRGB());

        String name = TensorClassic.MOD_NAME + " (" + ChatColor.YELLOW.text() + TensorClassic.MOD_VERSION + ChatColor.DEFAULT.text() + ")";
        this.drawCenteredString(name, clickGUI.x.getValue() + CategoryElement.width + 2 + ((this.guiWidth - CategoryElement.width + 2) / 2), clickGUI.y.getValue() + 2 + ((this.titleBoxHeight - 12) / 2), Color.WHITE.getRGB());

        this.categoryElements.forEach(categoryElement -> categoryElement.render(this, this.clickGUI.fillColor.getValue(), this.clickGUI.selectedFillColor.getValue(), this.clickGUI.outlineColor.getValue()));
    }

    @Override
    public void keyTyped(char c, int i) {
        if (i == Keyboard.KEY_ESCAPE) {
            super.keyTyped(c, i);
            return;
        }

        if (this.isButtonActive.get()) {

            if (this.activeButton != null) {
                activeButton.onKeyPressed(c, i);
            }

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
                                    element.onKeyPressed(c, i);
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

        if (this.isButtonActive.get()) {

            if (this.activeButton != null && k == 0) {
                this.activeButton.onClick(i, j);
            }

            return;
        }

        if (this.isHovered(this.clickGUI.x.getValue() + CategoryElement.width + 2, this.clickGUI.y.getValue(), this.clickGUI.x.getValue() + this.guiWidth, this.clickGUI.y.getValue() + this.titleBoxHeight + 2, i, j) && k == 0) {
            this.isDragging = true;

            this.dragXToSet = this.clickGUI.x.getValue();
            this.dragYToSet = this.clickGUI.y.getValue();

            int newDragOffsetX = this.dragXToSet - i;
            int newDragOffsetY = this.dragYToSet - j;

            if (newDragOffsetX + this.clickGUI.x.getValue() + this.guiWidth + 2 <= 0) {
                newDragOffsetX = this.dragXToSet;
            }

            if (newDragOffsetY + this.clickGUI.y.getValue() + this.guiHeight.get() + 2 <= 0) {
                newDragOffsetY = this.dragYToSet;
            }

            this.dragOffsetX = newDragOffsetX;
            this.dragOffsetY = newDragOffsetY;

        } else {
            this.isDragging = false;
        }

        if (k != 0) {
            return;
        }

        AtomicBoolean hasClicked = new AtomicBoolean(false);

        this.categoryElements.forEach(categoryElement -> {

            if (this.isHovered(categoryElement, i, j) && !categoryElement.isSelected()) {
                categoryElement.onClick(i, j);
                hasClicked.set(true);
                return;
            }

            if (!categoryElement.isSelected()) {
                return;
            }

            categoryElement.hackElements.forEach(hackElement -> {

                if (this.isHovered(hackElement, i, j)) {
                    hackElement.onClick(i, j);
                    hasClicked.set(true);
                    return;
                }

                if (!hackElement.isViewed()) {
                    return;
                }

                if (this.isHovered(hackElement.propertyElement, i, j)) {
                    hackElement.propertyElement.onClick(i, j);
                    hasClicked.set(true);
                    return;
                }

                hackElement.settingElements.forEach(settingElement -> {

                    if (this.isHovered(settingElement, i, j)) {
                        settingElement.onClick(i, j);
                        hasClicked.set(true);
                    }
                });
            });
        });

        if (!hasClicked.get()) {
            this.categoryElements.forEach(categoryElement -> {

                if (categoryElement.isSelected()) {
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
        }
    }

    @Override
    public void handleMouseInput() {

        if (Mouse.getEventButton() == 0) {
            this.isDragging = false;
        }

        if (!Mouse.getEventButtonState()) {

            int dWheel = Mouse.getDWheel();

            if (dWheel > 0) {
                scrollingSpeed = this.getScrollSpeed();
            } else if (dWheel < 0) {
                scrollingSpeed = -this.getScrollSpeed();
            } else {
                scrollingSpeed = 0;
            }
        }

        super.handleMouseInput();
    }

    @Override
    protected void mouseMovedOrUp(int i, int j, int k) {

        if (this.isButtonActive.get()) {

            if (this.activeButton != null) {
                this.activeButton.onHovered(i, j);
                activeButton.handleScroll(this.scrollingSpeed);
            }

            return;
        }

        if (this.isHovered(this.clickGUI.x.getValue() + CategoryElement.width + 2, this.clickGUI.y.getValue() + this.titleBoxHeight + 2, this.clickGUI.x.getValue() + CategoryElement.width + HackElement.width + 2, this.clickGUI.y.getValue() + this.guiHeight.get() - 2, i, j)) {
            this.handleScroll(Scroll.Hack);
        } else if (this.isHovered(this.clickGUI.x.getValue() + CategoryElement.width + HackElement.width + 4, this.clickGUI.y.getValue() + this.titleBoxHeight + 2, this.clickGUI.x.getValue() + this.guiWidth - 2, this.clickGUI.y.getValue() + this.guiHeight.get() - 2, i, j)) {
            this.handleScroll(Scroll.Setting);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        this.categoryElements.forEach(categoryElement -> {
            this.scrollingSpeed = 0;

            categoryElement.mScrollY.reset();
            categoryElement.hackElements.forEach(hackElement -> hackElement.sScrollY.reset());

            if (categoryElement.isSelected()) {
                categoryElement.hackElements.forEach(hackElement -> {

                    if (hackElement.isViewed()) {
                        hackElement.settingElements.forEach(settingElement -> {

                            if (settingElement instanceof KeyPressElement) {
                                KeyPressElement element = (KeyPressElement) settingElement;

                                element.setSearching(false);
                            } else if (settingElement instanceof ButtonSettingElement) {
                                ButtonSettingElement element = (ButtonSettingElement) settingElement;

                                element.button.close();
                            }
                        });
                    }
                });
            }
        });

        this.clickGUI.setEnabled(false);
    }

    public void closeButton() {
        this.isButtonActive.set(false);
        this.activeButton = null;
    }

    private boolean isHovered(Element element, double mouseX, double mouseY) {

        if (!(element instanceof CategoryElement)) {
            if (mouseY < this.clickGUI.y.getValue() + this.titleBoxHeight + 2) {
                return false;
            }

            if (mouseY > this.clickGUI.y.getValue() + this.guiHeight.get() - 4) {
                return false;
            }
        }

        return this.isHovered(element.getPosX(), element.getPosY(), element.getPosX() + element.getWidth(), element.getPosY() + element.getHeight(), mouseX, mouseY);
    }

    private void handleScroll(Scroll scroll) {
        if (this.scrollingSpeed == 0) {
            return;
        }

        if (scroll == Scroll.Hack) {

            this.categoryElements.forEach(categoryElement -> {

                if (categoryElement.isSelected()) {

                    IntegerSetting hackScrollY = categoryElement.mScrollY;

                    AtomicInteger hackHeight = new AtomicInteger();

                    categoryElement.hackElements.forEach(hackElement -> hackHeight.getAndAdd(hackElement.getHeight()));

                    int scrollAdjust = this.scrollingSpeed;

                    if (hackScrollY.getValue() > -this.getScrollSpeed() && this.scrollingSpeed > 0) {

                        scrollAdjust = -hackScrollY.getValue();
                    }

                    if (hackHeight.get() + (this.scrollingSpeed < 0 ? hackScrollY.getValue() - this.getScrollSpeed() : 0) <= this.guiHeight.get() - 22) {
                        int test = ((hackHeight.get() + (this.scrollingSpeed < 0 ? hackScrollY.getValue() : 0)) - (this.guiHeight.get() - 22));

                        if (test >= scrollAdjust) {
                            scrollAdjust = -test;
                        } else {
                            return;
                        }
                    }

                    hackScrollY.setValue(hackScrollY.getValue() + scrollAdjust);
                }
            });

        } else if (scroll == Scroll.Setting) {

            this.categoryElements.forEach(categoryElement -> {

                if (categoryElement.isSelected()) {

                    categoryElement.hackElements.forEach(hackElement -> {

                        if (hackElement.isViewed()) {

                            IntegerSetting settingScrollY = hackElement.sScrollY;

                            AtomicInteger settingHeight = new AtomicInteger();

                            settingHeight.getAndAdd(hackElement.propertyElement.getHeight());

                            hackElement.settingElements.forEach(settingElement -> settingHeight.getAndAdd(settingElement.getHeight()));

                            int scrollAdjust = this.scrollingSpeed;

                            if (settingScrollY.getValue() > -this.getScrollSpeed() && this.scrollingSpeed > 0) {

                                scrollAdjust = -settingScrollY.getValue();
                            }

                            if (settingHeight.get() + (this.scrollingSpeed < 0 ? settingScrollY.getValue() - this.getScrollSpeed() : 0) <= this.guiHeight.get() - 22) {
                                int test = ((settingHeight.get() + (this.scrollingSpeed < 0 ? settingScrollY.getValue() : 0)) - (this.guiHeight.get() - 22));

                                if (test >= scrollAdjust) {
                                    scrollAdjust = -test;
                                } else {
                                    return;
                                }
                            }

                            settingScrollY.setValue(settingScrollY.getValue() + scrollAdjust);
                        }
                    });
                }
            });

        }
    }

    public boolean isSearching() {

        if (this.isButtonActive.get() && this.activeButton != null && this.activeButton.isSearching()) {
            return true;
        }

        AtomicBoolean isSearching = new AtomicBoolean(false);

        this.categoryElements.forEach(categoryElement -> {

            if (categoryElement.isSelected()) {
                categoryElement.hackElements.forEach(hackElement -> {

                    if (hackElement.isViewed()) {
                        hackElement.settingElements.forEach(settingElement -> {

                            if (settingElement instanceof KeyPressElement) {
                                KeyPressElement element = (KeyPressElement) settingElement;

                                if (element.isSearching()) {
                                    isSearching.set(true);
                                }
                            }
                        });
                    }
                });
            }
        });

        return isSearching.get();
    }

    public int getScrollSpeed() {
        return this.isButtonActive.get() ? this.clickGUI.scrollSpeedWindow.getValue() : this.clickGUI.scrollSpeedGUI.getValue();
    }

    private enum Scroll {
        Hack,
        Setting
    }
}
