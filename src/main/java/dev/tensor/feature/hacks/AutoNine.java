package dev.tensor.feature.hacks;

import dev.tensor.backend.events.variable.AttackEntityEvent;
import dev.tensor.backend.events.variable.BlockBreakEvent;
import dev.tensor.backend.mixins.accessors.ItemToolAccessor;
import dev.tensor.backend.mixins.accessors.PlayerControllerMPAccessor;
import dev.tensor.misc.event.EventAction;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.helpers.block.BlockList;
import dev.tensor.misc.imp.hack.Category;
import dev.tensor.misc.imp.hack.Hack;
import dev.tensor.misc.imp.setting.settings.BooleanSetting;
import net.minecraft.src.*;

/**
 * @author Hoosiers
 * @since 06-02-2026
 */

/**
 * NOTE:
 * This implementation never damages your tools and only requires one set compared to InfiniteDurability.
 * So it is much better, but cannot be run with the following hacks:
 * AutoSword
 * AutoTool
 * InfiniteDurability
 */

@Hack.Info(name = "AutoNine", category = Category.Player)
public final class AutoNine extends Hack {

    public final BooleanSetting warning = new BooleanSetting("Warning Message", true);
    public final BooleanSetting doubleCheck = new BooleanSetting("Double Check", true);
    public final BooleanSetting swapBack = new BooleanSetting("Swap Hand Back", false);
    public final BooleanSetting swap = new BooleanSetting("Swap Items To 9", true);

    private final BlockList INSTANT_BLOCKS = new BlockList(BlockList.BREAK_INSTANT());
    private final BlockList BLOCKED_BLOCKS = new BlockList(BlockList.NUKER_BLOCKLIST());

    private boolean shouldWarn = false;
    private boolean shouldSwapBack = false;

    private int prevItem = -1;

    @Override
    public void onEnable(boolean isNull) {
        this.prevItem = -1;

        if (isNull) {
            this.shouldWarn = true;
            return;
        }

        this.sendWarningMessage();
    }

    @Override
    public void onClientTick() {
        if (this.shouldWarn) {
            this.sendWarningMessage();
            this.shouldWarn = false;
        }

        if (this.swapBack.getValue() && this.shouldSwapBack && this.prevItem != -1) {

            //You really don't want to swap back to a tool, so we will block this.
            //Prevents rare bugs where your tool is damaged.
            ItemStack prevItem = this.getInventory().getStackInSlot(this.prevItem);

            if (prevItem != null && prevItem.isItemStackDamageable()) {
                this.prevItem = -1;
                return;
            }

            this.getInventory().currentItem = this.prevItem;
            this.prevItem = -1;
        }

        //The following is kinda a hacky method but this fixes a glitch where the right tool will not
        //be selected if breaking really fast.
        //I think this is due to packets being out of order (???) or not sending a packet with status 2.
        //Essentially this glitch only really happens when breaking lots of blocks that need different tools.
        //You can test this by breaking a pillar of alternating dirt and cobblestone with fast tools and speedmine(s).
        if (!this.swap.getValue() || this.swap.getValue() && !this.doubleCheck.getValue()) {
            return;
        }

        if (this.getInventory().currentItem != 9) {
            return;
        }

        MovingObjectPosition position = this.getMinecraft().objectMouseOver;

        if (position == null) {
            return;
        }

        if (position.typeOfHit != EnumMovingObjectType.TILE) {
            return;
        }

        int blockID = this.getWorld().getBlockId(position.blockX, position.blockY, position.blockZ);

        if (this.badBlockID(blockID)) {
            return;
        }

        Block block = Block.blocksList[blockID];

        if (block == null) {
            return;
        }

        if (this.BLOCKED_BLOCKS.containsBlock(block)) {
            return;
        }

        if (this.INSTANT_BLOCKS.containsBlock(block)) {
            return;
        }

        PlayerController playerController = this.getMinecraft().playerController;

        if (playerController instanceof PlayerControllerMP) {
            PlayerControllerMP playerControllerMP = (PlayerControllerMP) playerController;

            if (((PlayerControllerMPAccessor) playerControllerMP).tensor_getCurBlockDamageMP() > 0F) {
                this.swapAndFindBestBlock(block, blockID);
            }
        }
    }

