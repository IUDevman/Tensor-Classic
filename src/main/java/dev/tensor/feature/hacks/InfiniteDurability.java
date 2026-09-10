package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.BlockBreakPacketEvent;
import dev.tensor.backend.events.variable.Packet7UseEntityEvent;
import dev.tensor.backend.mixins.accessors.ItemToolAccessor;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.event.imp.EventPriority;
import dev.tensor.misc.helpers.text.ChatColor;
import dev.tensor.misc.helpers.block.BlockList;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import net.minecraft.src.*;

/**
 * @author Hoosiers
 * @since 04-29-2026
 */

/**
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
 * 4. The Swap Slot 9 setting switches the swap slot to the desired tool if found, but doesn't filter by damage.
 * Put your least damaged tools closer to slot 9.
 * 5. This hack works with (Tensor) Nuker, AutoTool, and AutoSword.
 * 6. Your tools do not take damage, but most times you can see-client side damage (more apparent with lower durability
 * tools). This resets randomly when mining or when moving the item/switching tools.
 */

/**
 * Replaced by Auto9 Hack.
 * Depreciated 06/21/2026
 */

@Deprecated
@Hack.Info(name = "InfiniteDurability", category = Category.Player)
public final class InfiniteDurability extends Hack {

    public final BooleanSetting errorMessage = new BooleanSetting("Error Message", true);
    public final BooleanSetting swapTo9 = new BooleanSetting("Swap Slot 9", true);

    private final BlockList BLOCKED_BLOCKS = new BlockList(BlockList.NUKER_BLOCKLIST());

    private long prevTime = 0;

    @Override
    public void onDisable(boolean isNull) {
        this.prevTime = 0;
    }

    @SuppressWarnings("unused")
    private final EventAction<Packet7UseEntityEvent> packet7UseEntityEventAction = this.addEventAction(new EventAction<>(Packet7UseEntityEvent.class, event -> {
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
    }));

    @SuppressWarnings("unused")
    private final EventAction<BlockBreakPacketEvent> blockBreakPacketEventAction = this.addEventAction(new EventAction<>(BlockBreakPacketEvent.class, EventPriority.Lowest, event -> {
        int blockId = this.getWorld().getBlockId(event.packet.xPosition, event.packet.yPosition, event.packet.zPosition);

        if (blockId == 0 || this.BLOCKED_BLOCKS.containsBlock(blockId)) {
            return;
        }

        boolean hasDispatched = this.runSwapProtocol(event.packet, (blockId == Block.web.blockID || blockId == Block.leaves.blockID));

        if (hasDispatched) {
            event.cancel();
        }
    }));

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
                this.sendClientMessageWithHeader(ChatColor.RED.text() + "InfiniteDurability: Will not work without a tool of the same type (and >= level) in slot 9 (top left inventory slot)! Disable this message in settings.");
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
        boolean lookPick = item instanceof ItemPickaxe;
        boolean lookAxe = item instanceof ItemAxe;
        boolean lookShovel = item instanceof ItemSpade;
        boolean lookSword = item instanceof ItemSword;
        boolean lookShears = shears && item instanceof ItemShears;

        ItemStack itemStackOld = this.getInventory().mainInventory[9];
        Item item1;

        int returnVal = -1;

        if (itemStackOld == null) {
            if (lookPick) {
                returnVal = this.searchAndSwapTo9(ItemPickaxe.class, ((ItemToolAccessor) item).tensor_getToolMaterial().getHarvestLevel());

            } else if (lookAxe) {
                returnVal = this.searchAndSwapTo9(ItemAxe.class, ((ItemToolAccessor) item).tensor_getToolMaterial().getHarvestLevel());

            } else if (lookShovel) {
                returnVal = this.searchAndSwapTo9(ItemSpade.class, ((ItemToolAccessor) item).tensor_getToolMaterial().getHarvestLevel());

            } else if (lookSword) {
                returnVal = this.searchAndSwapTo9(ItemSword.class, item.getDamageVsEntity(null));

            } else if (lookShears) {
                returnVal = this.searchAndSwapTo9(ItemShears.class, -1);
            }

            if (returnVal == -1) {
                return -1;
            }
        }

        if (returnVal != -1) {
            return 9;
        } else {
            item1 = itemStackOld.getItem();
        }

