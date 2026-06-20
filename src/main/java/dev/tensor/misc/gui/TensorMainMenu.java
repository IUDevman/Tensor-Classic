package dev.tensor.misc.gui;

import dev.tensor.feature.hacks.ClickGUI;
import dev.tensor.feature.hacks.MainMenu;
import dev.tensor.misc.imp.render.Render2D;
import dev.tensor.misc.imp.render.RenderStatic;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * @author Hoosiers
 * @since 06-07-2026
 */

@SuppressWarnings("FieldCanBeLocal")
public final class TensorMainMenu implements Render2D {

    private static TensorMainMenu INSTANCE;

    public static TensorMainMenu getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TensorMainMenu();
        }

        return INSTANCE;
    }

    private final int IMAGE_CHUNK_WIDTH = 640;
    private final int IMAGE_CHUNK_HEIGHT = 360;
    private final int LOGO_HEIGHT = 162;

    private final MainMenu mainMenu;

    private int tb1 = -1;
    private int tb2 = -1;
    private int tb3 = -1;
    private int tb4 = -1;
    private int logo = -1;

    private TensorMainMenu() {
        this.mainMenu = this.getHackManager().getHack(MainMenu.class);

        if (this.mainMenu == null || !this.mainMenu.isEnabled()) {
            return;
        }

        this.tb1 = this.setupTexture("gui/background_1.png", RenderStatic.BACKGROUND_TEXTURE_1_ID);
        this.tb2 = this.setupTexture("gui/background_2.png", RenderStatic.BACKGROUND_TEXTURE_2_ID);
        this.tb3 = this.setupTexture("gui/background_3.png", RenderStatic.BACKGROUND_TEXTURE_3_ID);
        this.tb4 = this.setupTexture("gui/background_4.png", RenderStatic.BACKGROUND_TEXTURE_4_ID);
        this.logo = this.setupTexture("gui/logo.png", RenderStatic.BACKGROUND_LOGO_ID);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void onInitialization(int width, int height, List controlList) {
        if (!this.isActive()) {
            return;
        }

        StringTranslate translate = StringTranslate.getInstance();

        int centerX = width / 2 - 100;
        int startY = height / 4 + 48;

        controlList.add(new GuiButton(1, centerX - 102, startY, translate.translateKey("menu.singleplayer")));

        GuiButton multiplayer = new GuiButton(2, centerX + 102, startY, translate.translateKey("menu.multiplayer"));
        multiplayer.enabled = true;

        controlList.add(multiplayer);

        controlList.add(new GuiButton(3, centerX - 102, startY + 24, translate.translateKey("menu.mods")));
        controlList.add(new GuiButton(4, centerX - 102, startY + 48, translate.translateKey("menu.options")));
        controlList.add(new GuiButton(5, centerX, startY + 72, "Tensor Classic GUI"));
        controlList.add(new GuiButton(6, centerX + 102, startY + 48, translate.translateKey("menu.quit")));
        controlList.add(new GuiButton(7, centerX + 102, startY + 24, "Play 2beta2t.net"));
    }

    public void onActionPerformed(GuiScreen instance, GuiButton guiButton) {
        if (!this.isActive()) {
            return;
        }

        switch (guiButton.id) {
            case 1:
                this.getMinecraft().displayGuiScreen(new GuiSelectWorld(instance));
                break;

            case 2:
                this.getMinecraft().displayGuiScreen(new GuiMultiplayer(instance));
                break;

            case 3:
                this.getMinecraft().displayGuiScreen(new GuiTexturePacks(instance));
                break;

            case 4:
                this.getMinecraft().displayGuiScreen(new GuiOptions(instance, this.getMinecraft().gameSettings));
                break;

            case 5:
                ClickGUI clickGUI = this.getHackManager().getHack(ClickGUI.class);

                if (clickGUI != null) {

                    TensorGUI tensorGUI = new TensorGUI(clickGUI, instance);

                    clickGUI.tensorGUI = tensorGUI;

                    this.getMinecraft().displayGuiScreen(tensorGUI);
                }
                break;

            case 6:
                this.getMinecraft().shutdown();
                break;

            case 7:
                this.getMinecraft().displayGuiScreen(new GuiConnecting(this.getMinecraft(), "2beta2t.net", 25565));
                break;
        }
    }

    public void onDrawScreen(int screenWidth, int screenHeight) {
        if (!this.isActive()) {
            return;
        }

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);

        float[] color = this.mainMenu.getColorArray();

        GL11.glColor4f(color[0], color[1], color[2], 1.0F);

        this.drawCustomBackground(this.tb1, 0, this.IMAGE_CHUNK_WIDTH, this.IMAGE_CHUNK_HEIGHT, screenWidth, screenHeight);
        this.drawCustomBackground(this.tb2, 1, this.IMAGE_CHUNK_WIDTH, this.IMAGE_CHUNK_HEIGHT, screenWidth, screenHeight);
        this.drawCustomBackground(this.tb3, 2, this.IMAGE_CHUNK_WIDTH, this.IMAGE_CHUNK_HEIGHT, screenWidth, screenHeight);
        this.drawCustomBackground(this.tb4, 3, this.IMAGE_CHUNK_WIDTH, this.IMAGE_CHUNK_HEIGHT, screenWidth, screenHeight);

        GL11.glColor4f(1F, 1F, 1F, 1F);

        this.drawCustomBackground(this.logo, 4, this.IMAGE_CHUNK_WIDTH, this.LOGO_HEIGHT, screenWidth, screenHeight);
    }

    public boolean isActive() {
        return this.mainMenu != null && this.mainMenu.isEnabled();
    }

    private void drawCustomBackground(int background, int number, int width, int height, int screenWidth, int screenHeight) {
        if (background == -1) {
            return;
        }

        this.getMinecraft().renderEngine.bindTexture(background);

        int startX = 0;
        int startY = 0;

        if (number == 1) {
            startX = width;

        } else if (number == 2) {
            startY = height;

        } else if (number == 3) {
            startX = width;
            startY = height;

        } else if (number == 4) {
            startX = (1280 / 2) - (width / 2);
            startY = 30;
        }

        this.drawTexturedRectangle(startX, startY, width, height, (double) screenWidth / 1280, (double) screenHeight / 720);
    }
}
