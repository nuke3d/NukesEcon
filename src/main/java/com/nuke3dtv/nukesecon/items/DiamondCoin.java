package com.nuke3dtv.nukesecon.items;

import com.nuke3dtv.nukesecon.setup.ModSetup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class DiamondCoin extends Item {

    private static int defaultValue = 10000;
    private int coinValue;

    public DiamondCoin() {
        super(new Item.Properties()
                .maxStackSize(64)
                .group(ModSetup.ITEM_GROUP));
        coinValue = defaultValue;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flags) {
        list.add(new TranslationTextComponent("message.diamondcoin"));
    }

    public int GetCoinValue () {
        return coinValue;
    }
}
