package com.nuke3dtv.nukesecon.capabilities;


import com.nuke3dtv.nukesecon.capabilities.nukevalue.CapabilityNukeValue;
import com.nuke3dtv.nukesecon.capabilities.nukevalue.DefaultNukeValue;
import com.nuke3dtv.nukesecon.setup.Config;
import com.nuke3dtv.nukesecon.setup.Registration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

// Utility class that will hold a coin value and contain the itemStacks of coins required for that amount
// Will make passing around money much easier.

public class CoinWallet {
    private int coins;

    public CoinWallet(int inCoins) {
        coins = inCoins;
    }
    public CoinWallet(CoinWallet inWallet) {
        new CoinWallet(inWallet.coins);
    }
    public CoinWallet(ItemStack inStack) {
        new CoinWallet(GetCoinValue(inStack.getItem()) * inStack.getCount());
    }

    // The ItemStack and Wallet based routines are the preferred way to interact with the wallet, but you
    // can use the int based routines further down if needed.
    public CoinWallet addMoney(CoinWallet inWallet) {
        return new CoinWallet(AddToBalance(inWallet.coins));
    }
    public ItemStack addMoney(ItemStack inStack) {
        // If unable to hold all the coins routine will return what it could not hold
        int coinValue = GetCoinValue(inStack);
        int addValue = coinValue * inStack.getCount();
        int valueBack = AddToBalance(addValue);

        if (valueBack > 0) {
            // Must return back the correct coins
            int coinsBack = valueBack / coinValue;
            int coinsRemainder = valueBack % coinValue;
            if (coinsRemainder > 0) {
                SubFromBalance(coinValue);
                coinsBack++;
            }
            inStack.setCount(coinsBack);
        } else  {
            inStack = ItemStack.EMPTY;
        }
        return inStack;
    }

    public CoinWallet getMoney(CoinWallet inWallet) {
        return new CoinWallet(SubFromBalance(inWallet.coins));
    }
    public ItemStack getMoney(ItemStack inStack) {
        // If unable to get all the coins routine will return what it could get
        int subValue = GetCoinValue(inStack) * inStack.getCount();
        int valueBack = SubFromBalance(subValue);

        if (valueBack > 0) {
            int tooMuchCoins = subValue - valueBack;
            inStack.setCount(valueBack / GetCoinValue(inStack));
            // Now add back in any remainder coin value
            AddToBalance(tooMuchCoins);
        } else  {
            inStack = ItemStack.EMPTY;
        }
        return inStack;
    }

    // These routines use integers to mess with balances. Coders are encouraged to use
    // the routines above that actually involve ItemStacks first when appropriate

    // If adding more than it can hold, you get back what it cannot hold
    public int AddToBalance(int addCoins) {
        int newCoins = coins + addCoins;
        int retVal = 0;
        if (newCoins > Config.STRONGBOX_MAXCOINS.get()) {
            // Tried to add more than wallet could hold - return what you could not add
            retVal = newCoins - Config.STRONGBOX_MAXCOINS.get();
            coins = Config.STRONGBOX_MAXCOINS.get();
        } else {
            coins = newCoins;
        }
        return retVal;
    }
    // If subtracting more than in wallet, you get back what you could get
    public int SubFromBalance(int subCoins) {
        int newCoins = coins - subCoins;
        int retVal = 0;
        if (newCoins < 0) {
            // Tried to subtract more than is in wallet - return what you could get
            retVal = coins;
            coins = 0;
        } else {
            coins = newCoins;
        }
        return retVal;
    }
    public int GetBalance() {
        return coins;
    }
    public void SetBalance(int newBalance) {
        coins = newBalance;
    }

    // Utility methods to get the individual coin values of items and stacks
    // These could be expanded to get the value of non-coin items once that system is added
    public int GetCoinValue(ItemStack inStack) {
        return inStack.getCapability(CapabilityNukeValue.NUKEVALUE_CAPABILITY).orElse(new DefaultNukeValue(0)).getNukevalue();
    }
    public int GetCoinValue(Item inItem) {
        return GetCoinValue(new ItemStack(inItem, 1));
    }
}
