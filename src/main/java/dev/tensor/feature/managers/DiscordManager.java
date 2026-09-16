package dev.tensor.feature.managers;

import dev.firstdark.rpc.DiscordRpc;
import dev.firstdark.rpc.enums.ActivityType;
import dev.firstdark.rpc.enums.ErrorCode;
import dev.firstdark.rpc.handlers.RPCEventHandler;
import dev.firstdark.rpc.models.DiscordRichPresence;
import dev.firstdark.rpc.models.User;
import dev.tensor.TensorClassic;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.helpers.extensions.TensorRPCEventHandler;
import dev.tensor.misc.imp.Manager;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * @author Hoosiers
 * @since 05-06-2026
 */

public final class DiscordManager implements Manager {

    @Override
    public String getName() {
        return "DiscordManager";
    }

    private DiscordRpc rpc = null;
    private RPCEventHandler handler = null;
    private Thread presence = null;

    private long updateTimeMS = 15000L;

    private boolean usernameCheck = false;
    private boolean serverCheck = false;
    private String icon = "logo_tensor1_upscale";

    @Override
    public void load(TensorLogger LOGGER) {
        LOGGER.log("Loaded: " + this.getName(), TensorLogger.Level.INFO);
    }

    @Override
    public void save(TensorLogger LOGGER) {
        LOGGER.log("Saved: " + this.getName(), TensorLogger.Level.INFO);

        if (this.rpc != null) {
            this.rpc.shutdown();
        }

        this.rpc = null;
        this.handler = null;
        this.presence = null;
    }

    public void startInstance(boolean usernameCheck, boolean serverCheck, String icon, long updateTimeMS) {
        this.updateUsernameCheck(usernameCheck);
        this.updateServerCheck(serverCheck);
        this.updateIcon(icon);
        this.setUpdateTimeMS(updateTimeMS);

        if (this.rpc == null) {
            this.loadInstance();
        }

        try {
            this.rpc.init(this.getAppID(), this.handler, false);

            this.presence.start();

        } catch (Exception ignored) {
            this.getTensorLogger().log("Failed to initialize DiscordRPC instance!", TensorLogger.Level.WARN);

            this.stopInstance();

            this.rpc = null;
            this.handler = null;
            this.presence = null;
        }
    }

    public void stopInstance() {
        if (this.rpc != null) {
            this.rpc.shutdown();
        }

        if (this.presence != null && !this.presence.isInterrupted()) {
            this.presence.interrupt();
        }
    }

    public void updateInstance() {
        if (this.rpc == null) {
            return;
        }

        this.rpc.updatePresence(this.buildNewPresence());
    }

    public void updateUsernameCheck(boolean usernameCheck) {
        this.usernameCheck = usernameCheck;
    }

    public void updateServerCheck(boolean serverCheck) {
        this.serverCheck = serverCheck;
    }

    public void updateIcon(String icon) {
        this.icon = icon;
    }

    public long getUpdateTimeMS() {
        return this.updateTimeMS;
    }

    public void setUpdateTimeMS(long updateTimeMS) {
        if (updateTimeMS < 10000L) {
            this.updateTimeMS = 10000L;
            return;
        }

        this.updateTimeMS = updateTimeMS;
    }

    private void loadInstance() {
        this.rpc = new DiscordRpc();

        this.handler = new TensorRPCEventHandler() {

            @Override
            public void ready(User user) {
                updateInstance();
            }

            @Override
            public void disconnected(ErrorCode errorCode, String message) {
                this.getTensorLogger().log("Disconnected DiscordRPC '" + errorCode + "' - '" + message + "'!", TensorLogger.Level.INFO);
            }

            @Override
            public void errored(ErrorCode errorCode, String message) {
                this.getTensorLogger().log("Errored DiscordRPC '" + errorCode + "' - '" + message + "'!", TensorLogger.Level.WARN);
            }
        };

        if (this.presence != null && !this.presence.isInterrupted()) {
            this.presence.interrupt();
        }

        this.presence = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(this.getUpdateTimeMS());

                    if (this.rpc != null) {
                        this.updateInstance();
                    }

                } catch (Exception ignored) {
                }
            }
        });
    }

    private DiscordRichPresence buildNewPresence() {
        String name = TensorClassic.MOD_NAME + " " + TensorClassic.MOD_VERSION;
        String state = this.failsNullCheck() ? "In the Menus" : (!this.getWaypointServerName().equalsIgnoreCase("Singleplayer") ? (this.serverCheck ? "On " + this.getWaypointServerName() : "In Multiplayer") : "In Singleplayer");
        String details = this.icon.equalsIgnoreCase("logo_lukflug") ? "Lukflug" : "{name}";

        ActivityType activityType = ActivityType.PLAYING;

        String largeImageKey = this.icon;
        String largeImageUrl = "https://github.com/IUDevman/Tensor-Classic";
        String largeText = "Click to download!";

        String smallImageKey = "logo_iudevman";
        String smallImageUrl = "https://github.com/IUDevman";
        String smallImageText = "Made by IUDevman!";

        if (this.icon.equalsIgnoreCase("logo_lukflug")) {
            name = "Lukflug";
            state = "Lukflug Lukflug Lukflug";
            activityType = ActivityType.WATCHING;
            largeText = "Lukflug!";
            smallImageKey = this.icon;
            smallImageText = "Lukflug!";
        }

        if (this.usernameCheck && !this.failsNullCheck()) {

            details = details.replace("{name}", this.getPlayer().username);

            return DiscordRichPresence.builder()
                    .name(name)
                    .state(state)
                    .details(details)
                    .activityType(activityType)
                    .largeImageKey(largeImageKey)
                    .largeImageText(largeText)
                    .largeUrl(largeImageUrl)
                    .smallImageKey(smallImageKey)
                    .smallUrl(smallImageUrl)
                    .smallImageText(smallImageText)
                    .build();
        }

        return DiscordRichPresence.builder()
                .name(name)
                .state(state)
                .activityType(activityType)
                .largeImageKey(largeImageKey)
                .largeImageText(largeText)
                .largeUrl(largeImageUrl)
                .smallImageKey(smallImageKey)
                .smallUrl(smallImageUrl)
                .smallImageText(smallImageText)
                .build();
    }

    //Obviously easy to find but will prevent GitHub bots from scraping it.
    private String getAppID() {
        String encrypted = "YOLb1YT+eY85o1PwlNUX8+EtP8Cc4RO1Gb2NMKscEyQ=";

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("tensor1234");

        return encryptor.decrypt(encrypted);
    }
}
