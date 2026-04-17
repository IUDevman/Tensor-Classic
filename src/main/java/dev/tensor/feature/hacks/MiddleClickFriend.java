package dev.tensor.feature.hacks;

import dev.tensor.backend.events.MouseEvent;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityOtherPlayerMP;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.MovingObjectPosition;

@Hack.Info(name = "MiddleClickFriend", category = Category.Misc)
public final class MiddleClickFriend extends Hack {

    public final BooleanSetting onlyAdd = new BooleanSetting("Only Add", true);

    @SuppressWarnings("unused")
    @EventTarget
    public void onMouseEvent(MouseEvent event) {
        if (!event.eventButtonState || event.eventButton != 2) {
            return;
        }

        MovingObjectPosition result = this.getMinecraft().objectMouseOver;

        if (result == null) {
            return;
        }

        if (!result.typeOfHit.equals(EnumMovingObjectType.ENTITY)) {
            return;
        }

        Entity entity = result.entityHit;

        if (!(entity instanceof EntityOtherPlayerMP)) {
            return;
        }

        EntityOtherPlayerMP playerMP = (EntityOtherPlayerMP) entity;

        boolean isFriend = this.getFriendManager().isFriend(playerMP);

        if (!isFriend) {
            this.getFriendManager().addFriend(playerMP);
            this.sendClientMessageWithHeader(this.getColor(ChatColor.DEFAULT) + "Added friend '" + this.getColor(ChatColor.GREEN) + playerMP.username + this.getColor(ChatColor.DEFAULT) + "' to the friends list!");
            return;
        }

        if (!this.onlyAdd.getValue()) {
            this.getFriendManager().removeFriend(playerMP);
            this.sendClientMessageWithHeader(this.getColor(ChatColor.DEFAULT) + "Removed friend '" + this.getColor(ChatColor.RED) + playerMP.username + this.getColor(ChatColor.DEFAULT) + "' from the friends list!");
        }
    }
}
