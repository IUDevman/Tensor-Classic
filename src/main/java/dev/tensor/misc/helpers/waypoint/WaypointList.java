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
import java.util.stream.Collectors;

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

    public ArrayList<Waypoint> getWaypointsAll() {
        return this.waypoints;
    }

    public ArrayList<Waypoint> getWaypointsFor(boolean overworld, String server) {
        return this.getWaypointsAll().stream().filter(waypoint -> waypoint.isOverworld() == overworld).filter(waypoint -> waypoint.getServer().equalsIgnoreCase(server)).collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<Waypoint> getSortedWaypoints() {
        return this.sortedWaypoints.stream().sorted(Comparator.comparing(waypoint -> this.getPlayer().getDistanceSq(((Waypoint) waypoint).getX(), ((Waypoint) waypoint).getY(), ((Waypoint) waypoint).getZ())).reversed()).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Waypoint> getSortedWaypointsFor(boolean overworld, String server) {
        return this.getSortedWaypoints().stream().filter(waypoint -> waypoint.isOverworld() == overworld).filter(waypoint -> waypoint.getServer().equalsIgnoreCase(server)).collect(Collectors.toCollection(ArrayList::new));
    }

    public void addToWaypointList(Waypoint waypoint) {
        this.waypoints.add(waypoint);
        this.sortedWaypoints.add(waypoint);
    }

    public void removeFromWaypointsList(Waypoint waypoint) {
        this.waypoints.remove(waypoint);
        this.sortedWaypoints.remove(waypoint);
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
                boolean overworld = jsonObject.get("Overworld").getAsBoolean();
                String server = jsonObject.get("Server").getAsString();

                Waypoint waypoint = new Waypoint(name, x, y, z, overworld, server);
                waypoint.setEnabled(enabled);

                this.addToWaypointList(waypoint);
            }
        });
    }

    public void saveWaypoints(String unique) throws Exception {
        Path path = Paths.get(configPath.replace("{X}", unique));
        this.getConfigManager().registerFile(path);

        JsonArray jsonArray = new JsonArray();

        this.getWaypointsAll().forEach(waypoint -> {

            JsonObject jsonObject = new JsonObject();

            jsonObject.add("Name", new JsonPrimitive(waypoint.getName()));
            jsonObject.add("Enabled", new JsonPrimitive(waypoint.isEnabled()));
            jsonObject.add("X", new JsonPrimitive(waypoint.getX()));
            jsonObject.add("Y", new JsonPrimitive(waypoint.getY()));
            jsonObject.add("Z", new JsonPrimitive(waypoint.getZ()));
            jsonObject.add("Overworld", new JsonPrimitive(waypoint.isOverworld()));
            jsonObject.add("Server", new JsonPrimitive(waypoint.getServer()));

            jsonArray.add(jsonObject);
        });

        this.getConfigManager().createAndWrite(path, jsonArray);
    }

    public static ArrayList<Waypoint> EMPTY() {
        return new ArrayList<>();
    }
}
