package com.nuke3dtv.nukesecon.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NukeLockProvider implements ICapabilitySerializable<CompoundNBT> {

    private final DefaultNukeLock lock = new DefaultNukeLock();
    private final LazyOptional<INukeLock> lockOptional = LazyOptional.of(() -> lock);

    public void invalidate() {
        lockOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return lockOptional.cast();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (CapabilityNukeLock.NUKELOCK_CAPABILITY == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) CapabilityNukeLock.NUKELOCK_CAPABILITY.writeNBT(lock, null);
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (CapabilityNukeLock.NUKELOCK_CAPABILITY != null) {
            CapabilityNukeLock.NUKELOCK_CAPABILITY.readNBT(lock, null, nbt);
        }
    }
}
