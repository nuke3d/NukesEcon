package com.nuke3dtv.nukesecon.capabilities.nukevalue;

import com.nuke3dtv.nukesecon.capabilities.nukelock.CapabilityNukeLock;
import com.nuke3dtv.nukesecon.capabilities.nukelock.DefaultNukeLock;
import com.nuke3dtv.nukesecon.capabilities.nukelock.INukeLock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityNukeValue {
    @CapabilityInject(INukeValue.class)
    public static Capability<INukeValue> NUKEVALUE_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(INukeValue.class, new CapabilityNukeValue.Storage(), DefaultNukeValue::new);
    }

    public static class Storage implements Capability.IStorage<INukeValue> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<INukeValue> capability, INukeValue instance, Direction side) {
            CompoundNBT tag = new CompoundNBT();
            tag.putLong("nukevalue", instance.getNukevalue());
            return tag;
        }

        @Override
        public void readNBT(Capability<INukeValue> capability, INukeValue instance, Direction side, INBT nbt) {
            Long nukevalue = ((CompoundNBT) nbt).getLong("nukevalue");
            instance.setNukevalue(nukevalue);
        }
    }

}
