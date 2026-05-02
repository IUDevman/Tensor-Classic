package dev.tensor.feature.hacks;

import dev.tensor.misc.helpers.waypoint.Waypoint;
import dev.tensor.misc.helpers.waypoint.WaypointList;
import dev.tensor.misc.helpers.waypoint.WaypointRender;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.*;

import java.util.ArrayList;


/**
 * @author Hoosiers
 * @since 04-07-2026
 */

@Hack.Info(name = "Waypoints", category = Category.Render)
public final class Waypoints extends Render3DHack {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.Waypoints;
    }

    public final WaypointListSetting waypointList = new WaypointListSetting("List", new WaypointList(WaypointList.EMPTY()), "");
    public final BooleanSetting title = new BooleanSetting("Title", true);
    public final BooleanSetting showCoords = new BooleanSetting("Show Coordinates", true);
    public final DoubleSetting tagScale = new DoubleSetting("Tag Scale", 1.00, 0.10, 2.00, 2);
    public final IntegerSetting tagDistanceMultiplier = new IntegerSetting("Tag Distance Multiplier", 8, 2, 10);
    public final BooleanSetting tagBackground = new BooleanSetting("Tag Background", false);
    public final ColorSettingNoAlpha tagTextColor = new ColorSettingNoAlpha("Text Color", WaypointRender.defaultTagTextColor);
    public final ColorSetting tagBackgroundColor = new ColorSetting("Background Color", WaypointRender.defaultBackgroundColor);
    public final EnumSetting renderMode = new EnumSetting("Render Mode", Mode.Monolith);
    public final BooleanSetting onlyCloseMonoliths = new BooleanSetting("Only Close Monoliths", false);
    public final EnumSetting renderType = new EnumSetting("Render Type", Type.Both);
    public final IntegerSetting boxLineWidth = new IntegerSetting("Box Line Width", WaypointRender.defaultBoxLineWidth, 1, 5);
    public final IntegerSetting boxFillAlpha = new IntegerSetting("Box Fill Alpha", WaypointRender.defaultBoxFillAlpha, 0, 255);
    public final ColorSetting boxColor = new ColorSetting("Box Color", WaypointRender.defaultBoxColor);

    public final ArrayList<Waypoint> waypointsToRender = new ArrayList<>();

    @Override
    public void onDisable(boolean isNull) {
        this.waypointsToRender.clear();
    }

    @Override
    public void onClientTick() {
        this.waypointsToRender.clear();
        this.waypointsToRender.addAll(this.waypointList.getValue().getSortedWaypointsFor(!this.getWorld().worldProvider.isHellWorld, this.isMultiplayer() ? this.getServerIP() : "Singleplayer"));
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        this.waypointsToRender.forEach(waypoint -> {

            if (waypoint.isEnabled()) {
                WaypointRender.RenderType type = null;

                switch ((Type) this.renderType.getValue()) {
                    case Fill:
                        type = WaypointRender.RenderType.Fill;
                        break;
                    case Outline:
                        type = WaypointRender.RenderType.Outline;
                        break;
                    case Both:
                        type = WaypointRender.RenderType.Both;
                        break;
                }

                boolean renderBox = false;
                boolean monolith = false;

                switch ((Mode) this.renderMode.getValue()) {
                    case None:
                        renderBox = false;
                        monolith = false;
                        break;
                    case Monolith:
                        renderBox = false;
                        monolith = true;
                        break;
                    case Prism:
                        renderBox = true;
                        monolith = false;
                        break;
                }

                waypoint.getRender().render(
                        this.title.getValue(),
                        this.showCoords.getValue(),
                        this.tagBackground.getValue(),
                        this.tagScale.getValue(),
                        this.tagDistanceMultiplier.getValue(),
                        this.tagTextColor.getValue(),
                        this.tagBackgroundColor.getValue(),
                        renderBox,
                        monolith,
                        onlyCloseMonoliths.getValue(),
                        type,
                        this.boxLineWidth.getValue(),
                        this.boxColor.getValue(),
                        this.boxFillAlpha.getValue());
            }
        });
    }

    private enum Mode {
        Prism,
        Monolith,
        None
    }

    private enum Type {
        Outline,
        Fill,
        Both,
    }
}
