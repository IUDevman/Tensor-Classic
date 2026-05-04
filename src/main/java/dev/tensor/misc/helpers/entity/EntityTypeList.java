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
        this.addToEntityList(entity.getClass());
    }

    public void addToEntityList(Class<? extends Entity> entity) {
        String name = EntityName.CONVERTER.convert(entity);

        this.addToEntityList(name);
    }

    public void addToEntityList(String convertedName) {
        if (!this.entityList.contains(convertedName)) {
            this.entityList.add(convertedName);
        }
    }

    public void removeFromEntityList(Entity entity) {
        this.removeFromEntityList(entity.getClass());
    }

    public void removeFromEntityList(Class<? extends Entity> entity) {
        String name = EntityName.CONVERTER.convert(entity);

        this.removeFromEntityList(name);
    }

    public void removeFromEntityList(String convertedName) {
        this.entityList.remove(convertedName);
    }

    public void clearEntityList() {
        this.entityList.clear();
    }

    public void resetEntityList() {
        this.clearEntityList();
        this.entityList.addAll(entityListDefault);
    }

    public boolean containsEntity(Entity entity) {
        return this.containsEntity(entity.getClass());
    }

    public boolean containsEntity(Class<? extends Entity> entity) {
        String name = EntityName.CONVERTER.convert(entity);

        return this.containsEntity(name);
    }

    public boolean containsEntity(String convertedName) {
        return this.entityList.contains(convertedName);
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
            ANIMALS.add(EntityName.CONVERTER.convert(EntityChicken.class));
            ANIMALS.add(EntityName.CONVERTER.convert(EntityCow.class));
            ANIMALS.add(EntityName.CONVERTER.convert(EntityPig.class));
            ANIMALS.add(EntityName.CONVERTER.convert(EntitySheep.class));
            ANIMALS.add(EntityName.CONVERTER.convert(EntitySquid.class));
            ANIMALS.add(EntityName.CONVERTER.convert(EntityWolf.class));
        }

        return new ArrayList<>(ANIMALS);
    }

    private static final ArrayList<String> HOSTILES = new ArrayList<>();

    public static ArrayList<String> HOSTILES() {
        if (HOSTILES.isEmpty()) {
            HOSTILES.add(EntityName.CONVERTER.convert(EntityCreeper.class));
            HOSTILES.add(EntityName.CONVERTER.convert(EntityGhast.class));
            HOSTILES.add(EntityName.CONVERTER.convert(EntityGiantZombie.class));
            HOSTILES.add(EntityName.CONVERTER.convert(EntitySkeleton.class));
            HOSTILES.add(EntityName.CONVERTER.convert(EntitySlime.class));
            HOSTILES.add(EntityName.CONVERTER.convert(EntitySpider.class));
            HOSTILES.add(EntityName.CONVERTER.convert(EntityZombie.class));
            HOSTILES.add(EntityName.CONVERTER.convert(EntityPigZombie.class));
        }

        return new ArrayList<>(HOSTILES);
    }

    private static final ArrayList<String> MISC_KA = new ArrayList<>();

    public static ArrayList<String> MISC_KA() {
        if (MISC_KA.isEmpty()) {
            MISC_KA.add(EntityName.CONVERTER.convert(EntityBoat.class));
            MISC_KA.add(EntityName.CONVERTER.convert(EntityFireball.class));
            MISC_KA.add(EntityName.CONVERTER.convert(EntityMinecart.class));
            MISC_KA.add(EntityName.CONVERTER.convert(EntityPainting.class));
        }

        return new ArrayList<>(MISC_KA);
    }

    private static final ArrayList<String> MISC = new ArrayList<>();

    public static ArrayList<String> MISC() {
        if (MISC.isEmpty()) {
            MISC.add(EntityName.CONVERTER.convert(EntityArrow.class));
            MISC.add(EntityName.CONVERTER.convert(EntityBoat.class));
            MISC.add(EntityName.CONVERTER.convert(EntityEgg.class));
            MISC.add(EntityName.CONVERTER.convert(EntityFallingSand.class));
            MISC.add(EntityName.CONVERTER.convert(EntityFireball.class));
            MISC.add(EntityName.CONVERTER.convert(EntityFish.class));
            MISC.add(EntityName.CONVERTER.convert(EntityMinecart.class));
            MISC.add(EntityName.CONVERTER.convert(EntityPainting.class));
            MISC.add(EntityName.CONVERTER.convert(EntitySnowball.class));
            MISC.add(EntityName.CONVERTER.convert(EntityTNTPrimed.class));
        }

        return new ArrayList<>(MISC);
    }

    public static ArrayList<String> EMPTY() {
        return new ArrayList<>();
    }
}
