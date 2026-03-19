package dev.tensor.misc.imp.setting.settings;

import dev.tensor.misc.imp.setting.Setting;

/**
 * @author Hoosiers
 * @since 04-12-2021
 */

public class BooleanSetting implements Setting<Boolean> {

    private final String NAME;
    private final boolean DEFAULT_VALUE;

    private boolean VALUE;

    public BooleanSetting(String NAME, boolean VALUE) {
        this.NAME = NAME;
        this.DEFAULT_VALUE = VALUE;

        this.VALUE = VALUE;
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Override
    public Boolean getDefaultValue() {
        return this.DEFAULT_VALUE;
    }

    @Override
    public Boolean getValue() {
        return this.VALUE;
    }

    @Override
    public void setValue(Boolean VALUE) {
        this.VALUE = VALUE;
    }
}
