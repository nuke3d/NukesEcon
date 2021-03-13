package com.nuke3dtv.nukesecon.tools;

import com.nuke3dtv.nukesecon.capabilities.INukeLock;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*
    This class contains tools to facilitate trading
 */
public class TradeTools {

    public class TradeObject {
        private List<ItemStack> offeredList = new ArrayList<>();
        private List<ItemStack> wantedList = new ArrayList<>();
        private INukeLock tradeLock;

        TradeObject() {

        }
        public boolean addToOffered(ItemStack inStack) {
            return true;
        }
        public boolean addToWanted(ItemStack inStack) {
            return true;
        }
    }
}

