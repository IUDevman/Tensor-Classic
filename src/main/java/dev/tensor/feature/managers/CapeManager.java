package dev.tensor.feature.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.tensor.backend.events.variable.UpdateCapeEvent;
import dev.tensor.feature.hacks.Capes;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.Manager;
import net.minecraft.src.EntityOtherPlayerMP;
import org.slf4j.Logger;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 05-02-2026
 */

public final class CapeManager implements Manager {

    @Override
    public String getName() {
        return "CapeManager";
    }

    private String CAPE_URL = "https://raw.githubusercontent.com/IUDevman/gamesense-assets/refs/heads/main/files/tensor/cape_lowres.png";

    private final ArrayList<String> CAPED_PLAYERS = new ArrayList<>();

    @Override
    public void load(Logger LOGGER) {
        LOGGER.info("Loaded: " + this.getName());

        this.checkCapeURL(LOGGER);

        this.addCapedPlayer("go_hoosiers");
        this.addCapedPlayer("_lukflug");

        this.loadCapes(LOGGER);
    }

    @Override
    public void save(Logger LOGGER) {
        LOGGER.info("Saved: " + this.getName());

        this.saveCapes(LOGGER);
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onUpdateCapeEvent(UpdateCapeEvent event) {
        if (!this.shouldRenderCapes()) {
            return;
        }

        if (this.isCapedPlayer(event.username)) {
            event.playerCloakUrl = this.CAPE_URL;
            event.cancel();
        }
    }

    private void checkCapeURL(Logger LOGGER) {
        try {
            URL link = new URL(this.CAPE_URL);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();

            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != 200) {
                throw new Exception();
            }

        } catch (Exception ignored) {
            LOGGER.info("Failed to load cape (URL is broken)!");
            this.CAPE_URL = "broken";
        }
    }

    private void loadCapes(Logger LOGGER) {
        try {
            Path path = Paths.get(this.getConfigManager().getConfigPath() + this.getName() + ".json");

            if (!Files.exists(path)) return;

            JsonObject jsonObject = this.getConfigManager().getJsonObjectFromPath(path);
            JsonArray jsonArray = jsonObject.get("Capes").getAsJsonArray();

            this.clearCapedPlayers();
            jsonArray.forEach(jsonElement -> this.addCapedPlayer(jsonElement.getAsString()));

        } catch (Exception ignored) {
            LOGGER.info("Failed to load: " + this.getName());
        }
    }

    private void saveCapes(Logger LOGGER) {
        try {
            Path path = Paths.get(this.getConfigManager().getConfigPath() + this.getName() + ".json");
            this.getConfigManager().registerFile(path);

            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray = new JsonArray();

            this.getCapedPlayers().forEach(jsonArray::add);
            jsonObject.add("Capes", jsonArray);

            this.getConfigManager().createAndWrite(path, jsonObject);
        } catch (Exception ignored) {
            LOGGER.info("Failed to load: " + this.getName());
        }
    }

    public ArrayList<String> getCapedPlayers() {
        return this.CAPED_PLAYERS;
    }

    public void clearCapedPlayers() {
        this.getCapedPlayers().clear();
        this.refreshCapeRenders();
    }

    public void addCapedPlayer(String name) {
        this.getCapedPlayers().add(name.toLowerCase());
        this.refreshCapeRenders();
    }

    public void removeCapedPlayer(String name) {
        this.getCapedPlayers().remove(name.toLowerCase());
        this.refreshCapeRenders();
    }

    public boolean isCapedPlayer(EntityOtherPlayerMP entity) {
        return this.isCapedPlayer(entity.username);
    }

    public boolean isCapedPlayer(String name) {
        return this.getCapedPlayers().contains(name.toLowerCase());
    }

    private boolean shouldRenderCapes() {
        if (this.CAPE_URL.equalsIgnoreCase("broken")) {
            return false;
        }

        Capes capes = this.getHackManager().getHack(Capes.class);

        return capes != null && capes.isEnabled();
    }

    private void refreshCapeRenders() {
        if (this.failsNullCheck()) {
            return;
        }

        Capes capes = this.getHackManager().getHack(Capes.class);

        if (capes != null && capes.isEnabled()) {
            capes.refreshCapes();
        }
    }
}
