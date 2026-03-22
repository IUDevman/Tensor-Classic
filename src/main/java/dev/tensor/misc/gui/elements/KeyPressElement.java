package dev.tensor.misc.gui.elements;

public interface KeyPressElement {

    boolean isSearching();

    void setSearching(boolean searching);

    void onKeyPressed(int key);
}
