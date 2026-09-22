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

@Hack.Info(name = "Jesus", category = Category.Movement, bind = Keyboard.KEY_J, description = "Allows you to walk on fluids such as water.")
public final class Jesus extends Hack {

    public final BooleanSetting checkDistance = new BooleanSetting("Check Distance", true, "Enable to toggle hack only when near water. This prevents other mobs from being Jesus amd sqids not moving.");
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
    private final EventAction<FluidCollisionEvent> fluidCollisionEventAction = this.addHackEventAction(new EventAction<>(FluidCollisionEvent.class, event -> {
        boolean inWater = this.getPlayer().isInWater();
        boolean inLava = this.getPlayer().isInsideOfMaterial(Material.lava);

        if (inWater || inLava) {
            return;
        }

        if (this.checkDistance.getValue() && this.isFluidTooFarAway2D(event.x, event.z)) {
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

    private boolean isFluidTooFarAway2D(int fluidX, int fluidZ) {
        double x = this.getPlayer().posX;
        double z = this.getPlayer().posZ;

        HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

        if (freecam != null && freecam.isEnabled()) {
            x = freecam.oldX;
            z = freecam.oldZ;
        }

        if (this.squaredD(fluidX - x) > 1) {
            return true;
        }

        return this.squaredD(fluidZ - z) > 1;
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
        Episcopal
    }
}
