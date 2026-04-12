package dev.tensor.feature.commands;

import dev.tensor.backend.mixins.accessors.NetClientHandlerAccessor;
import dev.tensor.backend.mixins.accessors.NetworkManagerAccessor;
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

    @Override
    public void onCommandTriggered() {
        if (!this.isMultiplayer()) {
            this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Failed to determine server ping (in singleplayer)!");
        }

        InetAddress server = ((NetworkManagerAccessor) ((NetClientHandlerAccessor) this.getMinecraft().getSendQueue()).getNetManager()).getNetworkSocket().getInetAddress();

        if (server == null) {
            return;
        }

        this.sendClientMessageWithPrefix(this.getName() + ": Calculating server ping...");

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
            }

            if (start == 250) {
                this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Server ping not found (>250ms or unreachable)!");
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
            }

            if (ms != 0) {
                this.sendClientMessageWithPrefix(this.getName() + ": Server ping is " + this.getColor(ChatColor.AQUA) + ms + "ms" + this.getColor(ChatColor.DEFAULT) + "!");
            } else {
                this.sendClientMessageWithPrefix(this.getColor(ChatColor.RED) + this.getName() + ": Server ping not found (>250ms or unreachable)!");
            }
        });

        thread.start();
    }
}
