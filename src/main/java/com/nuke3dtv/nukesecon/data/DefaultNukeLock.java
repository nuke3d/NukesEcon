package com.nuke3dtv.nukesecon.data;

public class DefaultNukeLock implements INukeLock {
    private int keycode;

    @Override
    public void setKeyCode(int inCode) { this.keycode = inCode; }

    @Override
    public int getKeyCode() { return this.keycode; }

}
