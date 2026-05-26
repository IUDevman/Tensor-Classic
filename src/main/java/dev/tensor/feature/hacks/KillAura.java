package dev.tensor.feature.hacks;

import dev.tensor.backend.events.EventCache;
import dev.tensor.backend.events.variable.AttackEntityEvent;
import dev.tensor.misc.helpers.entity.EntityTypeList;
import dev.tensor.misc.helpers.entity.FakePlayer;
import dev.tensor.misc.helpers.rotation.RotationPriority;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.hack.render.Render3DHack;
import dev.tensor.misc.imp.hack.render.RenderOrder;
import dev.tensor.misc.imp.setting.settings.*;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Hoosiers
 * @since 03-19-2026
 */

@Hack.Info(name = "KillAura", category = Category.Combat, bind = Keyboard.KEY_R)
public final class KillAura extends Render3DHack {

    @Override
    public RenderOrder getRenderOrder() {
        return RenderOrder.KillAura;
    }

    public final BooleanSetting players = new BooleanSetting("Players", true);
    public final BooleanSetting ignoreFriends = new BooleanSetting("Ignore Friends", true);
    public final EnumSetting animals = new EnumSetting("Animals", Attack.None);
    public final EntityTypeListSetting animalsCustom = new EntityTypeListSetting("Animals List", new EntityTypeList(EntityTypeList.ANIMALS()), new EntityTypeList(EntityTypeList.ANIMALS()));
    public final BooleanSetting ignoreOwnPet = new BooleanSetting("Ignore Pets", true);
    public final EnumSetting hostiles = new EnumSetting("Hostiles", Attack.All);
    public final EntityTypeListSetting hostilesCustom = new EntityTypeListSetting("Hostiles List", new EntityTypeList(EntityTypeList.HOSTILES()), new EntityTypeList(EntityTypeList.HOSTILES()));
    public final EnumSetting misc = new EnumSetting("Misc", Attack.None);
    public final EntityTypeListSetting miscCustom = new EntityTypeListSetting("Misc List", new EntityTypeList(EntityTypeList.EMPTY()), new EntityTypeList(EntityTypeList.MISC_KA()));
    public final BooleanSetting behindWalls = new BooleanSetting("Behind Walls", false);
    public final EnumSetting freecamMode = new EnumSetting("Freecam", Freecam.Player);
    public final DoubleSetting range = new DoubleSetting("Range", 5.0, 1.0, 10.0, 1);
    public final IntegerSetting delay = new IntegerSetting("Tick Delay", 4, 0, 20);
    public final EnumSetting type = new EnumSetting("Hit Type", HitType.Packet);
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    public final BooleanSetting swing = new BooleanSetting("Swing", true);
    public final EnumSetting sort = new EnumSetting("Sort By", Sort.Distance);
    public final BooleanSetting esp = new BooleanSetting("ESP", true);
    public final EnumSetting renderMode = new EnumSetting("ESP Mode", RenderMode.Fill);
    public final IntegerSetting lineWidth = new IntegerSetting("Line Width", 2, 1, 5);
    public final IntegerSetting fillAlpha = new IntegerSetting("Fill Alpha", 50, 0, 255);
    public final ColorSetting esp_color = new ColorSetting("ESP Color", new Color(255, 0, 0, 255));

    private final ArrayList<Entity> target_Entities = new ArrayList<>();
    private int attackDelay = 0;

    @Override
    public void onEnable(boolean isNull) {
        this.attackDelay = this.delay.getValue();
    }

