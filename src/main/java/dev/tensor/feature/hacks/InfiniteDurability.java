package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.BlockBreakEvent;
import dev.tensor.backend.events.variable.Packet7UseEntityEvent;
import dev.tensor.backend.mixins.accessors.ItemToolAccessor;
import dev.tensor.misc.event.EventTarget;
import dev.tensor.misc.event.imp.EventPriority;
import dev.tensor.misc.helpers.block.BlockList;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import net.minecraft.src.*;

/**
 * @author Hoosiers
 * @since 04-29-2026
 * <p>
 * General notes for running this (on NoCheat servers such as 2bet2t):
 * 1. Slot position 9 is the only position that this exploit works with.
 * 2. This exploit ONLY works with damage from breaking blocks or attacking entities.
 * This means that damage like lighting flint and steel can not be bypassed.
 * 3. Slot position 9 MUST be occupied by a tool with the following:
 * a. Same type (ex: shovel -> shovel).
 * b. Greater than or equal harvest level (diamond tool must have diamond tool in slot 9;
 * wood can have gold, stone, iron, or diamond tool in slot 9).
 * c. Preferably slot 9 tool has no damage - If your slot 9 tool has 10 damage,
 * your main hand tool will take 10 damage and then not take anymore.
 */

@Hack.Info(name = "InfiniteDurability", category = Category.Player)
public final class InfiniteDurability extends Hack {
    public final BooleanSetting errorMessage = new BooleanSetting("Error Message", true);

    private final BlockList INSTANT_BLOCKS = new BlockList(BlockList.BREAK_INSTANT());
    private final BlockList BLOCKED_BLOCKS = new BlockList(BlockList.NUKER_BLOCKLIST());

    private long prevTime = 0;

    @Override
    public void onDisable(boolean isNull) {
        this.prevTime = 0;
    }

    @SuppressWarnings("unused")
    @EventTarget
    public void onPacket7UseEntityEvent(Packet7UseEntityEvent event) {
        if (event.packet.isLeftClick != 1) {
            ItemStack currentItem = this.getInventory().getCurrentItem();

            if (currentItem == null) {
                return;
            }

            if (currentItem.getItem() instanceof ItemShears) {
                boolean hasDispatched = this.runSwapProtocol(event.packet, true);

                if (hasDispatched) {
                    event.cancel();
                }
            }

            return;
        }

        boolean hasDispatched = this.runSwapProtocol(event.packet, false);

        if (hasDispatched) {
            event.cancel();
        }
    }

    @SuppressWarnings("unused")
    @EventTarget(priority = EventPriority.Lowest)
    public void onBlockBreakEvent(BlockBreakEvent event) {
        int blockId = this.getWorld().getBlockId(event.packet.xPosition, event.packet.yPosition, event.packet.zPosition);

        if (blockId == 0 || this.BLOCKED_BLOCKS.containsBlock(blockId)) {
            return;
        }

        if (event.packet.status != 2 && !this.INSTANT_BLOCKS.containsBlock(blockId)) {
            return;
        }

        boolean hasDispatched = this.runSwapProtocol(event.packet, (blockId == Block.web.blockID || blockId == Block.leaves.blockID));

        if (hasDispatched) {
            event.cancel();
        }
    }

    public boolean runSwapProtocol(Packet packet, boolean shears) {
        ItemStack currentStack = this.getInventory().getCurrentItem();

        if (currentStack == null) {
            return false;
        }

        Item currentItem = currentStack.getItem();

        if (!(currentItem instanceof ItemTool) && !(currentItem instanceof ItemSword) && !(shears && currentItem instanceof ItemShears)) {
            return false;
        }

        int prevItem = this.getInventory().currentItem;
        int mainInventoryItem = this.checkSlot9(currentItem, shears);

        if (mainInventoryItem < 9) {
            if (this.errorMessage.getValue() && System.currentTimeMillis() - this.prevTime > 5000) {
                this.sendClientMessageWithHeader(this.getColor(ChatColor.RED) + "InfiniteDurability: Will not work without a tool of the same type (and >= level) in slot 9 (top left inventory slot, you have to manually do this)! Disable this message in settings.");
                this.prevTime = System.currentTimeMillis();
            }

            return false;
        }

        this.sendPacketBypass(new Packet16BlockItemSwitch(mainInventoryItem));
        this.getInventory().currentItem = mainInventoryItem;

        this.sendPacketBypass(packet);

        this.sendPacketBypass(new Packet16BlockItemSwitch(prevItem));
        this.getInventory().currentItem = prevItem;

        return true;
    }

    private int checkSlot9(Item item, boolean shears) {
        ItemStack itemStack = this.getInventory().mainInventory[9];

        if (itemStack == null) {
            return -1;
        }

        Item item1 = itemStack.getItem();

        if (!item1.isDamagable()) {
            return -1;
        }

        boolean lookPick = item instanceof ItemPickaxe;
        boolean lookAxe = item instanceof ItemAxe;
        boolean lookShovel = item instanceof ItemSpade;
        boolean lookSword = item instanceof ItemSword;
        boolean lookShears = shears && item instanceof ItemShears;

        if (lookPick && item1 instanceof ItemPickaxe) {
            ItemPickaxe pickaxe9 = (ItemPickaxe) item1;
            ItemPickaxe packaxeH = (ItemPickaxe) item;

            if (((ItemToolAccessor) pickaxe9).tensor_getToolMaterial().getHarvestLevel() < ((ItemToolAccessor) packaxeH).tensor_getToolMaterial().getHarvestLevel()) {
                return -1;
            }

            return 9;

        } else if (lookAxe && item1 instanceof ItemAxe) {
            ItemAxe axe9 = (ItemAxe) item1;
            ItemAxe axeH = (ItemAxe) item;

            if (((ItemToolAccessor) axe9).tensor_getToolMaterial().getHarvestLevel() < ((ItemToolAccessor) axeH).tensor_getToolMaterial().getHarvestLevel()) {
                return -1;
            }

            return 9;

        } else if (lookShovel && item1 instanceof ItemSpade) {
            ItemSpade spade9 = (ItemSpade) item1;
            ItemSpade spadeH = (ItemSpade) item;

            if (((ItemToolAccessor) spade9).tensor_getToolMaterial().getHarvestLevel() < ((ItemToolAccessor) spadeH).tensor_getToolMaterial().getHarvestLevel()) {
                return -1;
            }

            return 9;

        } else if (lookSword && item1 instanceof ItemSword) {
            ItemSword sword9 = (ItemSword) item1;
            ItemSword swordH = (ItemSword) item;

            if (sword9.getDamageVsEntity(null) < swordH.getDamageVsEntity(null)) {
                return -1;
            }

            return 9;

        } else if (lookShears && item1 instanceof ItemShears) {
            return 9;

        } else {
            return -1;
        }
    }
}
