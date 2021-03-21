package com.nuke3dtv.nukesecon.capabilities;


import net.minecraft.item.ItemStack;

import java.math.BigInteger;

/*
    This interface describes how to accept, store, and give money as either objects or integer values
 */
public interface INukeMoney {

    ItemStack addMoney(ItemStack inStack);
    BigInteger addMoney(BigInteger addCoins);

    ItemStack getMoney(ItemStack inStack);
    BigInteger getMoney(BigInteger subCoins);

    BigInteger GetBalance();
    void SetBalance(BigInteger newBalance);
}