    @Override
    public void onDisable(boolean isNull) {
        this.target_Entities.clear();
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

        double playerX = this.getPlayer().posX;
        double playerY = this.getPlayer().posY;
        double playerZ = this.getPlayer().posZ;

        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

        if (freecam != null && freecam.isEnabled()) {

            if (this.freecamMode.getValue().equals(Freecam.Stop)) {
                return;
            }

            if (this.freecamMode.getValue().equals(Freecam.Player)) {
                playerX = freecam.oldX;
                playerY = freecam.oldY;
                playerZ = freecam.oldZ;
            }
        }

        ConcurrentHashMap<Entity, Double> entitiesToKill = new ConcurrentHashMap<>();

        final double finalPlayerX = playerX;
        final double finalPlayerY = playerY;
        final double finalPlayerZ = playerZ;

        this.getFilteredLoadedEntities().parallelStream().forEach(entity -> {
            if (entity == this.getPlayer() || entity instanceof FakePlayer) {
                return;
            }

            if (this.shouldAttackEntity(entity)) {
                double distance = this.getDistance(entity.posX - finalPlayerX, entity.posY - finalPlayerY, entity.posZ - finalPlayerZ);

                if (distance <= this.range.getValue()) {
                    entitiesToKill.put(entity, distance);
                }
            }
        });

        for (Entity entity : this.getFilteredLoadedEntities()) {
            if (entity == this.getPlayer() || entity instanceof FakePlayer) {
                continue;
            }

            if (this.shouldAttackEntity(entity)) {

                if (!this.behindWalls.getValue() && !this.isEntityVisible(entity, playerX, playerY, playerZ)) {
                    continue;
                }

                double distance = this.getDistance(entity.posX - playerX, entity.posY - playerY, entity.posZ - playerZ);

                if (distance <= this.range.getValue()) {
                    entitiesToKill.put(entity, distance);
                }
            }
        }

        if (entitiesToKill.isEmpty()) {
            return;
        }

        if (!this.behindWalls.getValue()) {
            entitiesToKill.entrySet().removeIf(entityDoubleEntry -> !this.isEntityVisible(entityDoubleEntry.getKey(), finalPlayerX, finalPlayerY, finalPlayerZ));

            if (entitiesToKill.isEmpty()) {
                return;
            }
        }

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

    @Override
    public void onRender3D(float renderPartialTicks) {
        if (!this.esp.getValue()) {
            return;
        }

        this.target_Entities.forEach(entity -> this.renderESP(entity, renderPartialTicks));
    }

    private boolean shouldAttackEntity(Entity entity) {
        if (entity instanceof EntityLiving && !entity.isEntityAlive()) {
            return false;
        }

        if (this.players.getValue() && entity instanceof EntityOtherPlayerMP) {
            return (!this.ignoreFriends.getValue() || !this.getFriendManager().isFriend((EntityOtherPlayerMP) entity));
        }

        Class<? extends Entity> entityClass = entity.getClass();

        if (this.hostilesCustom.getFullValue().containsEntity(entityClass)) {

            switch ((Attack) this.hostiles.getValue()) {
                case All:
                    return true;
                case Custom:
                    return this.hostilesCustom.containsEntity(entityClass);
                case None:
                    return false;
            }
        }

        if (this.animalsCustom.getFullValue().containsEntity(entityClass)) {

            if (entity instanceof EntityWolf) {

                String owner = ((EntityWolf) entity).getWolfOwner();

                if (owner.isEmpty()) {
                    return this.shouldAttackAnimal(entityClass);
                }

                if (owner.equalsIgnoreCase(this.getPlayer().username)) {
                    return !this.ignoreOwnPet.getValue() && this.shouldAttackAnimal(entityClass);
                }

                if (this.getFriendManager().isFriend(owner)) {
                    return !this.ignoreFriends.getValue() && this.players.getValue();
                }

                return this.players.getValue();

            } else {
                return this.shouldAttackAnimal(entityClass);
            }
        }

        if (this.miscCustom.getFullValue().containsEntity(entityClass)) {

            switch ((Attack) this.misc.getValue()) {
                case All:
                    return true;
                case Custom:
                    return this.miscCustom.getValue().containsEntity(entityClass);
                case None:
                    return false;
            }
        }

        return false;
    }

    private boolean shouldAttackAnimal(Class<? extends Entity> entityClass) {
        switch ((Attack) this.animals.getValue()) {
            case All:
                return true;
            case Custom:
                return this.animalsCustom.containsEntity(entityClass);
            case None:
                return false;
        }

        return false;
    }

    private void attackEntity(Entity entity) {

        if (this.rotate.getValue()) {
            this.getRotationManager().addToRotationQueue(this.getRotationTo(entity), RotationPriority.KillAura);
        }

        if (this.swing.getValue()) {
            this.getPlayer().swingItem();
        }

        switch ((HitType) this.type.getValue()) {
            case Vanilla:
                this.getMinecraft().playerController.attackEntity(this.getPlayer(), entity);
                break;
            case Packet:
                if (this.shouldCallEvent(AttackEntityEvent.class)) {
                    EventCache.getCachedEvent(AttackEntityEvent.class).withVariables(entity).call(true);
                }

                this.sendPacket(new Packet7UseEntity(this.getPlayer().entityId, entity.entityId, 1));
                break;
        }
    }

    //Method is not thread safe, so cannot be run in parallel.
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isEntityVisible(Entity entity, double playerX, double playerY, double playerZ) {
        if (this.getWorld().rayTraceBlocks(Vec3D.createVector(playerX, playerY + this.getPlayer().getEyeHeight(), playerZ), Vec3D.createVector(entity.posX, entity.posY + (double) entity.height / 2.0D, entity.posZ)) == null) {
            return true;
        } else {
            return this.getWorld().rayTraceBlocks(Vec3D.createVector(playerX, playerY, playerZ), Vec3D.createVector(entity.posX, entity.posY + (double) entity.height / 2.0D, entity.posZ)) == null;
        }
    }

    private void renderESP(Entity entity, float renderPartialTicks) {
        AxisAlignedBB bb = entity.boundingBox;

        double dX = this.translateOffset(entity.lastTickPosX, entity.posX, renderPartialTicks);
        double dY = this.translateOffset(entity.lastTickPosY, entity.posY, renderPartialTicks);
        double dZ = this.translateOffset(entity.lastTickPosZ, entity.posZ, renderPartialTicks);

        double x = bb.minX - RenderManager.renderPosX + dX;
        double y = bb.minY - RenderManager.renderPosY + dY;
        double z = bb.minZ - RenderManager.renderPosZ + dZ;

        double wX = bb.maxX - RenderManager.renderPosX + dX;
        double wY = bb.maxY - RenderManager.renderPosY + dY;
        double wZ = bb.maxZ - RenderManager.renderPosZ + dZ;

        switch ((RenderMode) this.renderMode.getValue()) {
            case Outline:
                this.drawOutlineBox(x, y, z, wX, wY, wZ, this.lineWidth.getValue(), this.esp_color.getValue());
                break;
            case Fill:
                this.drawFillBox(x, y, z, wX, wY, wZ, new Color(this.esp_color.getValue().getRed(), this.esp_color.getValue().getGreen(), this.esp_color.getValue().getBlue(), this.fillAlpha.getValue()));
                break;
            case Both:
                this.drawFillBox(x, y, z, wX, wY, wZ, new Color(this.esp_color.getValue().getRed(), this.esp_color.getValue().getGreen(), this.esp_color.getValue().getBlue(), this.fillAlpha.getValue()));
                this.drawOutlineBox(x, y, z, wX, wY, wZ, this.lineWidth.getValue(), this.esp_color.getValue());
                break;
        }
    }

    public enum Attack {
        All,
        None,
        Custom
    }

    private enum Freecam {
        Stop,
        Player,
        Camera
    }

    public enum HitType {
        Vanilla,
        Packet
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
