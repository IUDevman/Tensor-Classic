package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.*;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.EventPriority;
import dev.tensor.misc.event.imp.modifiers.cancellable.EventVariableCancellable;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.EnumSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Packet0KeepAlive;
import org.lwjgl.input.Keyboard;

/**
 * @author Hoosiers
 * @since 04-11-2026
 */

/**
 * @credit q7e
 * @source https://github.com/qe7/Hephaestus
 */

@Hack.Info(name = "HephFreecam", category = Category.Player, bind = Keyboard.KEY_V, description = "Leave your body and freely move around the map. From Hephaestus by qe7.")
public final class HephFreecam extends Hack {

    @Override
    public boolean shouldDisableOnDisconnect() {
        return true;
    }

    @Override
    public boolean shouldDisableOnDeath() {
        return true;
    }

    @Override
    public boolean shouldDisableOnWorldChange() {
        return true;
    }

    public final BooleanSetting copyPlayer = new BooleanSetting("Copy Player Model", true, "Spawns a fake player copy of you when the hack is enabled.") {

        @Override
        public void setValue(Boolean VALUE) {
            super.setValue(VALUE);

            if (this.failsNullCheck()) {
                return;
            }

            HephFreecam freecam = this.getHackManager().getHack(HephFreecam.class);

            if (freecam != null && freecam.isEnabled()) {

                if (freecam.fakePlayer != null) {
                    this.getWorld().setEntityDead(freecam.fakePlayer);
                }

                if (VALUE) {
                    freecam.loadPlayerCopy();

                } else {
                    freecam.fakePlayer = null;
                }
            }
        }
    };
    public final BooleanSetting updateInventory = new BooleanSetting("Update Inventory", true, "Allows for your inventory to be modified when in freecam.");
    public final EnumSetting highlight = new EnumSetting("Highlight", Highlight.Above_Ground, new String[]{
            "This is the logic uses to block the selection box.",
            "Normal® No blocking.",
            "None® Selection box will never show",
            "Above Ground® Selection box will show for surface blocks."
    });
    public final BooleanSetting showBreakProgress = new BooleanSetting("Break Progress", true, "Render vanilla break progress when in freecam.");
    public final IntegerSetting speed = new IntegerSetting("Speed", 5, 1, 15, "Blocks Per Second", "How fast you should move in Freecam.");

    public double oldX;
    public double oldY;
    public double oldZ;

    public float oldYaw;
    private float oldPitch;

    private boolean isThirdPerson;

    public EntityPlayer fakePlayer = null;

    @Override
    public void onEnable(boolean isNull) {
        if (!isNull) {
            this.oldX = this.getPlayer().posX;
            this.oldY = this.getPlayer().posY;
            this.oldZ = this.getPlayer().posZ;
            this.oldYaw = this.getPlayer().rotationYaw;
            this.oldPitch = this.getPlayer().rotationPitch;
            this.isThirdPerson = this.getMinecraft().gameSettings.thirdPersonView;

            if (this.copyPlayer.getValue()) {
                this.loadPlayerCopy();
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
            this.getMinecraft().gameSettings.thirdPersonView = this.isThirdPerson;

            if (this.fakePlayer != null) {
                this.getWorld().setEntityDead(this.fakePlayer);
            }
        }

        this.fakePlayer = null;
    }

    @Override
    public void onClientTick() {
        if (this.copyPlayer.getValue() && this.fakePlayer != null) {
            this.fakePlayer.inventory.currentItem = this.getInventory().currentItem;
            this.fakePlayer.inventory.mainInventory[this.getInventory().currentItem] = this.getInventory().getCurrentItem();
            this.fakePlayer.inventory.mainInventory[9] = this.getInventory().mainInventory[9];
            this.fakePlayer.motionX = 0;
            this.fakePlayer.motionY = 0;
            this.fakePlayer.motionZ = 0;
        }

        this.getMinecraft().gameSettings.thirdPersonView = false;

        this.getPlayer().noClip = true;
        this.getPlayer().onGround = true;

        this.getPlayer().motionY = 0;

        if (this.getMinecraft().currentScreen != null) {
            InventoryMove inventoryMove = this.getHackManager().getHack(InventoryMove.class);

            if (!(inventoryMove.isEnabled() && !inventoryMove.shouldNotMove())) {
                this.setPlayerFlyingSpeed(0.0D);
                return;
            }
        }

        if (Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindJump.keyCode)) {
            this.getPlayer().motionY += (this.speed.getValue() / 10.0D);
        } else if (Keyboard.isKeyDown(this.getMinecraft().gameSettings.keyBindSneak.keyCode)) {
            this.getPlayer().motionY -= (this.speed.getValue() / 10.0D);
        }

        if (this.isPlayerMoving2D()) {
            this.setPlayerFlyingSpeed((this.speed.getValue() / 10.0D));
        } else {
            this.setPlayerFlyingSpeed(0.0D);
        }
    }

