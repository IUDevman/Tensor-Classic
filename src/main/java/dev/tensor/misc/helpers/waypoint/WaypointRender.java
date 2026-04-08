package dev.tensor.misc.helpers.waypoint;

import dev.tensor.misc.imp.render.Render3D;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.RenderManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author Hoosiers
 * @since 04-07-2026
 */

public final class WaypointRender implements Render3D {

    public static final Color defaultTagTextColor = Color.WHITE;
    public static final Color defaultBackgroundColor = new Color(255, 0, 0, 150);
    public static final Color defaultBoxColor = Color.RED;
    public static final int defaultBoxFillAlpha = 100;
    public static final int defaultBoxLineWidth = 2;

    private final Waypoint instance;

    private final double width = 1.0D;
    private final double height = 6.0D;

    private AxisAlignedBB renderPos = null;

    public WaypointRender(Waypoint instance) {
        this.instance = instance;
    }

    public AxisAlignedBB getRenderPos() {

        if (this.renderPos == null) {
            int x = this.instance.getX();
            int y = this.instance.getY();
            int z = this.instance.getZ();

            double minX = x - (this.width / 2);
            double minY = y - (this.height / 2);
            double minZ = z - (this.width / 2);

            double maxX = x + (this.width / 2);
            double maxY = y + (this.height / 2);
            double maxZ = z + (this.width / 2);

            this.renderPos = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        }

        return this.renderPos;
    }

    public void render(boolean title, boolean showCoords, boolean tagBackground, double tagScale, int tagDistanceMultiplier, Color tagTextColor, Color tagBackgroundColor, boolean box, boolean monolith, boolean onlyCloseMonoliths, RenderType renderType, int lineWidth, Color boxColor, int boxFillAlpha) {

        double originalX = this.instance.getX();
        double originalY = this.instance.getY() + (this.height / 2) + 0.7;
        double originalZ = this.instance.getZ();

        if (monolith) {
            originalY = this.instance.getY();
        }

        double maxedX = this.setMax(originalX, this.getPlayer().posX, 256);
        double maxedY = this.setMax(originalY, this.getPlayer().posY, 256);
        double maxedZ = this.setMax(originalZ, this.getPlayer().posZ, 256);

        boolean changedX = !(onlyCloseMonoliths ? originalX == maxedX : monolith) && originalX != maxedX;
        boolean changedY = !monolith && originalY != maxedY;
        boolean changedZ = !(onlyCloseMonoliths ? originalZ == maxedZ : monolith) && originalZ != maxedZ;

        if ((monolith || box) && !changedX && !changedY && !changedZ) {
            double rMinX = (monolith ? maxedX - (this.width / 2) / 2 : this.getRenderPos().minX) - RenderManager.renderPosX;
            double rMinY = (monolith ? 0 : this.getRenderPos().minY) - RenderManager.renderPosY;
            double rMinZ = (monolith ? maxedZ - (this.width / 2) / 2 : this.getRenderPos().minZ) - RenderManager.renderPosZ;

            double rMaxX = (monolith ? maxedX + (this.width / 2) / 2 : this.getRenderPos().maxX) - RenderManager.renderPosX;
            double rMaxY = (monolith ? 192 : this.getRenderPos().maxY) - RenderManager.renderPosY;
            double rMaxZ = (monolith ? maxedZ + (this.width / 2) / 2 : this.getRenderPos().maxZ) - RenderManager.renderPosZ;

            switch (renderType) {
                case Outline:
                    this.drawOutlineBox(rMinX, rMinY, rMinZ, rMaxX, rMaxY, rMaxZ, lineWidth, boxColor);
                    break;
                case Fill:
                    this.drawFillBox(rMinX, rMinY, rMinZ, rMaxX, rMaxY, rMaxZ, new Color(boxColor.getRed(), boxColor.getGreen(), boxColor.getBlue(), boxFillAlpha));
                case Both:
                    this.drawFillBox(rMinX, rMinY, rMinZ, rMaxX, rMaxY, rMaxZ, new Color(boxColor.getRed(), boxColor.getGreen(), boxColor.getBlue(), boxFillAlpha));
                    this.drawOutlineBox(rMinX, rMinY, rMinZ, rMaxX, rMaxY, rMaxZ, lineWidth, boxColor);
                    break;
            }
        }

        double distance = Math.sqrt(this.getPlayer().getDistanceSq(maxedX, maxedY, maxedZ));
        double scaling = -Math.max(0.0833334D * tagScale, 0.0833334D * tagScale * Math.min(tagDistanceMultiplier, (distance / 45.2548D)));

        GL11.glPushMatrix();

        GL11.glTranslated(maxedX - RenderManager.renderPosX, maxedY - RenderManager.renderPosY, maxedZ - RenderManager.renderPosZ);

        GL11.glRotated(-RenderManager.instance.playerViewY, 0.0D, 1.0D, 0.0D);
        GL11.glRotated(RenderManager.instance.playerViewX, 1.0D, 0.0D, 0.0D);

        GL11.glScaled(scaling, scaling, scaling);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);

        String toRender = (title ? "Waypoint: " : "") + this.instance.getName() + (showCoords ? " (" + this.instance.getX() + ", " + this.instance.getY() + ", " + this.instance.getZ() + ")" : "");
        int toRenderWidth = this.getFontRenderer().getStringWidth(toRender);

        if (tagBackground) {
            this.drawRectangle(-toRenderWidth / 2 - 1, -1, toRenderWidth / 2 + 1, 9, tagBackgroundColor.getRGB());
        }

        this.drawString(toRender, -toRenderWidth / 2, 0, tagTextColor.getRGB());

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);

        GL11.glPopMatrix();
    }

    private double setMax(double value, double reference, double max) {

        if (value - reference > max) {
            value = reference + max;
        } else if (value - reference < -max) {
            value = reference - max;
        }

        return value;
    }

    public enum RenderType {
        Outline,
        Fill,
        Both
    }
}