        if (lookPick && item1 instanceof ItemPickaxe) {
            ItemPickaxe pickaxe9 = (ItemPickaxe) item1;
            ItemPickaxe packaxeH = (ItemPickaxe) item;

            if (((ItemToolAccessor) pickaxe9).tensor_getToolMaterial().getHarvestLevel() < ((ItemToolAccessor) packaxeH).tensor_getToolMaterial().getHarvestLevel()) {
                return this.searchAndSwapTo9(ItemPickaxe.class, ((ItemToolAccessor) packaxeH).tensor_getToolMaterial().getHarvestLevel());
            }

            return 9;

        } else if (lookAxe && item1 instanceof ItemAxe) {
            ItemAxe axe9 = (ItemAxe) item1;
            ItemAxe axeH = (ItemAxe) item;

            if (((ItemToolAccessor) axe9).tensor_getToolMaterial().getHarvestLevel() < ((ItemToolAccessor) axeH).tensor_getToolMaterial().getHarvestLevel()) {
                return this.searchAndSwapTo9(ItemAxe.class, ((ItemToolAccessor) axeH).tensor_getToolMaterial().getHarvestLevel());
            }

            return 9;

        } else if (lookShovel && item1 instanceof ItemSpade) {
            ItemSpade spade9 = (ItemSpade) item1;
            ItemSpade spadeH = (ItemSpade) item;

            if (((ItemToolAccessor) spade9).tensor_getToolMaterial().getHarvestLevel() < ((ItemToolAccessor) spadeH).tensor_getToolMaterial().getHarvestLevel()) {
                return this.searchAndSwapTo9(ItemSpade.class, ((ItemToolAccessor) spadeH).tensor_getToolMaterial().getHarvestLevel());
            }

            return 9;

        } else if (lookSword && item1 instanceof ItemSword) {
            ItemSword sword9 = (ItemSword) item1;
            ItemSword swordH = (ItemSword) item;

            if (sword9.getDamageVsEntity(null) < swordH.getDamageVsEntity(null)) {
                return this.searchAndSwapTo9(ItemSword.class, swordH.getDamageVsEntity(null));
            }

            return 9;

        } else if (lookShears && item1 instanceof ItemShears) {
            return 9;

        } else {
            if (lookPick) {
                return this.searchAndSwapTo9(ItemPickaxe.class, ((ItemToolAccessor) item).tensor_getToolMaterial().getHarvestLevel());

            } else if (lookAxe) {
                return this.searchAndSwapTo9(ItemAxe.class, ((ItemToolAccessor) item).tensor_getToolMaterial().getHarvestLevel());

            } else if (lookShovel) {
                return this.searchAndSwapTo9(ItemSpade.class, ((ItemToolAccessor) item).tensor_getToolMaterial().getHarvestLevel());

            } else if (lookSword) {
                return this.searchAndSwapTo9(ItemSword.class, item.getDamageVsEntity(null));

            } else if (lookShears) {
                return this.searchAndSwapTo9(ItemShears.class, -1);

            } else {
                return -1;
            }
        }
    }

    private int searchAndSwapTo9(Class<? extends Item> classToFind, int harvestLevelToReach) {
        if (!this.swapTo9.getValue()) {
            return -1;
        }

        int startPosition = 9;
        int newPosition = startPosition;

        ItemStack[] mainInventory = this.getInventory().mainInventory;

        for (int i = startPosition; i < mainInventory.length; i++) {

            ItemStack itemStack = mainInventory[i];

            if (itemStack == null) {
                continue;
            }

            Item item = itemStack.getItem();

            if (item == null) {
                continue;
            }

            if (item.getClass() == classToFind) {

                if (item instanceof ItemShears) {
                    newPosition = i;
                    break;

                } else if (item instanceof ItemSword) {
                    ItemSword sword = (ItemSword) item;

                    if (sword.getDamageVsEntity(null) >= harvestLevelToReach) {
                        newPosition = i;
                        break;
                    }

                } else if (item instanceof ItemTool) {
                    ItemTool tool = (ItemTool) item;

                    if (((ItemToolAccessor) tool).tensor_getToolMaterial().getHarvestLevel() >= harvestLevelToReach) {
                        newPosition = i;
                        break;
                    }
                }
            }
        }

        if (newPosition != startPosition) {
            ItemStack from9 = mainInventory[startPosition];
            ItemStack toSwap = mainInventory[newPosition];

            Packet102WindowClick click1 = new Packet102WindowClick(0, newPosition, 0, false, toSwap, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
            this.sendPacket(click1);
            this.getInventory().mainInventory[newPosition] = null;

            Packet102WindowClick click2 = new Packet102WindowClick(0, startPosition, 0, false, from9, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
            this.sendPacket(click2);
            this.getInventory().mainInventory[startPosition] = toSwap;

            if (from9 != null) {
                Packet102WindowClick click3 = new Packet102WindowClick(0, newPosition, 0, false, from9, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
                this.sendPacket(click3);
                this.getInventory().mainInventory[newPosition] = from9;
            }

            return startPosition;
        }

        return -1;
    }
}
