package dev.tensor.feature.managers;

import dev.tensor.misc.imp.Manager;
import net.minecraft.src.EntityOtherPlayerMP;
import org.slf4j.Logger;

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
    public void load(Logger LOGGER) {
        LOGGER.info("Loaded: " + this.getName());
    }

    public ArrayList<String> getFriends() {
        return this.FRIENDS;
    }

    public void addFriend(EntityOtherPlayerMP entity) {
        this.addFriend(entity.username);
    }

    public void addFriend(String name) {
        this.getFriends().add(name.toLowerCase());
    }

    public boolean isFriend(EntityOtherPlayerMP entity) {
        return this.isFriend(entity.username);
    }

    public boolean isFriend(String name) {
        return this.getFriends().contains(name.toLowerCase());
    }

    @Override
    public void save(Logger LOGGER) {
        LOGGER.info("Saved: " + this.getName());
    }
}
