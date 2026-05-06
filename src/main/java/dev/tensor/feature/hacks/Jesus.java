package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.FluidCollisionEvent;
import dev.tensor.backend.mixins.accessors.EntityAccessor;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import net.minecraft.src.Material;
import org.lwjgl.input.Keyboard;

/**
 * @author Hoosiers
 * @since 04-05-2026
 */

@Hack.Info(name = "Jesus", category = Category.Movement, bind = Keyboard.KEY_J)
public final class Jesus extends Hack {

    public final BooleanSetting checkDistance = new BooleanSetting("Check Distance", true);
    @SuppressWarnings("unused")
    public final EnumSetting denomination = new EnumSetting("Denomination", Denomination.Baptist);

    @Override
    public void onClientTick() {
        HephFreecam hephFreecam = this.getHackManager().getHack(HephFreecam.class);

        if (hephFreecam.isEnabled()) {
            return;
        }

        boolean inWater = this.getPlayer().isInWater();
        boolean inLava = this.getPlayer().isInsideOfMaterial(Material.lava);

        if (inWater || inLava) {

            if (this.getMinecraft().currentScreen == null && Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindSneak.keyCode)) {
                return;
            }

            this.getPlayer().motionY = inLava ? 1.20D : 0.30D;
        }
    }

    @SuppressWarnings("unused")
    EventAction<FluidCollisionEvent> fluidCollisionEventAction = this.addEventAction(new EventAction<>(FluidCollisionEvent.class, event -> {
        boolean inWater = this.getPlayer().isInWater();
        boolean inLava = this.getPlayer().isInsideOfMaterial(Material.lava);

        if (inWater || inLava) {
            return;
        }

        if (this.checkDistance.getValue() && this.isFluidToFarAway2D(event.x, event.z)) {
            return;
        }

        Material material = this.getWorld().getBlockMaterial(event.x, event.y, event.z);

        if (material.equals(Material.lava)) {
            event.cancel();
            return;
        }

        if (this.getMinecraft().currentScreen == null && Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindSneak.keyCode)) {
            return;
        }

        if (!(((EntityAccessor) this.getPlayer()).tensor_getFallDistance() >= 3)) {
            event.cancel();
        }
    }));

    private boolean isFluidToFarAway2D(int fluidX, int fluidZ) {
        double x = this.getPlayer().posX;
        double z = this.getPlayer().posZ;

        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

        if (freecam != null && freecam.isEnabled()) {
            x = freecam.oldX;
            z = freecam.oldZ;
        }

        if (Math.pow(fluidX - x, 2) > 1) {
            return true;
        }

        return Math.pow(fluidZ - z, 2) > 1;
    }

    private enum Denomination {
        Baptist,
        Methodist,
        Presbyterian,
        Orthodox,
        Catholic,
        Christian,
        Lutheran,
        Spirit,
        Reformed,
        Episcopal,
        Iran
    }
}
