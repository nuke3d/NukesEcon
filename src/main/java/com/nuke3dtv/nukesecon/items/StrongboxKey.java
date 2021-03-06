package com.nuke3dtv.nukesecon.items;

import com.nuke3dtv.nukesecon.blocks.StrongBoxTile;
import com.nuke3dtv.nukesecon.data.CapabilityNukeLock;
import com.nuke3dtv.nukesecon.data.DefaultNukeLock;
import com.nuke3dtv.nukesecon.data.INukeLock;
import com.nuke3dtv.nukesecon.data.NukeLockProvider;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;

public class StrongboxKey extends Item {

    public StrongboxKey() {
        super(new Item.Properties()
                .maxStackSize(64)
                .group(ModSetup.ITEM_GROUP));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flags) {
        list.add(new TranslationTextComponent("message.strongbox_key"));
        if (Screen.hasShiftDown()) {
            INukeLock keyKey = stack.getCapability(CapabilityNukeLock.NUKELOCK_CAPABILITY).orElse(new DefaultNukeLock());
            Integer keyCode = keyKey.getInverseOf(keyKey.getKeyCode());
            list.add(new StringTextComponent(keyCode.toString()));
        }
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new NukeLockProvider();
    }

}
