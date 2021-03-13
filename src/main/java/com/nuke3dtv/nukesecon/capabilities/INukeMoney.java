package com.nuke3dtv.nukesecon.capabilities;


import net.minecraft.item.ItemStack;

/*
    This interface describes how to accept, store, and give money as either objects or integer values
 */
public interface INukeMoney {

    ItemStack addMoney(ItemStack inStack);
    int addMoney(int addCoins);

    ItemStack getMoney(ItemStack inStack);
    int getMoney(int subCoins);

    int GetBalance();
    void SetBalance(int newBalance);
}
