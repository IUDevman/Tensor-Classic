package dev.tensor.misc.helpers.waypoint;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.imp.Globals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Hoosiers
 * @since 04-08-2026
 */

public final class WaypointList implements Globals {

    private final String configPath = this.getConfigManager().getConfigPath() + "Waypoints{X}.json";

    private final ArrayList<Waypoint> waypoints;
    private final ArrayList<Waypoint> sortedWaypoints;

    public WaypointList(ArrayList<Waypoint> waypoints) {
        this.waypoints = new ArrayList<>(waypoints);
        this.sortedWaypoints = new ArrayList<>(waypoints);
    }

    public ArrayList<Waypoint> getWaypoints() {
        return this.waypoints;
    }

    public ArrayList<Waypoint> getSortedWaypoints() {
        this.sortWaypoints();
        return this.sortedWaypoints;
    }

    public void addToWaypointList(Waypoint waypoint) {
        this.waypoints.add(waypoint);
        this.sortedWaypoints.add(waypoint);
    }

    public void removeFromWaypointsList(Waypoint waypoint) {
        this.waypoints.remove(waypoint);
        this.sortedWaypoints.remove(waypoint);
    }

    public void sortWaypoints() {
        if (this.failsNullCheck()) {
            return;
        }

        this.sortedWaypoints.sort(Comparator.comparing(waypoint -> this.getPlayer().getDistanceSq(((Waypoint) waypoint).getX(), ((Waypoint) waypoint).getY(), ((Waypoint) waypoint).getZ())).reversed());
    }

    public void loadWaypoints(String unique) throws Exception {
        Path path = Paths.get(configPath.replace("{X}", unique));

        if (!Files.exists(path)) {
            this.getTensorLogger().info("Failed to load waypoints!");
            return;
        }

        JsonArray jsonArray = this.getConfigManager().getJsonArrayFromPath(path);

        if (jsonArray == null || jsonArray.isEmpty()) {
            return;
        }

        jsonArray.forEach(jsonElement -> {

            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject != null) {

                String name = jsonObject.get("Name").getAsString();
                boolean enabled = jsonObject.get("Enabled").getAsBoolean();
                int x = jsonObject.get("X").getAsInt();
                int y = jsonObject.get("Y").getAsInt();
                int z = jsonObject.get("Z").getAsInt();

                Waypoint waypoint = new Waypoint(name, x, y, z);
                waypoint.setEnabled(enabled);

                this.addToWaypointList(waypoint);
            }
        });
    }

    public void saveWaypoints(String unique) throws Exception {
        Path path = Paths.get(configPath.replace("{X}", unique));
        this.getConfigManager().registerFile(path);

        JsonArray jsonArray = new JsonArray();

        this.getWaypoints().forEach(waypoint -> {

            JsonObject jsonObject = new JsonObject();

            jsonObject.add("Name", new JsonPrimitive(waypoint.getName()));
            jsonObject.add("Enabled", new JsonPrimitive(waypoint.isEnabled()));
            jsonObject.add("X", new JsonPrimitive(waypoint.getX()));
            jsonObject.add("Y", new JsonPrimitive(waypoint.getY()));
            jsonObject.add("Z", new JsonPrimitive(waypoint.getZ()));

            jsonArray.add(jsonObject);
        });

        this.getConfigManager().createAndWrite(path, jsonArray);
    }

    public static ArrayList<Waypoint> EMPTY() {
        return new ArrayList<>();
    }
}
