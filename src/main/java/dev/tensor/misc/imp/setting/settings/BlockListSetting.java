package dev.tensor.misc.imp.setting.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.tensor.misc.helpers.ChatColor;
import dev.tensor.misc.helpers.block.BlockList;
import dev.tensor.misc.imp.setting.Setting;
import net.minecraft.src.Block;

/**
 * @author Hoosiers
 * @since 03-27-2026
 */

public class BlockListSetting implements Setting<BlockList> {

    private final String NAME;

    private final BlockList VALUE;

    public BlockListSetting(String NAME, BlockList VALUE) {
        this.NAME = NAME;

        this.VALUE = VALUE;
    }

    @Override
    public String getValueTypeName() {
        return "BlockList";
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Deprecated
    @Override
    public BlockList getDefaultValue() {
        return null;
    }

    @Override
    public BlockList getValue() {
        return this.VALUE;
    }

    @Override
    public String getValueString(boolean color) {
        return (color ? ChatColor.AQUA.text() : "") + this.getValue().saveBlockListToString();
    }

    @Deprecated
    @Override
    public void setValue(BlockList VALUE) {

    }

    @Override
    public void setValueFromString(String string) throws Exception {
        this.getValue().loadBlockListFromString(string, true);
    }

    public void addBlock(Block block) {
        this.getValue().addToBlockList(block);
        this.hasChanged();

    }

    public void addBlock(int blockID) {
        this.getValue().addToBlockList(blockID, true);
        this.hasChanged();
    }

    public void removeBlock(Block block) {
        this.getValue().removeFromBlockList(block);
        this.hasChanged();
    }

    public void removeBlock(int blockID) {
        this.getValue().removeFromBlockList(blockID);
        this.hasChanged();
    }

    public void hasChanged() {

    }

    public boolean containsBlock(Block block) {
        return this.getValue().containsBlock(block);
    }

    public boolean containsBlock(int blockID) {
        return this.getValue().containsBlock(blockID);
    }

    @Override
    public void load(JsonElement jsonElement) {
        try {
            String value = jsonElement.getAsString();

            if (!value.equals("null")) {
                this.setValueFromString(value);
            }
        } catch (Exception ignored) {
            this.getTensorLogger().info("Failed to load BlockListSetting: " + this.getName() + "!");
        }
    }

    @Override
    public void save(JsonObject jsonObject) {

        String value = this.getValue().saveBlockListToString();

        if (value == null || value.isEmpty() || value.equals(" ") || value.equals("_")) {
            value = "null";
        }

        jsonObject.add(this.getName(), new JsonPrimitive(value));
    }

    @Override
    public void reset() {
        /* removed reset 04-08-26 */
    }
}