    @SuppressWarnings("unused")
    EventAction<AttackEntityEvent> attackEntityEventAction = this.addEventAction(new EventAction<>(AttackEntityEvent.class, event -> {
        this.swapTo9(true);

        if (!this.swap.getValue()) {
            return;
        }

        int swapSlot = 9;
        float currentDamage = 0;

        ItemStack currentItem = this.getInventory().getStackInSlot(swapSlot);

        if (currentItem != null) {
            currentDamage = currentItem.getDamageVsEntity(null);
        }

        int oldSlot = swapSlot;

        for (int i = 9; i < 36; i++) {

            ItemStack itemStack = this.getInventory().getStackInSlot(i);

            if (itemStack == null) {
                continue;
            }

            float damage = itemStack.getDamageVsEntity(null);

            if (damage > currentDamage) {
                currentDamage = damage;
                swapSlot = i;
            }
        }

        if (swapSlot != oldSlot) {
            this.putItemIn9(swapSlot);
        }
    }));

    @SuppressWarnings("unused")
    EventAction<BlockBreakEvent> blockBreakEventAction = this.addEventAction(new EventAction<>(BlockBreakEvent.class, event -> {
        Packet14BlockDig packet = event.packet;

        if (packet.status == 2) {
            this.shouldSwapBack = true;
            return;
        }

        int blockId = this.getWorld().getBlockId(packet.xPosition, packet.yPosition, packet.zPosition);

        if (this.badBlockID(blockId)) {
            return;
        }

        Block block = Block.blocksList[blockId];

        if (this.BLOCKED_BLOCKS.containsBlock(block)) {
            return;
        }

        this.swapTo9(false);

        if (this.INSTANT_BLOCKS.containsBlock(block)) {
            return;
        }

        if (!this.swap.getValue()) {
            return;
        }

        this.swapAndFindBestBlock(block, blockId);
    }));

    private boolean badBlockID(int blockID) {
        if (blockID == 0) {
            return true;
        }

        if (blockID == Block.tnt.blockID) {
            ItemStack currentItem = this.getInventory().getCurrentItem();

            if (currentItem != null && currentItem.getItem() instanceof ItemFlintAndSteel) {
                return true;
            }
        }

        return false;
    }

    public void swapAndFindBestBlock(Block block, int blockId) {
        int swapSlot = 9;
        float currentStrength = 0;

        ItemStack currentItem = this.getInventory().getStackInSlot(swapSlot);

        if (currentItem != null) {
            if (currentItem.getItem() == Item.flintAndSteel && blockId == Block.tnt.blockID) {
                return;
            }

            if (currentItem.getItem() instanceof ItemTool) {
                currentStrength = currentItem.getItem().getStrVsBlock(currentItem, block);
            } else {
                currentStrength = currentItem.getStrVsBlock(block);
            }
        }

        int oldSlot = swapSlot;

        for (int i = 9; i < 36; i++) {

            ItemStack itemStack = this.getInventory().getStackInSlot(i);

            if (itemStack == null) {
                continue;
            }

            float strength;

            if (itemStack.getItem() instanceof ItemTool) {
                strength = itemStack.getItem().getStrVsBlock(itemStack, block);
            } else {
                strength = itemStack.getStrVsBlock(block);
            }

            if (itemStack.getItem() instanceof ItemPickaxe && this.isBlockOnBullshitListForSwap(blockId)) {
                ItemTool tool = (ItemTool) itemStack.getItem();

                strength = 10 + ((ItemToolAccessor) tool).tensor_getToolMaterial().getHarvestLevel();
            }

            if (strength > currentStrength) {
                currentStrength = strength;
                swapSlot = i;
            }
        }

        if (swapSlot != oldSlot) {
            this.putItemIn9(swapSlot);
        }
    }

    private void swapTo9(boolean swapBack) {
        int current = this.getInventory().currentItem;

        if (current != 9) {
            this.prevItem = current;
            this.getInventory().currentItem = 9;
            this.shouldSwapBack = swapBack;
        }
    }

    private void putItemIn9(int newPosition) {
        ItemStack[] mainInventory = this.getInventory().mainInventory;

        ItemStack from9 = mainInventory[9];
        ItemStack toSwap = mainInventory[newPosition];

        Packet102WindowClick click1 = new Packet102WindowClick(0, newPosition, 0, false, toSwap, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
        this.sendPacket(click1);
        this.getInventory().mainInventory[newPosition] = null;

        Packet102WindowClick click2 = new Packet102WindowClick(0, 9, 0, false, from9, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
        this.sendPacket(click2);
        this.getInventory().mainInventory[9] = toSwap;

        if (from9 != null) {
            Packet102WindowClick click3 = new Packet102WindowClick(0, newPosition, 0, false, from9, this.getPlayer().inventorySlots.func_20111_a(this.getInventory()));
            this.sendPacket(click3);
            this.getInventory().mainInventory[newPosition] = from9;
        }
    }

    private void sendWarningMessage() {
        if (!this.warning.getValue()) {
            return;
        }

        this.sendClientMessageWithHeader(ChatColor.RED.text() + "AutoNine: Hack breaks some enabled hacks - AutoSword, AutoTool, and InfiniteDurability!");
    }
}
