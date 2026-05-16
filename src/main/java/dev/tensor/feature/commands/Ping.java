package dev.tensor.feature.commands;

import dev.tensor.backend.mixins.accessors.NetClientHandlerAccessor;
import dev.tensor.backend.mixins.accessors.NetworkManagerAccessor;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.imp.command.SimpleCallableCommand;

import java.net.InetAddress;

/**
 * @author Hoosiers
 * @since 04-12-2026
 */

public final class Ping extends SimpleCallableCommand {

    @Override
    public String getName() {
        return "Ping";
    }

    @Override
    public String[] getAcceptedNames() {
        return new String[]{
                this.getName()
        };
    }

    @Override
    public String getDescription() {
        return "Determines the speed of your connection to the server.";
    }

    private boolean isSearching = false;

    @Override
    public void onCommandTriggered() {
        if (!this.isMultiplayer()) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to determine server ping (in singleplayer)!");
            return;
        }

        if (this.isSearching) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Currently calculating ping! Please wait...");
            return;
        }

        InetAddress server = ((NetworkManagerAccessor) ((NetClientHandlerAccessor) this.getMinecraft().getSendQueue()).tensor_getNetManager()).tensor_getNetworkSocket().getInetAddress();

        if (server == null) {
            this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to determine server ping (could not find server connection)!");
            this.isSearching = false;
            return;
        }

        this.sendClientMessageWithHeader(this.getName() + ": Calculating server ping...");

        this.isSearching = true;

        //Is this DDOS? :O
        Thread thread = new Thread(() -> {
            int ms = 0;

            int start = 1;

            try {
                boolean reached = false;

                if (!server.isReachable(50)) {
                    start = 51;
                } else {
                    reached = true;
                }

                if (!reached) {
                    if (!server.isReachable(100)) {
                        start = 101;
                    } else {
                        reached = true;
                    }
                }

                if (!reached) {
                    if (!server.isReachable(150)) {
                        start = 151;
                    } else {
                        reached = true;
                    }
                }

                if (!reached) {
                    if (!server.isReachable(200)) {
                        start = 201;
                    } else {
                        reached = true;
                    }
                }

                if (!reached) {
                    if (!server.isReachable(250)) {
                        start = 250;
                    }
                }

            } catch (Exception ignored) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to determine server ping (unknown exception)!");
                this.isSearching = false;
                return;
            }

            if (start == 250) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Server ping not found (>250ms or unreachable)!");
                this.isSearching = false;
                return;
            }

            try {
                for (int i = start; i <= 250; i++) {
                    boolean reachable = server.isReachable(i);

                    if (reachable) {
                        ms = i;
                        break;
                    }
                }
            } catch (Exception ignored) {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Failed to determine server ping (unknown exception)!");
                this.isSearching = false;
                return;
            }

            if (ms != 0) {
                this.sendClientMessageWithHeader(this.getName() + ": Server ping is " + ChatColor.AQUA.text() + ms + "ms" + ChatColor.DEFAULT.text() + "!");
            } else {
                this.sendClientMessageWithHeader(ChatColor.RED.text() + this.getName() + ": Server ping not found (>250ms or unreachable)!");
            }

            this.isSearching = false;
        });

        thread.start();
    }
}
