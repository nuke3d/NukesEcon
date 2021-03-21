package com.nuke3dtv.nukesecon.capabilities.nukevalue;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NukeValueProvider implements ICapabilitySerializable<CompoundNBT> {
    private final DefaultNukeValue nukevalue = new DefaultNukeValue();
    private final LazyOptional<INukeValue> valueOptional = LazyOptional.of(() -> nukevalue);

    public NukeValueProvider(Long inValue) {
        nukevalue.setNukevalue(inValue);
    }
    public NukeValueProvider() {
        nukevalue.setNukevalue(0L);
    }

    public void invalidate() {
        valueOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return valueOptional.cast();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (CapabilityNukeValue.NUKEVALUE_CAPABILITY == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) CapabilityNukeValue.NUKEVALUE_CAPABILITY.writeNBT(nukevalue, null);
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (CapabilityNukeValue.NUKEVALUE_CAPABILITY != null) {
            CapabilityNukeValue.NUKEVALUE_CAPABILITY.readNBT(nukevalue, null, nbt);
        }
    }

}
