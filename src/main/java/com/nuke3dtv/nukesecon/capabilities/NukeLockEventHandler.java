package com.nuke3dtv.nukesecon.capabilities;

import com.nuke3dtv.nukesecon.NukesEcon;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;

// If the Itemstack is mine, override initCapabilities/use getCapability and setCapabilty. Otherwise, I need to use the onAttachCapabilities event.
// Commented out below but can be added if needed. Check ModSetup for attaching to events

public class NukeLockEventHandler {
    /*public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof CreatureEntity) {
            NukeLockProvider provider = new NukeLockProvider();
            event.addCapability(new ResourceLocation(NukesEcon.MODID, "keycode"), provider);
            event.addListener(provider::invalidate);
        }
    }*/
}
