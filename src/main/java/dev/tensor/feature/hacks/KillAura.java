package dev.tensor.feature.hacks;

import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.*;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

@Hack.Info(name = "KillAura", category = Category.Combat, bind = Keyboard.KEY_R)
public final class KillAura extends Hack {

    public final BooleanSetting animals = new BooleanSetting("Animals", true);
    public final BooleanSetting hostiles = new BooleanSetting("Hostiles", false);
    public final BooleanSetting players = new BooleanSetting("Players", true);
    public final BooleanSetting ignoreFriends = new BooleanSetting("Ignore Friends", true);
    public final BooleanSetting onlySeen = new BooleanSetting("Only Seen", true);
    public final DoubleSetting range = new DoubleSetting("Range", 5.0, 1.0, 10.0, 1);
    public final IntegerSetting delay = new IntegerSetting("Tick Delay", 4, 0, 20);
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    public final BooleanSetting swing = new BooleanSetting("Swing", true);
    public final BooleanSetting esp = new BooleanSetting("ESP", true);
    public final ColorSetting esp_color = new ColorSetting("ESP Color", new Color(255, 0,  0, 255));
    public final EnumSetting sort = new EnumSetting("Sort By", Sort.Distance);

    private ArrayList<Entity> target_Entities = new ArrayList<>();
    private int attackDelay = 0;

    @Override
    public void onDisable(boolean nullCheck) {
        this.target_Entities.clear();
        this.attackDelay = 0;
    }

    @Override
    public void onClientTick() {

        this.target_Entities.clear();

        if (this.attackDelay >= this.delay.getValue()) {
            this.attackDelay = 0;
        } else {
            this.attackDelay++;
            return;
        }

        LinkedHashMap<Entity, Double> entitiesToKill = new LinkedHashMap<>();

        for (int i = 0; i < this.getWorld().loadedEntityList.size(); i++) {
            Entity entity = (Entity) this.getWorld().loadedEntityList.get(i);

            if (entity == null) {
                continue;
            }

            if (entity == this.getPlayer()) {
                continue;
            }

            if (this.shouldAttackEntity(entity)) {

                double distance = this.getPlayer().getDistanceToEntity(entity);

                if (distance <= this.range.getValue()) {
                    entitiesToKill.put(entity, distance);
                }
            }
        }

        if (!entitiesToKill.isEmpty()) {

            List<Map.Entry<Entity, Double>> entryList = new ArrayList<>(entitiesToKill.entrySet());

            switch ((Sort) this.sort.getValue()) {
                case Health:
                    entryList.sort(Comparator.comparing(entityDoubleEntry -> 20 - entityDoubleEntry.getKey().heartsLife));
                    break;
                default:
                    entryList.sort(Map.Entry.comparingByValue());
                    break;
            }

            if (this.sort.getValue().equals(Sort.Multi)) {

                entitiesToKill.forEach((entity, aDouble) -> {
                    this.target_Entities.add(entity);
                    this.attackEntity(entity);
                });


            } else {
                Map.Entry<Entity, Double> result = entryList.stream().findFirst().orElse(null);

                if (result != null) {

                    this.target_Entities.add(result.getKey());

                    this.attackEntity(result.getKey());
                }
            }
        }
    }

    @Override
    public void onRender3D() {

        if (this.target_Entities.isEmpty()) {
            return;
        }

        this.target_Entities.forEach(entity -> {
            //todo: render box
        });
    }

    private boolean shouldAttackEntity(Entity entity) {
        if (!entity.isEntityAlive()) {
            return false;
        }

        if (this.players.getValue() && entity instanceof EntityOtherPlayerMP) {
            return (this.ignoreFriends.getValue() ? !this.getFriendManager().isFriend((EntityOtherPlayerMP) entity) : true);
        }

        if (this.hostiles.getValue() && (entity instanceof EntityMob || entity instanceof EntitySlime)) {
            return true;
        }

        if (this.animals.getValue() && entity instanceof EntityAnimal) {
            return true;
        }

        return false;
    }

    private void attackEntity(Entity entity) {

        if (this.rotate.getValue()) {
            //todo
        }

        if (this.swing.getValue())  {
            this.getPlayer().swingItem();
        }

        this.getMinecraft().playerController.attackEntity(this.getPlayer(), entity);
    }

    public enum Sort {
        Distance,
        Health,
        Multi
    }
}
