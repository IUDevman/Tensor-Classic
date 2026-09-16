package dev.tensor.misc.helpers.block;

import dev.tensor.misc.imp.Globals;
import dev.tensor.misc.imp.render.Render3D;
import net.minecraft.src.RenderManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Hoosiers
 * @since 06-12-2026
 */

public final class PortalHunter implements Globals {

    private final ArrayList<PortalGroup> portalGroups = new ArrayList<>();

    public boolean isEmpty() {
        return this.portalGroups.isEmpty();
    }

    public void addPortalCoords(BlockCoords portalCoords) {
        AtomicReference<PortalGroup> foundGroup = new AtomicReference<>(null);

        this.portalGroups.forEach(portalGroup -> {
            if (foundGroup.get() != null) {
                return;
            }

            if (portalGroup.isWithinGroupIncorporate(portalCoords)) {
                foundGroup.set(portalGroup);
            }
        });

        if (foundGroup.get() == null) {
            this.portalGroups.add(new PortalGroup(portalCoords));
        }
    }

    public void renderAll(Color color, int alpha, int lineWidth) {
        this.portalGroups.forEach(portalGroup -> portalGroup.render(color, alpha, lineWidth));
    }

    private static class PortalGroup implements Render3D {

        private int minX;
        private int minY;
        private int minZ;

        private int maxX;
        private int maxY;
        private int maxZ;

        public PortalGroup(BlockCoords blockCoords) {
            this.minX = blockCoords.x;
            this.minY = blockCoords.y;
            this.minZ = blockCoords.z;

            this.maxX = this.minX + 1;
            this.maxY = this.minY + 1;
            this.maxZ = this.minZ + 1;
        }

        public boolean isWithinGroupIncorporate(BlockCoords portalCoords) {
            boolean touchingX = (portalCoords.x >= this.minX - 1) && (portalCoords.x <= this.maxX);
            boolean touchingY = (portalCoords.y >= this.minY - 1) && (portalCoords.y <= this.maxY);
            boolean touchingZ = (portalCoords.z >= this.minZ - 1) && (portalCoords.z <= this.maxZ);

            if (touchingX && touchingY && touchingZ) {
                this.minX = Math.min(this.minX, portalCoords.x);
                this.minY = Math.min(this.minY, portalCoords.y);
                this.minZ = Math.min(this.minZ, portalCoords.z);

                this.maxX = Math.max(this.maxX, portalCoords.x + 1);
                this.maxY = Math.max(this.maxY, portalCoords.y + 1);
                this.maxZ = Math.max(this.maxZ, portalCoords.z + 1);

                return true;
            }

            return false;
        }

        public void render(Color color, int alpha, int lineWidth) {
            double centerX = this.minX + ((double) (this.maxX - this.minX) / 2);
            double centerY = this.minY + ((double) (this.maxY - this.minY) / 2);
            double centerZ = this.minZ + ((double) (this.maxZ - this.minZ) / 2);

            this.drawTracer(centerX - RenderManager.renderPosX, centerY - RenderManager.renderPosY, centerZ - RenderManager.renderPosZ, lineWidth, color, alpha);
        }
    }
}
