package com.nuke3dtv.nukesecon.capabilities;

import com.nuke3dtv.nukesecon.capabilities.nukelock.CapabilityNukeLock;
import com.nuke3dtv.nukesecon.capabilities.nukelock.DefaultNukeLock;
import com.nuke3dtv.nukesecon.capabilities.nukelock.INukeLock;
import com.nuke3dtv.nukesecon.capabilities.nukevalue.CapabilityNukeValue;
import com.nuke3dtv.nukesecon.capabilities.nukevalue.DefaultNukeValue;
import com.nuke3dtv.nukesecon.capabilities.nukevalue.INukeValue;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/* This class is a capability provider for items that need both the value and lock providers */
public class NukeLockValueProvider implements ICapabilitySerializable<CompoundNBT> {
    private final DefaultNukeLock lock = new DefaultNukeLock();
    private final LazyOptional<INukeLock> lockOptional = LazyOptional.of(() -> lock);

    private final DefaultNukeValue nukevalue = new DefaultNukeValue();
    private final LazyOptional<INukeValue> valueOptional = LazyOptional.of(() -> nukevalue);

    public void invalidate() {
        lockOptional.invalidate();
        valueOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityNukeLock.NUKELOCK_CAPABILITY) { return lockOptional.cast(); }
        if (cap == CapabilityNukeValue.NUKEVALUE_CAPABILITY) { return valueOptional.cast(); }
        return null;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT outNBT = new CompoundNBT();
        if (CapabilityNukeLock.NUKELOCK_CAPABILITY != null) {
            outNBT.merge((CompoundNBT) CapabilityNukeLock.NUKELOCK_CAPABILITY.writeNBT(lock, null));
        }
        if (CapabilityNukeValue.NUKEVALUE_CAPABILITY != null) {
            outNBT.merge((CompoundNBT) CapabilityNukeValue.NUKEVALUE_CAPABILITY.writeNBT(nukevalue, null));
        }
        return outNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (CapabilityNukeLock.NUKELOCK_CAPABILITY != null) {
            CapabilityNukeLock.NUKELOCK_CAPABILITY.readNBT(lock, null, nbt);
        }
        if (CapabilityNukeValue.NUKEVALUE_CAPABILITY != null) {
            CapabilityNukeValue.NUKEVALUE_CAPABILITY.readNBT(nukevalue, null, nbt);
        }
    }

}
