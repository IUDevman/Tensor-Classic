package dev.tensor.feature.hacks;

import dev.tensor.backend.events.Packet10FlyingEvent;
import dev.tensor.misc.event.EventTarget;
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
    public final BooleanSetting ignoreOwnPet = new BooleanSetting("Ignore Pets", true);
    public final BooleanSetting offScreen = new BooleanSetting("Off Screen", true);
    public final BooleanSetting behindWalls = new BooleanSetting("Behind Walls", false);
    public final DoubleSetting range = new DoubleSetting("Range", 5.0, 1.0, 10.0, 1);
    public final IntegerSetting delay = new IntegerSetting("Tick Delay", 4, 0, 20);
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    public final BooleanSetting swing = new BooleanSetting("Swing", true);
    public final BooleanSetting esp = new BooleanSetting("ESP", true);
    public final EnumSetting renderMode = new EnumSetting("ESP Mode", RenderMode.Fill);
    public final IntegerSetting lineWidth = new IntegerSetting("Line Width", 2, 1, 5);
    public final IntegerSetting fillAlpha = new IntegerSetting("Fill Alpha", 50, 0, 255);
    public final ColorSetting esp_color = new ColorSetting("ESP Color", new Color(255, 0, 0, 255));
    public final EnumSetting sort = new EnumSetting("Sort By", Sort.Distance);

    private final ArrayList<Entity> target_Entities = new ArrayList<>();
    private int attackDelay = 0;

    private boolean rotated = false;
    private float pitch = 0;
    private float yaw = 0;

    @Override
    public void onDisable(boolean nullCheck) {
        this.target_Entities.clear();
        this.attackDelay = 0;
        this.rotated = false;
        this.pitch = 0;
        this.yaw = 0;
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
                
                if (!this.offScreen.getValue() && !this.isEntityOnScreen(entity)) {
                    continue;
                }

                if (!this.behindWalls.getValue() && !this.isEntityBehindWalls(entity)) {
                    continue;
                }

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

        if (!this.esp.getValue()) {
            return;
        }

        if (this.target_Entities.isEmpty()) {
            return;
        }

        this.target_Entities.forEach(this::renderESP);
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onPacket10FlyingEvent(Packet10FlyingEvent event) {
        if (this.rotate.getValue() && this.rotated) {
            event.packet.pitch = this.pitch;
            event.packet.yaw = this.yaw;
            this.pitch = 0;
            this.yaw = 0;
            this.rotated = false;
        }
    }

    private boolean shouldAttackEntity(Entity entity) {
        if (!entity.isEntityAlive()) {
            return false;
        }

        if (this.players.getValue() && entity instanceof EntityOtherPlayerMP) {
            return (!this.ignoreFriends.getValue() || !this.getFriendManager().isFriend((EntityOtherPlayerMP) entity));
        }

        if (this.hostiles.getValue() && (entity instanceof EntityMob || entity instanceof EntitySlime)) {
            return true;
        }

        if (entity instanceof EntityWolf) {
            EntityWolf entityWolf = (EntityWolf) entity;

            String owner = entityWolf.getWolfOwner();

            if (owner.isEmpty()) {
                return this.animals.getValue();

            } else if (owner.equalsIgnoreCase(this.getPlayer().username)) {
                if (this.ignoreOwnPet.getValue()) {
                    return false;

                } else {
                    return this.animals.getValue();
                }

            } else {
                boolean isFriend = this.getFriendManager().isFriend(owner);

                if (this.ignoreFriends.getValue() && isFriend) {
                    return false;

                } else {
                    return this.players.getValue();
                }
            }
        }

        return this.animals.getValue() && entity instanceof EntityAnimal;
    }

    private void attackEntity(Entity entity) {

        if (this.rotate.getValue()) {
            float[] pitchAndYaw = this.pitchAndYawTo(entity);

            this.pitch = pitchAndYaw[0];
            this.yaw = pitchAndYaw[1];
            this.rotated = true;
        }

        if (this.swing.getValue()) {
            this.getPlayer().swingItem();
        }

        this.getMinecraft().playerController.attackEntity(this.getPlayer(), entity);
    }

    //todo
    private boolean isEntityOnScreen(Entity entity) {
        return true;
    }

    private boolean isEntityBehindWalls(Entity entity) {

        if (this.getWorld().rayTraceBlocks(Vec3D.createVector(this.getPlayer().posX, this.getPlayer().posY + this.getPlayer().getEyeHeight(), this.getPlayer().posZ), Vec3D.createVector(entity.posX, entity.posY + (double) entity.height / 2.0D, entity.posZ)) == null) {
            return true;
        } else
            return this.getWorld().rayTraceBlocks(Vec3D.createVector(this.getPlayer().posX, this.getPlayer().posY, this.getPlayer().posZ), Vec3D.createVector(entity.posX, entity.posY + (double) entity.height / 2.0D, entity.posZ)) == null;
    }

    private void renderESP(Entity entity) {
        AxisAlignedBB bb = entity.boundingBox;

        double x = bb.minX - RenderManager.renderPosX;
        double y = bb.minY - RenderManager.renderPosY;
        double z = bb.minZ - RenderManager.renderPosZ;

        double wX = bb.maxX - RenderManager.renderPosX;
        double wY = bb.maxY - RenderManager.renderPosY;
        double wZ = bb.maxZ - RenderManager.renderPosZ;

        switch ((RenderMode) this.renderMode.getValue()) {
            case Outline:
                this.drawOutlineBox(x, y, z, wX, wY, wZ, this.lineWidth.getValue(), this.esp_color.getValue());
                break;
            case Fill:
                this.drawFillBox(x, y, z, wX, wY, wZ, new Color(this.esp_color.getValue().getRed(), this.esp_color.getValue().getGreen(), this.esp_color.getValue().getBlue(), this.fillAlpha.getValue()));
                break;
            case Both:
                this.drawFillBox(x, y, z, wX, wY, wZ, this.esp_color.getValue());
                this.drawOutlineBox(x, y, z, wX, wY, wZ, this.lineWidth.getValue(), new Color(this.esp_color.getValue().getRed(), this.esp_color.getValue().getGreen(), this.esp_color.getValue().getBlue(), this.fillAlpha.getValue()));
                break;
        }
    }

    public enum Sort {
        Distance,
        Health,
        Multi
    }

    public enum RenderMode {
        Outline,
        Fill,
        Both
    }
}
