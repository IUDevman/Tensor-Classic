package dev.tensor.feature.hacks.hud;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.HUDObject;
import dev.tensor.misc.imp.setting.settings.ColorSettingNoAlpha;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * @author Hoosiers
 * @since 04-05-2026
 */

@Hack.Info(name = "Slug", category = Category.HUD)
public final class Slug extends Hack implements HUDObject {

    public final IntegerSetting x = new IntegerSetting("X", 0, 0, this.getMaxX());
    public final IntegerSetting y = new IntegerSetting("Y", 369, 0, this.getMaxY());
    public final DoubleSetting scale = new DoubleSetting("Scale", 0.50, 0.10, 2.00, 2);
    public final EnumSetting mode = new EnumSetting("Mode", Mode.None);
    public final ColorSettingNoAlpha color = new ColorSettingNoAlpha("Color", Color.WHITE);

    private int texture = -1;

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
        return 256;
    }

    @Override
    public int getHeight() {
        return 256;
    }

    @Override
    public int getWidthScaled() {
        return (int) (this.getWidth() * this.scale.getValue());
    }

    @Override
    public int getHeightScaled() {
        return (int) (this.getHeight() * this.scale.getValue());
    }

    private int rotation = 0;

    @Override
    public void onClientTick() {
        if (this.rotation >= 360) {
            this.rotation = 0;
        } else {
            this.rotation += 20;
        }
    }

    @Override
    public void render() {
        if (texture == -1) {
            this.setupTexture();
        }

        if (texture == -1) {
            return;
        }

        this.prepareScale();

        this.getMinecraft().renderEngine.bindTexture(texture);

        GL11.glColor4f(this.color.getValue().getRed() / 255F, this.color.getValue().getGreen() / 255F, this.color.getValue().getBlue() / 255F, 1F);

        if (this.mode.getValue().equals(Mode.VibeCoded)) {
            GL11.glTranslatef(128F, 128F, -0F);
            GL11.glRotatef(this.rotation, 0F, 0F, 0F);
            GL11.glTranslatef(128, 0F, 0F);

        } else if (this.mode.getValue().equals(Mode.Circle)) {
            GL11.glTranslatef(128F, 128F, 0F);
            GL11.glRotatef(-this.rotation, 0.0F, 0.0F, 1.0f);

        } else if (this.mode.getValue().equals(Mode.Rotate)) {
            GL11.glTranslatef(128F, 128F, 0F);
            GL11.glRotatef(-this.rotation, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-128F, -128F, 0.0F);
        }

        this.drawTexturedRectangle(0, 0, 0, 0, 256, 256);

        this.finishScale();
    }

    @SuppressWarnings("deprecation")
    private void setupTexture() {
        try {
            Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer("tensorclassic");

            if (!modContainer.isPresent()) {
                return;
            }

            Path imagePath = modContainer.get().getPath("assets/tensorclassic/Slug.png");

            InputStream inputStream = Files.newInputStream(imagePath);

            BufferedImage image = ImageIO.read(inputStream);

            this.texture = this.getMinecraft().renderEngine.allocateAndSetupTexture(image);

        } catch (Exception ignored) {
            this.getTensorLogger().info("Failed to load slug texture!");
        }
    }

    public void drawTexturedRectangle(int minX, int minY, int offsetX, int offsetY, int imageWidth, int imageHeight) {
        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.addVertexWithUV(minX, minY + imageHeight, 0, (float) (offsetX) * 0.00390625F, (float) (offsetY + imageHeight) * 0.00390625F);
        Tessellator.instance.addVertexWithUV(minX + imageWidth, minY + imageHeight, 0, (float) (offsetX + imageWidth) * 0.00390625F, (float) (offsetY + imageHeight) * 0.00390625F);
        Tessellator.instance.addVertexWithUV(minX + imageWidth, minY, 0, (float) (offsetX + imageWidth) * 0.00390625F, (float) (offsetY) * 0.00390625F);
        Tessellator.instance.addVertexWithUV(minX, minY, 0, (float) (offsetX) * 0.00390625F, (float) (offsetY) * 0.00390625F);
        Tessellator.instance.draw();
    }

    private enum Mode {
        None,
        Rotate,
        Circle,
        VibeCoded
    }
}
