package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.backend.events.constant.UpdateClientPlayerActionEvent;
import dev.tensor.backend.events.variable.*;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.event.imp.Priority;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Packet0KeepAlive;
import org.lwjgl.input.Keyboard;

/**
 * @author Hoosiers
 * @credit q7e
 * @source https://github.com/qe7/Hephaestus
 * @since 04-11-2026
 */

@Hack.Info(name = "HephFreecam", category = Category.Player, bind = Keyboard.KEY_V)
public final class HephFreecam extends Hack {

    @Override
    public boolean shouldDisableOnDisconnect() {
        return true;
    }

    public final BooleanSetting copyPlayer = new BooleanSetting("Copy Player Model", true);
    public final EnumSetting highlight = new EnumSetting("Highlight", Highlight.Above_Ground);
    public final BooleanSetting showBreakProgress = new BooleanSetting("Break Progress", true);
    public final IntegerSetting speed = new IntegerSetting("Speed", 5, 1, 15);

    public double oldX;
    public double oldY;
    public double oldZ;
    private float oldYaw;
    private float oldPitch;

    private EntityPlayer fakePlayer = null;

    @Override
    public void onEnable(boolean isNull) {
        if (!isNull) {
            this.oldX = this.getPlayer().posX;
            this.oldY = this.getPlayer().posY;
            this.oldZ = this.getPlayer().posZ;
            this.oldYaw = this.getPlayer().rotationYaw;
            this.oldPitch = this.getPlayer().rotationPitch;

            if (this.copyPlayer.getValue()) {
                this.fakePlayer = new dev.tensor.misc.helpers.entity.FakePlayer(this.getWorld());

                this.fakePlayer.inventory.mainInventory[0] = this.getInventory().getCurrentItem();
                this.fakePlayer.inventory.armorInventory[0] = this.getInventory().armorInventory[0];
                this.fakePlayer.inventory.armorInventory[1] = this.getInventory().armorInventory[1];
                this.fakePlayer.inventory.armorInventory[2] = this.getInventory().armorInventory[2];
                this.fakePlayer.inventory.armorInventory[3] = this.getInventory().armorInventory[3];

                this.fakePlayer.username = this.getPlayer().username;
                this.fakePlayer.setPositionAndRotation(this.oldX, this.oldY, this.oldZ, this.oldYaw, this.oldPitch);

                this.getWorld().entityJoinedWorld(this.fakePlayer);
            }

            this.getPlayer().noClip = true;
            this.getPlayer().motionX = 0;
            this.getPlayer().motionY = 0;
            this.getPlayer().motionZ = 0;
        }
    }

    @Override
    public void onDisable(boolean isNull) {
        if (!isNull) {
            this.getPlayer().noClip = false;
            this.getPlayer().motionX = 0;
            this.getPlayer().motionY = 0;
            this.getPlayer().motionZ = 0;
            this.getPlayer().setPositionAndRotation(this.oldX, this.oldY, this.oldZ, this.oldYaw, this.oldPitch);

            if (this.fakePlayer != null) {
                this.getWorld().setEntityDead(fakePlayer);
            }
        }

        this.fakePlayer = null;
    }

