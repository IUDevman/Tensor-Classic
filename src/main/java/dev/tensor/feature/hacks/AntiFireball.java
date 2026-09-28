package dev.tensor.feature.hacks;

import dev.tensor.misc.helpers.rotation.RotationPriority;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.DoubleSetting;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFireball;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Hoosiers
 * @since 09-27-2026
 */

//Honestly, you can probably just use KillAura for this, but I could see why you would want this to be its own thing.
@Hack.Info(name = "AntiFireball", category = Category.Combat, description = "Automatically blocks incoming fireballs.")
public final class AntiFireball extends Hack {

    public final DoubleSetting range = new DoubleSetting("Range", 5.0, 1.0, 10.0, 1, "Blocks", "How far should the hack search for fireballs.");
    public final BooleanSetting rotate = new BooleanSetting("Rotate", true, "Enable to adjust the players rotation to the direction of the fireball. Managed by the global Rotation hack.");
    public final BooleanSetting swing = new BooleanSetting("Swing", true, "Swings the player's hand when attacking fireballs.");

    @Override
    public void onClientTick() {
        CopyOnWriteArrayList<Entity> fireballsToTarget = new CopyOnWriteArrayList<>();

        double x = this.getPlayer().posX;
        double y = this.getPlayer().posY;
        double z = this.getPlayer().posZ;

        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

        if (freecam.isEnabled()) {
            x = freecam.oldX;
            y = freecam.oldY;
            z = freecam.oldZ;
        }

        final double fX = x;
        final double fY = y;
        final double fZ = z;

        this.getFilteredLoadedEntities().parallelStream().forEach(entity -> {

            if (entity instanceof EntityFireball) {
                double distance = this.getDistance(entity.posX - fX, entity.posY - fY, entity.posZ - fZ);

                if (distance <= this.range.getValue()) {
                    fireballsToTarget.add(entity);
                }
            }
        });

        if (fireballsToTarget.isEmpty()) {
            return;
        }

        fireballsToTarget.forEach(this::attackFireball);
    }

    private void attackFireball(Entity entity) {
        if (this.rotate.getValue()) {
            this.getRotationManager().addToRotationQueue(this.getRotationTo(entity), RotationPriority.KillAura);
        }

        if (this.swing.getValue()) {
            this.getPlayer().swingItem();
        }

        this.getMinecraft().playerController.attackEntity(this.getPlayer(), entity);
    }

    public enum Swap {
        None,
        No_Tool,
        Slot_9
    }
}
