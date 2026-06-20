package dev.tensor.feature.hacks;

import dev.tensor.backend.events.constant.ChangeWorldEvent;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import dev.tensor.misc.imp.setting.settings.IntegerSetting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet102WindowClick;

import java.util.HashMap;

/**
 * @author Hoosiers
 * @since 06-14-2026
 */

@Hack.Info(name = "HotbarRefill", category = Category.Player)
public final class HotbarRefill extends Hack {

    public final BooleanSetting screenStop = new BooleanSetting("Stop In Screens", true);
    public final IntegerSetting refreshTime = new IntegerSetting("Refresh Time", 10, 0, 20);

    private final Item[] itemsCache = new Item[9];

    private boolean needsUpdate = false;

    private int tickTime = 0;

    @Override
    public void onEnable(boolean isNull) {
        if (isNull) {
            this.needsUpdate = true;
            return;
        }

        this.resetItemCache();
        this.needsUpdate = false;
    }

    @Override
    public void onClientTick() {
        if (this.screenStop.getValue() && this.getMinecraft().currentScreen != null) {
            this.needsUpdate = true;
            return;
        }

        if (this.needsUpdate) {
            this.resetItemCache();
            this.needsUpdate = false;
            return;
        }

        ItemStack[] currentItems = this.getInventory().mainInventory;

        HashMap<Integer, Item> toUpdate = new HashMap<>();

        for (int i = 0; i < 9; i++) {
            ItemStack currentItem = currentItems[i];

            Item cachedItem = this.itemsCache[i];

            if (currentItem == null && cachedItem == null) {
                continue;
            }

            if (currentItem == null) {
                toUpdate.put(i, cachedItem);
            }
        }

        if (!toUpdate.isEmpty()) {
            this.tickTime = 0;

            toUpdate.forEach((integer, item) -> {

                for (int i = 9; i < 36; i++) {
                    ItemStack hotbarStack = this.getInventory().mainInventory[integer];

                    if (hotbarStack != null) {
                        this.itemsCache[integer] = hotbarStack.getItem();
                        break;
                    }

                    ItemStack inventoryStack = this.getInventory().mainInventory[i];

                    if (inventoryStack == null) {
                        continue;
                    }

                    Item inventoryItem = inventoryStack.getItem();

                    if (inventoryItem == item) {
                        this.swapFromInventory(integer, i);
                        break;
                    }
                }
            });
        } else {
            if (this.tickTime >= this.refreshTime.getValue()) {
                this.resetItemCache();
            } else {
                this.tickTime++;
            }
        }
    }

    @SuppressWarnings("unused")
    private final EventAction<ChangeWorldEvent> changeWorldEventAction = this.addEventAction(new EventAction<>(ChangeWorldEvent.class, event -> this.resetItemCache()));

    private void resetItemCache() {
        this.tickTime = 0;

        for (int i = 0; i < 9; i++) {
            ItemStack currentStack = this.getInventory().mainInventory[i];

            if (currentStack == null) {
                this.itemsCache[i] = null;
                continue;
            }

            this.itemsCache[i] = currentStack.getItem();
        }
    }

    private void swapFromInventory(int hotbarSlot, int inventorySlot) {
        ItemStack fromInventory = this.getInventory().mainInventory[inventorySlot];
        ItemStack inHotbar = this.getInventory().mainInventory[hotbarSlot];

        if (inHotbar != null) {
            this.itemsCache[hotbarSlot] = inHotbar.getItem();
            return;
        }

        this.sendPacket(new Packet102WindowClick(0, inventorySlot, 0, false, fromInventory, this.getPlayer().inventorySlots.func_20111_a(this.getInventory())));
        this.sendPacket(new Packet102WindowClick(0, hotbarSlot + 36, 0, false, null, (short) (this.getPlayer().inventorySlots.func_20111_a(this.getInventory()))));

        if (!this.isMultiplayer()) {
            this.getInventory().mainInventory[hotbarSlot] = fromInventory;
            this.getInventory().mainInventory[inventorySlot] = null;
        }
    }
}
