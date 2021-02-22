package com.nuke3dtv.nukesecon.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityNukeLock {

    @CapabilityInject(INukeLock.class)
    public static Capability<INukeLock> NUKELOCK_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(INukeLock.class, new Storage(), DefaultNukeLock::new);
    }

    public static class Storage implements Capability.IStorage<INukeLock> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<INukeLock> capability, INukeLock instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("keycode", instance.getKeyCode());
            return tag;
        }

        @Override
        public void readNBT(Capability<INukeLock> capability, INukeLock instance, Direction side, INBT nbt) {
            int keycode = ((CompoundNBT) nbt).getInt("keycode");
            instance.setKeyCode(keycode);
        }
    }

}
