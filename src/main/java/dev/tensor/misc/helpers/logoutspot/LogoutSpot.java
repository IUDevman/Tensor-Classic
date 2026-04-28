package dev.tensor.misc.helpers.logoutspot;

import dev.tensor.feature.hacks.HephFreecam;
import dev.tensor.misc.helpers.block.BlockPos;
import dev.tensor.misc.imp.Globals;

/**
 * @author Hoosiers
 * @since 04-26-2026
 */

public final class LogoutSpot implements Globals {

    public final String username;
    public final BlockPos coords;
    private final long timeLogged;

    private final LogoutSpotRenderer render;

    public LogoutSpot(String username, BlockPos coords, long timeLogged) {
        this.username = username;
        this.coords = coords;
        this.timeLogged = timeLogged;

        this.render = new LogoutSpotRenderer(this);
    }

    public LogoutSpotRenderer getRender() {
        return this.render;
    }

    public int getDistance() {
        double posX = this.getPlayer().posX;
        double posY = this.getPlayer().posY;
        double posZ = this.getPlayer().posZ;

        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

        if (freecam != null && freecam.isEnabled()) {
            posX = freecam.oldX;
            posY = freecam.oldY;
            posZ = freecam.oldZ;
        }

        return (int) this.getDistance(coords.x - posX, coords.y - posY, coords.z - posZ);
    }

    public long getDiffTimeMS() {
        return System.currentTimeMillis() - timeLogged;
    }
}
