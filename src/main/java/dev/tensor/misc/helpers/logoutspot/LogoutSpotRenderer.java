package dev.tensor.misc.helpers.logoutspot;

import dev.tensor.misc.imp.render.Render3D;
import dev.tensor.misc.imp.render.task.RenderTask;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.RenderManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-27-2026
 */

public final class LogoutSpotRenderer implements Render3D {

    public static final Color defaultTagTextColor = Color.RED;
    public static final Color defaultBackgroundColor = new Color(255, 255, 0, 150);
    public static final Color defaultBoxColor = Color.YELLOW;
    public static final int defaultBoxFillAlpha = 100;
    public static final int defaultBoxLineWidth = 2;

    private final LogoutSpot instance;

    private final double width = 1.0D;
    private final double height = 2.0D;

    private AxisAlignedBB renderPos = null;

    public LogoutSpotRenderer(LogoutSpot instance) {
        this.instance = instance;
    }

    private AxisAlignedBB getRenderPos() {
        if (this.renderPos == null) {
            int x = this.instance.coords.x;
            int y = this.instance.coords.y;
            int z = this.instance.coords.z;

            double minX = x - (this.width / 2);
            double minZ = z - (this.width / 2);

            double maxX = x + (this.width / 2);
            double maxY = y + this.height;
            double maxZ = z + (this.width / 2);

            this.renderPos = AxisAlignedBB.getBoundingBox(minX, y, minZ, maxX, maxY, maxZ);
        }

        return this.renderPos;
    }

    public void render(boolean box, boolean title, boolean coords, boolean distance, boolean time, boolean background, double tagScale, int tagDistanceMultiplier, Color tagTextColor, Color tagBackgroundColor, RenderType renderType, int lineWidth, Color boxColor, int boxFillAlpha) {
        if (box) {
            AxisAlignedBB bb = this.getRenderPos();

            double rX = bb.minX - RenderManager.renderPosX;
            double rY = bb.minY - RenderManager.renderPosY;
            double rZ = bb.minZ - RenderManager.renderPosZ;

            double mX = bb.maxX - RenderManager.renderPosX;
            double mY = bb.maxY - RenderManager.renderPosY;
            double mZ = bb.maxZ - RenderManager.renderPosZ;

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            switch (renderType) {
                case Outline:
                    this.drawOutlineBox(rX, rY, rZ, mX, mY, mZ, lineWidth, boxColor);
                    break;
                case Fill:
                    this.drawFillBox(rX, rY, rZ, mX, mY, mZ, new Color(boxColor.getRed(), boxColor.getGreen(), boxColor.getBlue(), boxFillAlpha));
                case Both:
                    this.drawFillBox(rX, rY, rZ, mX, mY, mZ, new Color(boxColor.getRed(), boxColor.getGreen(), boxColor.getBlue(), boxFillAlpha));
                    this.drawOutlineBox(rX, rY, rZ, mX, mY, mZ, lineWidth, boxColor);
                    break;
            }

            RenderTask.getInstance().render();
            RenderTask.getInstance().clear();

            GL11.glDisable(GL11.GL_BLEND);
        }

        double tagMaxX = this.instance.coords.x;
        double tagMaxY = this.instance.coords.y + this.height + 0.5;
        double tagMaxZ = this.instance.coords.z;

        double distanceSq = this.getPlayer().getDistanceSq(tagMaxX, tagMaxY, tagMaxZ);

        double scaling = -Math.max(0.0416667D * tagScale, 0.0416667D * tagScale * Math.min(tagDistanceMultiplier, distanceSq / 512));

        GL11.glPushMatrix();

        GL11.glTranslated(tagMaxX - RenderManager.renderPosX, tagMaxY - RenderManager.renderPosY, tagMaxZ - RenderManager.renderPosZ);

        GL11.glRotated(-RenderManager.instance.playerViewY, 0.0D, 1.0D, 0.0D);
        GL11.glRotated(RenderManager.instance.playerViewX, 1.0D, 0.0D, 0.0D);

        GL11.glScaled(scaling, scaling, scaling);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);

        String username = this.instance.username;
        boolean isFriend = this.getFriendManager().isFriend(username);

        int timeMS = Math.toIntExact(this.instance.getDiffTimeMS());

        boolean longerThan1Sec = false;
        double timeS = 0;

        if (timeMS >= 1000) {
            longerThan1Sec = true;
            timeS = (double) timeMS / 1000;
        }

        String timeString = time ? " (" + (longerThan1Sec ? (timeS >= 100 ? "100+" : String.format("%.1f", timeS)) + "s" : timeMS + "ms") + ")" : "";

        String toRender = (title ? "LogoutSpot: " : "") + (isFriend ? "(F) " : "") + username + (coords ? " (" + this.instance.coords.x + ", " + this.instance.coords.y + ", " + this.instance.coords.z + ")" : "") + (distance ? " (" + this.instance.getDistance() + ")" : "") + timeString;
        int toRenderWidth = this.getFontRenderer().getStringWidth(toRender);

        if (background) {
            this.drawRectangle(-toRenderWidth / 2 - 1, -1, toRenderWidth / 2 + 1, 9, tagBackgroundColor.getRGB());
        }

        this.drawString(toRender, -toRenderWidth / 2, 0, tagTextColor.getRGB());

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);

        GL11.glPopMatrix();
    }

    public enum RenderType {
        Outline,
        Fill,
        Both
    }
}
