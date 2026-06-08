package dev.tensor.backend.mixins;

import dev.tensor.backend.MixinPriority;
import dev.tensor.feature.hacks.ClickGUI;
import dev.tensor.feature.managers.HackManager;
import dev.tensor.misc.gui.TensorGUI;
import dev.tensor.misc.imp.render.Render2D;
import dev.tensor.misc.imp.render.RenderStatic;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Hoosiers
 * @since 06-07-2026
 */

@Mixin(value = GuiMainMenu.class, priority = MixinPriority.VALUE)
public abstract class GuiMainMenuMixin extends GuiScreen implements Render2D {

    @Unique
    private boolean loaded = false;

    @Unique
    private int tb1 = -1;

    @Unique
    private int tb2 = -1;

    @Unique
    private int tb3 = -1;

    @Unique
    private int tb4 = -1;

    @Unique
    private int logo = -1;

    @SuppressWarnings("unchecked")
    @Inject(method = "initGui", at = @At("HEAD"), cancellable = true)
    public void tensor_onInitGui(CallbackInfo ci) {
        if (!this.loaded) {
            this.tb1 = this.setupTexture("gui/background_1.png", RenderStatic.BACKGROUND_TEXTURE_1_ID);
            this.tb2 = this.setupTexture("gui/background_2.png", RenderStatic.BACKGROUND_TEXTURE_2_ID);
            this.tb3 = this.setupTexture("gui/background_3.png", RenderStatic.BACKGROUND_TEXTURE_3_ID);
            this.tb4 = this.setupTexture("gui/background_4.png", RenderStatic.BACKGROUND_TEXTURE_4_ID);
            this.logo = this.setupTexture("gui/logo.png", RenderStatic.BACKGROUND_LOGO_ID);

            this.loaded = true;
        }

        StringTranslate translate = StringTranslate.getInstance();

        int centerX = this.width / 2 - 100;
        int startY = this.height / 4 + 48;

        this.controlList.add(new GuiButton(1, centerX, startY, translate.translateKey("menu.singleplayer")));

        GuiButton multiplayer = new GuiButton(2, centerX, startY + 24, translate.translateKey("menu.multiplayer"));
        multiplayer.enabled = true;

        this.controlList.add(multiplayer);

        this.controlList.add(new GuiButton(3, centerX, startY + 48, translate.translateKey("menu.mods")));
        this.controlList.add(new GuiButton(4, centerX, startY + 72, translate.translateKey("menu.options")));
        this.controlList.add(new GuiButton(5, centerX, startY + 96, "Tensor Classic GUI"));
        this.controlList.add(new GuiButton(6, centerX, startY + 120, translate.translateKey("menu.quit")));

        ci.cancel();
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    public void tensor_onActionPerformed(GuiButton guiButton, CallbackInfo ci) {
        switch (guiButton.id) {
            case 1:
                this.getMinecraft().displayGuiScreen(new GuiSelectWorld(this));
                break;

            case 2:
                this.getMinecraft().displayGuiScreen(new GuiMultiplayer(this));
                break;

            case 3:
                this.mc.displayGuiScreen(new GuiTexturePacks(this));
                break;

            case 4:
                this.getMinecraft().displayGuiScreen(new GuiOptions(this, this.getMinecraft().gameSettings));
                break;

            case 5:
                ClickGUI clickGUI = this.tensor_getClickGUI();
                if (clickGUI != null) {

                    TensorGUI tensorGUI = new TensorGUI(clickGUI, this);

                    clickGUI.tensorGUI = tensorGUI;

                    this.getMinecraft().displayGuiScreen(tensorGUI);
                }
                break;

            case 6:
                this.getMinecraft().shutdown();
                break;
        }

        ci.cancel();
    }

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    public void tensor_onDrawScreen(int x, int y, float partialTicks, CallbackInfo ci) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);

        GL11.glColor4f(176 / 255F, 24 / 255F, 24 / 255F, 1.0F);

        this.tensor_drawBackground(this.tb1, 0, 640, 360);
        this.tensor_drawBackground(this.tb2, 1, 640, 360);
        this.tensor_drawBackground(this.tb3, 2, 640, 360);
        this.tensor_drawBackground(this.tb4, 3, 640, 360);

        GL11.glColor4f(1F, 1F, 1F, 1F);

        this.tensor_drawBackground(this.logo, 4, 640, 162);

        super.drawScreen(x, y, partialTicks);

        ci.cancel();
    }

    @Unique
    private ClickGUI tensor_getClickGUI() {
        HackManager hackManager = this.getHackManager();

        if (hackManager == null) {
            return null;
        }

        return this.getHackManager().getHack(ClickGUI.class);
    }

    @Unique
    public void tensor_drawBackground(int background, int number, int width, int height) {
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

        this.drawTexturedRectangle(startX, startY, width, height, (double) this.width / 1280, (double) this.height / 720);
    }
}
