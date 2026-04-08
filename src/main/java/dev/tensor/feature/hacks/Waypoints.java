package dev.tensor.feature.hacks;

import dev.tensor.misc.helpers.waypoint.Waypoint;
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
    public int getRenderOrder() {
        return RenderOrder.WAYPOINTS;
    }

    //todo: waypoints list setting; NOTE FOR FUTURE ME: Do not have the list reset on hack reset.
    public final BooleanSetting title = new BooleanSetting("Title", true);
    public final BooleanSetting showCoords = new BooleanSetting("Show Coordinates", true);
    public final DoubleSetting tagScale = new DoubleSetting("Tag Scale", 1.00, 0.10, 2.00, 2);
    public final IntegerSetting tagDistanceMultiplier = new IntegerSetting("Tag Distance Multiplier", 6, 2, 10);
    public final BooleanSetting tagBackground = new BooleanSetting("Tag Background", false);
    public final ColorSettingNoAlpha tagTextColor = new ColorSettingNoAlpha("Tag Text Color", WaypointRender.defaultTagTextColor);
    public final ColorSetting tagBackgroundColor = new ColorSetting("Tag Background Color", WaypointRender.defaultBackgroundColor);
    public final EnumSetting renderMode = new EnumSetting("Render Mode", Mode.Monolith);
    public final BooleanSetting onlyCloseMonoliths = new BooleanSetting("Only Close Monoliths", false);
    public final EnumSetting renderType = new EnumSetting("Render Type", Type.Both);
    public final IntegerSetting boxLineWidth = new IntegerSetting("Box Line Width", WaypointRender.defaultBoxLineWidth, 1, 5);
    public final IntegerSetting boxFillAlpha = new IntegerSetting("Box Fill Alpha", WaypointRender.defaultBoxFillAlpha, 0, 255);
    public final ColorSetting boxColor = new ColorSetting("Box Color", WaypointRender.defaultBoxColor);

    private final ArrayList<Waypoint> waypoints = new ArrayList<>();

    private boolean sorted = false;

    @Override
    public void onEnable(boolean isNull) {

        //temporary for testing
        this.waypoints.add(new Waypoint("Test", 1000, 100, 200));
        this.waypoints.add(new Waypoint("Test1", 0, 100, 0));
        this.waypoints.add(new Waypoint("Test2", 500, 100, 200));
        this.waypoints.add(new Waypoint("Test3", 30, 30, 60));

        this.waypoints.forEach(waypoint -> waypoint.setEnabled(true));

        if (!isNull) {
            this.sortWaypoints();
        }
    }

    @Override
    public void onDisable(boolean isNull) {
        this.sorted = false;
    }

    @Override
    public void onRender3D(float renderPartialTicks) {
        if (!this.sorted) {
            this.sortWaypoints();
        }

        //todo: sort waypoints by distance (NOT IN THIS METHOD DO IT WHEN LOADING)
        //Should render farthest first?
        this.waypoints.forEach(waypoint -> {

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

    private void sortWaypoints() {
        //todo
        this.sorted = true;
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
