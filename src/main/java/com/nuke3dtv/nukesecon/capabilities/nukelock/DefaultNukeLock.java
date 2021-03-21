package com.nuke3dtv.nukesecon.capabilities.nukelock;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Random;

public class DefaultNukeLock implements INukeLock, INBTSerializable<CompoundNBT> {
    private int keycode;
    private boolean locked;
    private boolean skeleton;

    private Random ran = new Random();

    public DefaultNukeLock() {
        setKeyCode(generateKeyCode());
    }
    public DefaultNukeLock(int inKeyCode) {
        setKeyCode(inKeyCode);
    }

    @Override
    public int generateKeyCode() {
        // A zero keycode will never be generated with this code
        // This can be useful because forcing a keycode of zero on a key will make a key that will never unlock anything
        return ran.nextInt(1000000000)+1;
    }

    @Override
    public void setKeyCode(int inCode) {
        this.keycode = inCode;
    }

    @Override
    public int getKeyCode() { return this.keycode; }

    @Override
    public void setLocked(boolean inLocked) {
        locked = inLocked;
    }

    @Override
    public boolean getLocked() {
        return locked;
    }

    @Override
    public void setSkeleton(boolean inSkeleton) {
        skeleton = inSkeleton;
    }

    @Override
    public boolean getSkeleton() {
        return skeleton;
    }

    @Override
    public boolean testLock(INukeLock inLock) {
        if (inLock.getKeyCode() == keycode) return true;
        return false;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("keycode", getKeyCode());
        tag.putBoolean("locked", getLocked());
        tag.putBoolean("skeleton", getSkeleton());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setKeyCode(nbt.getInt("keycode"));
        setLocked(nbt.getBoolean("locked"));
        setSkeleton(nbt.getBoolean("skeleton"));
    }
    public int getInverseOf(int inCode) {
        int invKey = 0;
        int subAmt = 0;
        int compVal = 100000000;
        for (int i=1; i <= 9; i++) {
            int compVal1 = (inCode - subAmt) / compVal;
            invKey += ((9 - compVal1) * compVal);
            subAmt += (compVal1 * compVal);
            compVal = compVal / 10;
        }
        return invKey;
    }

}
