package dev.tensor.misc.imp.setting.settings;

import dev.tensor.misc.imp.setting.Setting;

/**
 * @author Hoosiers
 * @since 05-20-2021
 */

public class EnumSetting implements Setting<Enum<?>> {

    private final String NAME;
    private final Enum<?> DEFAULT_VALUE;

    private Enum<?> VALUE;

    public EnumSetting(String NAME, Enum<?> VALUE) {
        this.NAME = NAME;
        this.DEFAULT_VALUE = VALUE;

        this.VALUE = VALUE;
    }

    @Override
    public String getName() {
        return this.NAME;
    }

    @Override
    public Enum<?> getDefaultValue() {
        return this.DEFAULT_VALUE;
    }

    @Override
    public Enum<?> getValue() {
        return this.VALUE;
    }

    @Override
    public void setValue(Enum<?> VALUE) {
        this.VALUE = VALUE;
    }

    public Enum<?>[] getEnumConstants() {
        return this.getValue().getDeclaringClass().getEnumConstants();
    }

    public void increment() {
        Enum<?>[] array = getValue().getDeclaringClass().getEnumConstants();
        int index = getValue().ordinal() + 1;

        if (index >= array.length) index = 0;

        this.setValue(array[index]);
    }
}
