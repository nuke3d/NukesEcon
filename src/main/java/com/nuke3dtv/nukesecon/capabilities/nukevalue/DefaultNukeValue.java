package com.nuke3dtv.nukesecon.capabilities.nukevalue;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.math.BigInteger;

public class DefaultNukeValue implements INukeValue, INBTSerializable<CompoundNBT> {

    private Long nukevalue;

    public DefaultNukeValue() {
        nukevalue = 0L;
    }
    public DefaultNukeValue(Long inValue) {
        nukevalue = inValue;
    }

    // INukeValue begin
    @Override
    public Long getNukevalue() {
        return nukevalue;
    }

    @Override
    public void setNukevalue(Long inValue) {
        nukevalue = inValue;
    }

    // IBNTSerializable begin
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putLong("nukevalue", nukevalue);
        return tag;
    }
    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        nukevalue = nbt.getLong("nukevalue");
    }
}
