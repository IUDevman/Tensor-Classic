package dev.tensor.misc.helpers.entity;

import net.minecraft.src.*;

import java.util.HashMap;

public final class EntityName {

    public static final EntityName CONVERTER = new EntityName();

    private final HashMap<Class<? extends Entity>, String> nameMap = new HashMap<>();

    private EntityName() {
        this.nameMap.put(EntityChicken.class, "Chicken");
        this.nameMap.put(EntityCow.class, "Cow");
        this.nameMap.put(EntityPig.class, "Pig");
        this.nameMap.put(EntitySheep.class, "Sheep");
        this.nameMap.put(EntitySquid.class, "Squid");
        this.nameMap.put(EntityWolf.class, "Wolf");
        this.nameMap.put(EntityCreeper.class, "Creeper");
        this.nameMap.put(EntityGhast.class, "Ghast");
        this.nameMap.put(EntityGiantZombie.class, "Giant");
        this.nameMap.put(EntitySkeleton.class, "Skeleton");
        this.nameMap.put(EntitySlime.class, "Slime");
        this.nameMap.put(EntitySpider.class, "Spider");
        this.nameMap.put(EntityZombie.class, "Zombie");
        this.nameMap.put(EntityPigZombie.class, "Zombie Pigman");
        this.nameMap.put(EntityArrow.class, "Arrow");
        this.nameMap.put(EntityBoat.class, "Boat");
        this.nameMap.put(EntityEgg.class, "Egg");
        this.nameMap.put(EntityFallingSand.class, "Falling Sand");
        this.nameMap.put(EntityFireball.class, "Fireball");
        this.nameMap.put(EntityFish.class, "Fish");
        this.nameMap.put(EntityMinecart.class, "Minecart");
        this.nameMap.put(EntityPainting.class, "Painting");
        this.nameMap.put(EntitySnowball.class, "Snowball");
        this.nameMap.put(EntityTNTPrimed.class, "TNT Primed");
    }

    public String convert(Class<? extends Entity> toConvert) {
        String result = this.nameMap.get(toConvert);

        if (result == null || result.isEmpty()) {
            return toConvert.getSimpleName();
        }

        return result;
    }
}
