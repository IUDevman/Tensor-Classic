package dev.tensor.feature.commands;

import dev.tensor.misc.helpers.waypoint.Waypoint;
import dev.tensor.misc.helpers.waypoint.WaypointList;
import dev.tensor.misc.imp.command.Command;

import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 04-08-2026
 */

public final class Waypoints implements Command {

    @Override
    public String getName() {
        return "Waypoints";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                this.getName(),
                "Waypoint",
                "Marker",
                "Markers"
        };
    }

    @Override
    public String getRegex() {
        return "{name} {list/toggle/add/remove} {name$or$name_this/x_y_z}";
    }

    @Override
    public String getDescription() {
        return "Toggle, add, remove, or list waypoints to render with Waypoints.";
    }

    @Override
    public void onCommand(String[] ARGS) {

        if (ARGS == null || ARGS.length == 0) {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify waypoint list (no arguments inputted)!");
            return;
        }

        String firstEntry = ARGS[0];

        if (firstEntry == null || firstEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify waypoint list (no arguments inputted)!");
            return;
        }

        if (!firstEntry.equalsIgnoreCase("list") && !firstEntry.equalsIgnoreCase("add") && !firstEntry.equalsIgnoreCase("remove") && !firstEntry.equalsIgnoreCase("toggle")) {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to modify waypoint list (unknown argument inputted)!");
            return;
        }

        WaypointList waypointList = this.getHackManager().getHack(dev.tensor.feature.hacks.Waypoints.class).waypointList.getValue();

        if (firstEntry.equalsIgnoreCase("list")) {
            ArrayList<Waypoint> waypoints = waypointList.getWaypointsFor(!this.getWorld().worldProvider.isHellWorld, this.isMultiplayer() ? this.getServerIP() : "Singleplayer");

            String MESSAGE_PRE = this.getName() + " (" + (this.getWorld().worldProvider.isHellWorld ? this.getColor(ChatColor.RED) + "Nether" : this.getColor(ChatColor.GREEN) + "Overworld")
                    + this.getColor(ChatColor.DEFAULT) + "/" + (this.isMultiplayer() ? this.getServerIP() : "Singleplayer") + ") ("
                    + this.getColor(ChatColor.AQUA) + waypoints.size() + this.getColor(ChatColor.DEFAULT) + "): ";

            ArrayList<String> WAYPOINTS = new ArrayList<>();

            waypoints.forEach(waypoint -> WAYPOINTS.add((waypoint.isEnabled() ? this.getColor(ChatColor.GREEN) : this.getColor(ChatColor.RED)) + waypoint.getName()
                    + this.getColor(ChatColor.DEFAULT) + " (" + this.getColor(ChatColor.YELLOW) + waypoint.getX() + this.getColor(ChatColor.DEFAULT)
                    + ", " + this.getColor(ChatColor.YELLOW) + waypoint.getY() + this.getColor(ChatColor.DEFAULT) + ", "
                    + this.getColor(ChatColor.YELLOW) + waypoint.getZ() + this.getColor(ChatColor.DEFAULT) + ")"));

            this.sendClientMessageWithHeader(MESSAGE_PRE + WAYPOINTS);
            return;
        }

        boolean shouldAdd = firstEntry.equalsIgnoreCase("add");

        if (ARGS.length == 1) {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to toggle/add/remove waypoint (no name inputted)!");
            return;
        }

        String secondEntry = ARGS[1];

        if (secondEntry == null || secondEntry.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to toggle/add/remove waypoint (no name inputted)!");
            return;
        }

        String[] secondEntryArgs = secondEntry.split("_");

        if (secondEntryArgs.length < 1) {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to toggle/add/remove waypoint (no name inputted)!");
            return;
        }

        String name = secondEntryArgs[0];

        if (name == null || name.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to toggle/add/remove waypoint (no name inputted)!");
            return;
        }

        dev.tensor.misc.helpers.waypoint.Waypoint waypoint = waypointList.getWaypointsFor(!this.getWorld().worldProvider.isHellWorld, this.isMultiplayer() ? this.getServerIP() : "Singleplayer").stream().filter(waypoint1 -> waypoint1.getName().equalsIgnoreCase(name)).findFirst().orElse(null);

        if (waypoint == null && !shouldAdd) {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to find waypoint '" + this.getColor(ChatColor.AQUA) + name + this.getColor(ChatColor.RED) + "' from waypoint list!");
            return;
        }

        if (waypoint != null && firstEntry.equalsIgnoreCase("toggle")) {
            waypoint.toggle();
            this.sendClientMessageWithHeader(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Waypoint '" + this.getColor(ChatColor.AQUA) + waypoint.getName() + this.getColor(ChatColor.DEFAULT) + "' (" + (this.getWorld().worldProvider.isHellWorld ? this.getColor(ChatColor.RED) + "Nether" : this.getColor(ChatColor.GREEN) + "Overworld") + this.getColor(ChatColor.DEFAULT) + "/" + (this.isMultiplayer() ? this.getServerIP() : "Singleplayer") + ") with coordinates (" + this.getColor(ChatColor.YELLOW) + waypoint.getX() + this.getColor(ChatColor.DEFAULT) + ", " + this.getColor(ChatColor.YELLOW) + waypoint.getY() + this.getColor(ChatColor.DEFAULT) + ", " + this.getColor(ChatColor.YELLOW) + waypoint.getZ() + this.getColor(ChatColor.DEFAULT) + ") toggled (" + (waypoint.isEnabled() ? this.getColor(ChatColor.GREEN) + "Enabled" : this.getColor(ChatColor.RED) + "Disabled") + this.getColor(ChatColor.DEFAULT) + ")!");
            return;
        }

        if (!shouldAdd) {
            waypointList.removeFromWaypointsList(waypoint);
            this.sendClientMessageWithHeader(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Waypoint '" + this.getColor(ChatColor.AQUA) + waypoint.getName() + this.getColor(ChatColor.DEFAULT) + "' (" + (this.getWorld().worldProvider.isHellWorld ? this.getColor(ChatColor.RED) + "Nether" : this.getColor(ChatColor.GREEN) + "Overworld") + this.getColor(ChatColor.DEFAULT) + "/" + (this.isMultiplayer() ? this.getServerIP() : "Singleplayer") + ") with coordinates (" + this.getColor(ChatColor.YELLOW) + waypoint.getX() + this.getColor(ChatColor.DEFAULT) + ", " + this.getColor(ChatColor.YELLOW) + waypoint.getY() + this.getColor(ChatColor.DEFAULT) + ", " + this.getColor(ChatColor.YELLOW) + waypoint.getZ() + this.getColor(ChatColor.DEFAULT) + ") removed from waypoints list!");
            return;
        }

        if (waypoint != null) {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to add waypoint '" + this.getColor(ChatColor.AQUA) + name + this.getColor(ChatColor.RED) + "' to waypoint list (name already used)!");
            return;
        }

        if (secondEntryArgs.length < 2) {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to add waypoint (no 'this' or coordinates inputted)!");
            return;
        }

        String thisOrX = secondEntryArgs[1];

        if (thisOrX == null || thisOrX.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to add waypoint '" + this.getColor(ChatColor.AQUA) + name + this.getColor(ChatColor.RED) + "' to waypoint list (no 'this' or coordinates inputted)!");
            return;
        }

        if (thisOrX.equalsIgnoreCase("this")) {
            try {
                int x = (int) this.getPlayer().posX;
                int y = (int) this.getPlayer().posY;
                int z = (int) this.getPlayer().posZ;

                dev.tensor.misc.helpers.waypoint.Waypoint waypointAdd = new dev.tensor.misc.helpers.waypoint.Waypoint(name, x, y, z, !this.getWorld().worldProvider.isHellWorld, this.isMultiplayer() ? this.getServerIP() : "Singleplayer");
                waypointAdd.setEnabled(true);
                waypointList.addToWaypointList(waypointAdd);
                this.sendClientMessageWithHeader(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Waypoint '" + this.getColor(ChatColor.AQUA) + waypointAdd.getName() + this.getColor(ChatColor.DEFAULT) + "' (" + (this.getWorld().worldProvider.isHellWorld ? this.getColor(ChatColor.RED) + "Nether" : this.getColor(ChatColor.GREEN) + "Overworld") + this.getColor(ChatColor.DEFAULT) + "/" + (this.isMultiplayer() ? this.getServerIP() : "Singleplayer") + ") with coordinates (" + this.getColor(ChatColor.YELLOW) + waypointAdd.getX() + this.getColor(ChatColor.DEFAULT) + ", " + this.getColor(ChatColor.YELLOW) + waypointAdd.getY() + this.getColor(ChatColor.DEFAULT) + ", " + this.getColor(ChatColor.YELLOW) + waypointAdd.getZ() + this.getColor(ChatColor.DEFAULT) + ") added to waypoints list!");

            } catch (Exception ignored) {
                this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to add waypoint '" + this.getColor(ChatColor.AQUA) + name + this.getColor(ChatColor.RED) + "' to waypoint list (unknown error occurred)!");
            }

            return;
        }

        if (secondEntryArgs.length < 4) {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to add waypoint (not all coordinate values inputted)!");
            return;
        }

        String Y = secondEntryArgs[2];
        String Z = secondEntryArgs[3];

        if (Y == null || Y.equalsIgnoreCase(" ") || Z == null || Z.equalsIgnoreCase(" ")) {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to add waypoint '" + this.getColor(ChatColor.AQUA) + name + this.getColor(ChatColor.RED) + "' to waypoint list (invalid coordinates inputted)!");
            return;
        }

        try {
            dev.tensor.misc.helpers.waypoint.Waypoint waypointAdd = new dev.tensor.misc.helpers.waypoint.Waypoint(name, Integer.parseInt(thisOrX), Integer.parseInt(Y), Integer.parseInt(Z), !this.getWorld().worldProvider.isHellWorld, this.isMultiplayer() ? this.getServerIP() : "Singleplayer");
            waypointAdd.setEnabled(true);
            waypointList.addToWaypointList(waypointAdd);
            this.sendClientMessageWithHeader(this.getColor(ChatColor.DEFAULT) + this.getName() + ": Waypoint '" + this.getColor(ChatColor.AQUA) + waypointAdd.getName() + this.getColor(ChatColor.DEFAULT) + "' (" + (this.getWorld().worldProvider.isHellWorld ? this.getColor(ChatColor.RED) + "Nether" : this.getColor(ChatColor.GREEN) + "Overworld") + this.getColor(ChatColor.DEFAULT) + "/" + (this.isMultiplayer() ? this.getServerIP() : "Singleplayer") + ") with coordinates (" + this.getColor(ChatColor.YELLOW) + waypointAdd.getX() + this.getColor(ChatColor.DEFAULT) + ", " + this.getColor(ChatColor.YELLOW) + waypointAdd.getY() + this.getColor(ChatColor.DEFAULT) + ", " + this.getColor(ChatColor.YELLOW) + waypointAdd.getZ() + this.getColor(ChatColor.DEFAULT) + ") added to waypoints list!");

        } catch (Exception ignored) {
            this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + this.getName() + ": Failed to add waypoint '" + this.getColor(ChatColor.AQUA) + name + this.getColor(ChatColor.RED) + "' to waypoint list (unknown error occurred)!");
        }
    }
}
