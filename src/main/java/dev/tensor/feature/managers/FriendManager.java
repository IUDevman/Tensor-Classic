package dev.tensor.feature.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.tensor.misc.helpers.TensorLogger;
import dev.tensor.misc.imp.manager.Manager;
import net.minecraft.src.EntityOtherPlayerMP;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

public final class FriendManager implements Manager {

    @Override
    public String getName() {
        return "FriendManager";
    }

    private final ArrayList<String> FRIENDS = new ArrayList<>();

    @Override
    public void onLoad(TensorLogger LOGGER) {
        this.loadFriends(LOGGER);
    }

    @Override
    public void onSave(TensorLogger LOGGER) {
        this.saveFriends(LOGGER);
    }

    private void loadFriends(TensorLogger LOGGER) {
        try {
            Path path = Paths.get(this.getConfigManager().getConfigPath() + this.getName() + ".json");

            if (!Files.exists(path)) return;

            JsonObject jsonObject = this.getConfigManager().getJsonObjectFromPath(path);
            JsonArray jsonArray = jsonObject.get("Friends").getAsJsonArray();

            this.clearFriends();
            jsonArray.forEach(jsonElement -> this.addFriend(jsonElement.getAsString()));

        } catch (Exception ignored) {
            LOGGER.log("Failed to load config for: " + this.getName(), TensorLogger.Level.ERROR);
        }
    }

    public ArrayList<String> getFriends() {
        return this.FRIENDS;
    }

    public void clearFriends() {
        this.getFriends().clear();
    }

    public void addFriend(EntityOtherPlayerMP entity) {
        this.addFriend(entity.username);
    }

    public void addFriend(String name) {
        this.getFriends().add(name.toLowerCase());
    }

    public void removeFriend(EntityOtherPlayerMP entity) {
        this.removeFriend(entity.username);
    }

    public void removeFriend(String name) {
        this.getFriends().remove(name.toLowerCase());
    }

    public boolean isFriend(EntityOtherPlayerMP entity) {
        return this.isFriend(entity.username);
    }

    public boolean isFriend(String name) {
        return this.getFriends().contains(name.toLowerCase());
    }

    private void saveFriends(TensorLogger LOGGER) {
        try {
            Path path = Paths.get(this.getConfigManager().getConfigPath() + this.getName() + ".json");
            this.getConfigManager().registerFile(path);

            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray = new JsonArray();

            this.getFriends().forEach(jsonArray::add);
            jsonObject.add("Friends", jsonArray);

            this.getConfigManager().createAndWrite(path, jsonObject);
        } catch (Exception ignored) {
            LOGGER.log("Failed to load: " + this.getName(), TensorLogger.Level.ERROR);
        }
    }
}
