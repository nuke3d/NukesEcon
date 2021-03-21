package com.nuke3dtv.nukesecon.tools;

import com.nuke3dtv.nukesecon.capabilities.nukelock.DefaultNukeLock;
import com.nuke3dtv.nukesecon.capabilities.nukelock.INukeLock;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*
    This class contains tools to facilitate trading
 */
public class TradeTools {

    // The methods are from the point of view of the owner of the trade(The object offering the trade)
    // For player to player trades this would be the one who notifies the other a trade is available
    // For player to shop trades this would be the shop
    // For player to order board trades this would be the order board
    public class TradeObject {
        private List<ItemStack> offeredList = new ArrayList<>();
        private List<ItemStack> wantedList = new ArrayList<>();
        private INukeLock tradeLock;

        TradeObject() {
            tradeLock = new DefaultNukeLock();
        }
        public List<ItemStack> addOffered(ItemStack inStack) {
            return addOffered(inStack, new DefaultNukeLock(0)); // A zero key generally will not unlock anything except a lock that was explicitly set to zero
        }
        public List<ItemStack> addOffered(ItemStack inStack, INukeLock inLock) {
            if (tradeLock.getLocked()) {
                if (tradeLock.testLock(inLock)) {
                    offeredList.add(inStack);
                }
            } else {
                offeredList.add(inStack);
            }
            return offeredList;
        }
        public List<ItemStack> removeOffered(ItemStack inStack) {
            return removeOffered(inStack, new DefaultNukeLock(0));
        }
        public List<ItemStack> removeOffered(ItemStack inStack, INukeLock inLock) {
            if (tradeLock.getLocked()) {
                if (tradeLock.testLock(inLock)) {
                    offeredList.remove(inStack);
                }
            } else {
                offeredList.remove(inStack);
            }
            return wantedList;
        }

        public List<ItemStack> addWanted(ItemStack inStack) {
            return addWanted(inStack, new DefaultNukeLock(0)); // A zero key generally will not unlock anything except a lock that was explicitly set to zero
        }
        public List<ItemStack> addWanted(ItemStack inStack, INukeLock inLock) {
            if (tradeLock.getLocked()) {
                if (tradeLock.testLock(inLock)) {
                    wantedList.add(inStack);
                }
            } else {
                wantedList.add(inStack);
            }
            return wantedList;
        }
        public List<ItemStack> removeWanted(ItemStack inStack) {
            return removeWanted(inStack, new DefaultNukeLock(0));
        }
        public List<ItemStack> removeWanted(ItemStack inStack, INukeLock inLock) {
            if (tradeLock.getLocked()) {
                if (tradeLock.testLock(inLock)) {
                    wantedList.remove(inStack);
                }
            } else {
                wantedList.remove(inStack);
            }
            return wantedList;
        }


        public void lockTrade() {
            tradeLock.setLocked(true);
        }
        public void lockTrade(INukeLock inLock) {
            tradeLock = inLock;
            lockTrade();
        }
        public void unlockTrade() {
            tradeLock.setLocked(false);
        }
        public boolean isLocked() {
            return tradeLock.getLocked();
        }
        public boolean testKey(INukeLock inKey) {
            if (inKey.getKeyCode() == tradeLock.getKeyCode()) { return true; }
            return false;
        }
    }


}

