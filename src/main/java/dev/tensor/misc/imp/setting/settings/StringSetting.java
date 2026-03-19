package dev.tensor.misc.imp.setting.settings;

import dev.tensor.misc.imp.setting.Setting;

/**
 * @author Hoosiers
 * @since 03-18-2026
 */

public class StringSetting implements Setting<String> {

    private final String NAME;
    private final String DEFAULT_VALUE;

    private String VALUE;

    public StringSetting(String NAME, String VALUE) {
        this.NAME = NAME;
        this.DEFAULT_VALUE = VALUE;

        this.VALUE = VALUE;
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Override
    public String getDefaultValue() {
        return this.DEFAULT_VALUE;
    }

    @Override
    public String getValue() {
        return this.VALUE;
    }

    @Override
    public void setValue(String VALUE) {
        this.VALUE = VALUE;
    }
}
