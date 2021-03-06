package com.nuke3dtv.nukesecon.data;

public interface INukeLock {

    void setKeyCode(int newCode);
    int getKeyCode();
    int getInverseOf(int inCode);
    void setLocked(boolean inLocked);
    boolean getLocked();
    void setSkeleton(boolean inSkeleton);
    boolean getSkeleton();
}
