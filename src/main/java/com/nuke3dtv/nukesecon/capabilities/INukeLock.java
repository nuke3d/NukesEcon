package com.nuke3dtv.nukesecon.capabilities;

public interface INukeLock {

    void setKeyCode(int newCode);
    int getKeyCode();
    void setLocked(boolean inLocked); // setLocked on a key means KeyCode cannot be overwritten, on a lock means it is locked
    boolean getLocked();

    // A key set as Skeleton can open any lock it fits
    // A lock set Skeleton can only be opened by a Skeleton key
    void setSkeleton(boolean inSkeleton);
    boolean getSkeleton();

    // It is recommended that implementations also have a boolean testLock(INukelock inLock) overload as well,
    // but just having the basic testLock will suffice
    boolean testLock(int inCode);
}