    @Override
    public void onClientTick() {
        if (this.copyPlayer.getValue() && this.fakePlayer != null) {
            this.fakePlayer.inventory.mainInventory[0] = this.getInventory().getCurrentItem();
            this.fakePlayer.motionX = 0;
            this.fakePlayer.motionY = 0;
            this.fakePlayer.motionZ = 0;
        }

        this.getPlayer().noClip = true;
        this.getPlayer().onGround = true;

        this.getPlayer().motionY = 0;

        if (this.getMinecraft().currentScreen != null) {
            if (this.getMinecraft().currentScreen instanceof GuiGameOver) {
                this.setEnabled(false);
                return;
            }

            InventoryMove inventoryMove = this.getHackManager().getHack(InventoryMove.class);

            if (!(inventoryMove.isEnabled() && !inventoryMove.shouldNotMove())) {
                this.setPlayerSpeed(0.0D);
                return;
            }
        }

        if (Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindJump.keyCode)) {
            this.getPlayer().motionY += (this.speed.getValue() / 10.0D);
        } else if (Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindSneak.keyCode)) {
            this.getPlayer().motionY -= (this.speed.getValue() / 10.0D);
        }

        if (this.isPlayerMoving()) {
            this.setPlayerSpeed((this.speed.getValue() / 10.0D));
        } else {
            this.setPlayerSpeed(0.0D);
        }
    }

    @SuppressWarnings("unused")
    @EventTarget(priority = Priority.HIGHEST)
    public void onPacket10FlyingEvent(Packet10FlyingEvent event) {
        event.cancel();
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onRenderOverlayEvent(RenderOverlayEvent event) {
        if (event.type.equals(RenderOverlayEvent.Type.blocks) || event.type.equals(RenderOverlayEvent.Type.water)) {
            event.cancel();
        }
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onVelocityEvent(VelocityEvent event) {
        if (event.type.equals(VelocityEvent.Type.Block) || event.type.equals(VelocityEvent.Type.Fluid)) {
            event.cancel();
        }
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onUpdateClientPlayerActionEvent(UpdateClientPlayerActionEvent event) {
        this.sendPacket(new Packet0KeepAlive());
        event.cancel();
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onChangeWorldEvent(ChangeWorldEvent event) {
        this.setEnabled(false);
    }

    @SuppressWarnings("unused")
    @EventTarget(priority = Priority.HIGHEST)
    public void onRenderSelectionBoxEvent(RenderSelectionBoxEvent event) {
        switch ((Highlight) this.highlight.getValue()) {
            case None:
                event.cancel();
                break;
            case Normal:
                break;
            case Above_Ground:
                if (this.isInSelectionResult(event.positionToRender)) {
                    event.cancel();
                }
                break;
        }
    }

    @SuppressWarnings("unused")
    @EventTarget(priority = Priority.HIGHEST)
    public void onRenderBlockBreakEvent(RenderBlockBreakEvent event) {
        if (!this.showBreakProgress.getValue()) {
            event.cancel();
        }
    }

    private boolean isInSelectionResult(MovingObjectPosition position) {
        boolean inX = false;
        boolean inY = false;
        boolean inZ = false;

        if (this.getPlayer().posX >= position.blockX - 0.5 && this.getPlayer().posX <= position.blockX + 1.5) {
            inX = true;
        }

        if (this.getPlayer().posY >= position.blockY - 0.5 && this.getPlayer().posY <= position.blockY + 1.5) {
            inY = true;
        } else if (this.getPlayer().posY + 1 >= position.blockY - 0.5 && this.getPlayer().posY + 1 <= position.blockY + 1.5) {
            inY = true;
        }

        if (this.getPlayer().posZ >= position.blockZ - 0.5 && this.getPlayer().posZ <= position.blockZ + 1.5) {
            inZ = true;
        }

        return inX && inY && inZ;
    }

    private boolean isPlayerMoving() {
        return this.getPlayer().motionX != 0 || this.getPlayer().motionZ != 0;
    }

    private void setPlayerSpeed(double moveSpeed) {
        float yaw = this.getPlayer().rotationYaw;

        float strafeSpeed = this.getPlayer().movementInput.moveStrafe;
        float forwardSpeed = this.getPlayer().movementInput.moveForward;

        if (forwardSpeed != 0.0F) {

            if (strafeSpeed > 0.0F) {
                yaw += ((forwardSpeed > 0.0F) ? -45 : 45);

            } else if (strafeSpeed < 0.0F) {
                yaw += ((forwardSpeed > 0.0F) ? 45 : -45);

            }

            strafeSpeed = 0.0F;

            if (forwardSpeed > 0.0F) {
                forwardSpeed = 1.0F;

            } else if (forwardSpeed < 0.0F) {
                forwardSpeed = -1.0F;

            }
        }

        if (strafeSpeed > 0.0F) {
            strafeSpeed = 1.0F;

        } else if (strafeSpeed < 0.0F) {
            strafeSpeed = -1.0F;

        }

        double motionX = Math.cos(Math.toRadians((yaw + 90.0F)));
        double motionZ = Math.sin(Math.toRadians((yaw + 90.0F)));

        this.getPlayer().motionX = forwardSpeed * moveSpeed * motionX + strafeSpeed * moveSpeed * motionZ;
        this.getPlayer().motionZ = forwardSpeed * moveSpeed * motionZ - strafeSpeed * moveSpeed * motionX;
    }

    private enum Highlight {
        Normal,
        None,
        Above_Ground
    }
}
