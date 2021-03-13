package com.nuke3dtv.nukesecon.items;

import com.nuke3dtv.nukesecon.capabilities.CapabilityNukeLock;
import com.nuke3dtv.nukesecon.capabilities.DefaultNukeLock;
import com.nuke3dtv.nukesecon.capabilities.INukeLock;
import com.nuke3dtv.nukesecon.capabilities.NukeLockProvider;
import com.nuke3dtv.nukesecon.setup.ModSetup;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class StrongboxSkeletonKey extends Item {

    public StrongboxSkeletonKey() {
        super(new Properties()
                .maxStackSize(64)
                .group(ModSetup.ITEM_GROUP));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flags) {
        list.add(new TranslationTextComponent("message.strongbox_skeletonkey"));
        if (Screen.hasShiftDown()) {
            INukeLock keyKey = stack.getCapability(CapabilityNukeLock.NUKELOCK_CAPABILITY).orElse(new DefaultNukeLock());
            Integer keyCode = keyKey.getKeyCode();
            list.add(new StringTextComponent(keyCode.toString()));
        }
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        NukeLockProvider newProvider = new NukeLockProvider();
        newProvider.getCapability(CapabilityNukeLock.NUKELOCK_CAPABILITY).ifPresent(h -> {
            h.setSkeleton(true);
        });
        return newProvider;
    }

}
