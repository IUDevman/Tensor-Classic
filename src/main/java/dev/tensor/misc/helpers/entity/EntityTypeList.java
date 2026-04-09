package dev.tensor.misc.helpers.entity;

import dev.tensor.misc.imp.Globals;
import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Hoosiers
 * @since 03-31-2026
 */

public final class EntityTypeList implements Globals {

    private final ArrayList<String> entityListDefault;
    private final ArrayList<String> entityList;

    public EntityTypeList(ArrayList<String> entityList) {
        this.entityListDefault = new ArrayList<>(entityList);
        this.entityList = new ArrayList<>(entityList);
    }

    public ArrayList<String> getEntityList() {
        return this.entityList;
    }

    public void addToEntityList(Entity entity) {
        this.addToEntityList(entity.getClass().getSimpleName());
    }

    public void addToEntityList(String entity) {
        entity = EntityName.convert(entity);

        if (!this.entityList.contains(entity)) {
            this.entityList.add(entity);
        }
    }

    public void removeFromEntityList(Entity entity) {
        this.removeFromEntityList(entity.getClass().getSimpleName());
    }

    public void removeFromEntityList(String entity) {
        entity = EntityName.convert(entity);

        this.entityList.remove(entity);
    }

    public void clearEntityList() {
        this.entityList.clear();
    }

    public void resetEntityList() {
        this.clearEntityList();
        this.entityList.addAll(entityListDefault);
    }

    public boolean containsEntity(Entity entity) {
        return this.containsEntity(entity.getClass().getSimpleName());
    }

    public boolean containsEntity(String entity) {
        entity = EntityName.convert(entity);

        return this.entityList.contains(entity);
    }

    public void loadEntityListFromString(String string) {
        this.clearEntityList();

        if (string.equalsIgnoreCase("null")) {
            return;
        }

        String[] pooledString = string.split("_");

        for (int i = 0; i < pooledString.length; i++) {
            this.addToEntityList(pooledString[i]);
        }
    }

    public String saveEntityListToString() {
        if (this.entityList.isEmpty()) {
            return "null";
        }

        AtomicReference<String> list = new AtomicReference<>("");

        this.entityList.forEach(entity -> list.set(list.get() + entity + "_"));
        list.set(list.get().substring(0, list.get().length() - 1));

        return list.get();
    }

    private static final ArrayList<String> ANIMALS = new ArrayList<>();

    public static ArrayList<String> ANIMALS() {
        if (ANIMALS.isEmpty()) {
            ANIMALS.add(EntityName.convert(EntityChicken.class.getSimpleName()));
            ANIMALS.add(EntityName.convert(EntityCow.class.getSimpleName()));
            ANIMALS.add(EntityName.convert(EntityPig.class.getSimpleName()));
            ANIMALS.add(EntityName.convert(EntitySheep.class.getSimpleName()));
            ANIMALS.add(EntityName.convert(EntitySquid.class.getSimpleName()));
            ANIMALS.add(EntityName.convert(EntityWolf.class.getSimpleName()));
        }

        return new ArrayList<>(ANIMALS);
    }

    private static final ArrayList<String> HOSTILES = new ArrayList<>();

    public static ArrayList<String> HOSTILES() {
        if (HOSTILES.isEmpty()) {
            HOSTILES.add(EntityName.convert(EntityCreeper.class.getSimpleName()));
            HOSTILES.add(EntityName.convert(EntityGhast.class.getSimpleName()));
            HOSTILES.add(EntityName.convert(EntityGiantZombie.class.getSimpleName()));
            HOSTILES.add(EntityName.convert(EntitySkeleton.class.getSimpleName()));
            HOSTILES.add(EntityName.convert(EntitySlime.class.getSimpleName()));
            HOSTILES.add(EntityName.convert(EntitySpider.class.getSimpleName()));
            HOSTILES.add(EntityName.convert(EntityZombie.class.getSimpleName()));
            HOSTILES.add(EntityName.convert(EntityPigZombie.class.getSimpleName()));
        }

        return new ArrayList<>(HOSTILES);
    }

    private static final ArrayList<String> MISC_KA = new ArrayList<>();

    public static ArrayList<String> MISC_KA() {
        if (MISC_KA.isEmpty()) {
            MISC_KA.add(EntityName.convert(EntityBoat.class.getSimpleName()));
            MISC_KA.add(EntityName.convert(EntityFireball.class.getSimpleName()));
            MISC_KA.add(EntityName.convert(EntityMinecart.class.getSimpleName()));
            MISC_KA.add(EntityName.convert(EntityPainting.class.getSimpleName()));
        }

        return new ArrayList<>(MISC_KA);
    }

    private static final ArrayList<String> MISC = new ArrayList<>();

    public static ArrayList<String> MISC() {
        if (MISC.isEmpty()) {
            MISC.add(EntityName.convert(EntityArrow.class.getSimpleName()));
            MISC.add(EntityName.convert(EntityBoat.class.getSimpleName()));
            MISC.add(EntityName.convert(EntityEgg.class.getSimpleName()));
            MISC.add(EntityName.convert(EntityFallingSand.class.getSimpleName()));
            MISC.add(EntityName.convert(EntityFireball.class.getSimpleName()));
            MISC.add(EntityName.convert(EntityFish.class.getSimpleName()));
            MISC.add(EntityName.convert(EntityMinecart.class.getSimpleName()));
            MISC.add(EntityName.convert(EntityPainting.class.getSimpleName()));
            MISC.add(EntityName.convert(EntitySnowball.class.getSimpleName()));
            MISC.add(EntityName.convert(EntityTNTPrimed.class.getSimpleName()));
        }

        return new ArrayList<>(MISC);
    }

    public static ArrayList<String> EMPTY() {
        return new ArrayList<>();
    }
}