    @SuppressWarnings("unused")
    private final EventAction<Packet10FlyingEvent> packet10FlyingEventAction = this.addHackEventAction(new EventAction<>(Packet10FlyingEvent.class, EventPriority.Highest, EventVariableCancellable::cancel));

    @SuppressWarnings("unused")
    private final EventAction<RenderOverlayEvent> renderOverlayEventAction = this.addHackEventAction(new EventAction<>(RenderOverlayEvent.class, event -> {
        if (event.type.equals(RenderOverlayEvent.Type.BLOCK) || event.type.equals(RenderOverlayEvent.Type.WATER)) {
            event.cancel();
        }
    }));

    @SuppressWarnings("unused")
    private final EventAction<VelocityEvent> velocityEventAction = this.addHackEventAction(new EventAction<>(VelocityEvent.class, event -> {
        if (event.type.equals(VelocityEvent.Type.BLOCK) || event.type.equals(VelocityEvent.Type.FLUID)) {
            event.cancel();
        }
    }));

    @SuppressWarnings("unused")
    private final EventAction<UpdateClientPlayerActionEvent> updateClientPlayerActionEventAction = this.addHackEventAction(new EventAction<>(UpdateClientPlayerActionEvent.class, event -> {
        this.sendPacket(new Packet0KeepAlive());
        event.shouldUpdateInventory = this.updateInventory.getValue();
        event.cancel();
    }));

    @SuppressWarnings("unused")
    private final EventAction<RenderSelectionBoxEvent> renderSelectionBoxEventAction = this.addHackEventAction(new EventAction<>(RenderSelectionBoxEvent.class, EventPriority.Highest, event -> {
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
    }));

    @SuppressWarnings("unused")
    private final EventAction<RenderBlockBreakEvent> renderBlockBreakEventAction = this.addHackEventAction(new EventAction<>(RenderBlockBreakEvent.class, EventPriority.Highest, event -> {
        if (!this.showBreakProgress.getValue()) {
            event.cancel();
        }
    }));

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

    public void loadPlayerCopy() {
        this.fakePlayer = new dev.tensor.misc.helpers.entity.FakePlayer(this.getWorld());

        this.fakePlayer.inventory.currentItem = this.getInventory().currentItem;
        this.fakePlayer.inventory.mainInventory[this.getInventory().currentItem] = this.getInventory().getCurrentItem();
        this.fakePlayer.inventory.mainInventory[9] = this.getInventory().mainInventory[9];
        this.fakePlayer.inventory.armorInventory[0] = this.getInventory().armorInventory[0];
        this.fakePlayer.inventory.armorInventory[1] = this.getInventory().armorInventory[1];
        this.fakePlayer.inventory.armorInventory[2] = this.getInventory().armorInventory[2];
        this.fakePlayer.inventory.armorInventory[3] = this.getInventory().armorInventory[3];

        this.fakePlayer.username = this.getPlayer().username;
        this.fakePlayer.setPositionAndRotation(this.oldX, this.oldY, this.oldZ, this.oldYaw, this.oldPitch);
        this.fakePlayer.skinUrl = this.getPlayer().skinUrl;

        this.getWorld().entityJoinedWorld(this.fakePlayer);
    }

    private enum Highlight {
        Normal,
        None,
        Above_Ground
    }
}
