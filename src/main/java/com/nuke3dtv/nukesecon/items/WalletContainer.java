package com.nuke3dtv.nukesecon.items;

import com.nuke3dtv.nukesecon.blocks.StrongBoxTile;
import com.nuke3dtv.nukesecon.capabilities.WalletProvider;
import com.nuke3dtv.nukesecon.capabilities.nukelock.CapabilityNukeLock;
import com.nuke3dtv.nukesecon.capabilities.nukelock.DefaultNukeLock;
import com.nuke3dtv.nukesecon.capabilities.nukelock.INukeLock;
import com.nuke3dtv.nukesecon.capabilities.nukevalue.CapabilityNukeValue;
import com.nuke3dtv.nukesecon.capabilities.nukevalue.DefaultNukeValue;
import com.nuke3dtv.nukesecon.setup.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import static com.nuke3dtv.nukesecon.tools.MathTools.*;

public class WalletContainer extends Container {

    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;
    private WalletProvider walletProvider;

    public WalletContainer(int windowId, World world, PlayerInventory playerInventory, PlayerEntity player, WalletProvider inProvider) {
        super(Registration.WALLET_CONTAINER.get(), windowId);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.walletProvider = inProvider;

        if (walletProvider != null) {
            walletProvider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 135, 45));  // Key In
                addSlot(new SlotItemHandler(h, 1, 153, 45));  // Key Out
                addSlot(new SlotItemHandler(h, 2, 9, 23));  // Coin deposit
                // Not sure why I didn't call addSlotRange to begin with
                addSlotRange(h, 3, 38, 23, 6, 18);
                    //addSlot(new SlotItemHandler(h, 3, 38, 23));  // Wood slot
                    //addSlot(new SlotItemHandler(h, 4, 56, 23));  // Iron slot
                    //addSlot(new SlotItemHandler(h, 5, 74, 23));  // Copper slot
                    //addSlot(new SlotItemHandler(h, 6, 92, 23)); // Gold slot
                    //addSlot(new SlotItemHandler(h, 7, 110, 23)); // Diamond slot
                    //addSlot(new SlotItemHandler(h, 8, 128, 23)); // Emerald slot
                addSlot(new SlotItemHandler(h, 9, 152, 2));  // Lock slot
            });
        }
        layoutPlayerInventorySlots(10, 70);
        trackCoins();
        trackKey();
        //trackLocked();
    }

    // Setup syncing of coins from server to client so that the GUI can show the amount of coins in the block
    private void trackCoins() {
        // Unfortunately on a dedicated server ints are actually truncated to short so we need
        // to split our integer here (split our 32 bit integer into two 16 bit integers)
        // UPDATE: Now that I need to track a long, I need more ints.
        int[] intCoins = longToTwoInts(walletProvider.getCapability(CapabilityNukeValue.NUKEVALUE_CAPABILITY).orElse(new DefaultNukeValue()).getNukevalue());
        int[] shortCoins1 = intToShortInts(intCoins[0]);
        int[] shortCoins2 = intToShortInts(intCoins[1]);
        trackInt(new IntReferenceHolder() {
            @Override
            /*public int get() { return ((StrongBoxTile) tileEntity).wallet.GetBalance() & 0xffff; }*/
            public int get() {
                return shortCoins1[0];
            }

            @Override
            public void set(int value) {
                walletProvider.getCapability(CapabilityNukeValue.NUKEVALUE_CAPABILITY).ifPresent(h -> {
                    long coinStored = h.getNukevalue() & 0xffff0000;
                    h.setNukevalue(twoIntsToLong());
                });
                int coinStored = ((StrongBoxTile) tileEntity).wallet.GetBalance() & 0xffff0000;
                ((StrongBoxTile) tileEntity).wallet.SetBalance(coinStored + (value & 0xffff));
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() { return shortCoins1[1]; }

            @Override
            public void set(int value) {
                int coinStored = ((StrongBoxTile) tileEntity).wallet.GetBalance() & 0x0000ffff;
                ((StrongBoxTile) tileEntity).wallet.SetBalance(coinStored | (value << 16));
            }
        });
    }
    // Also setup tracking of keys
    private void trackKey() {
        // Unfortunately on a dedicated server ints are actually truncated to short so we need
        // to split our integer here (split our 32 bit integer into two 16 bit integers)
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return getKeyCode() & 0xffff;
                //return ((StrongBoxTile) tileEntity).getKeyCode() & 0xffff;
            }

            @Override
            public void set(int value) {
                tileEntity.getCapability(CapabilityNukeLock.NUKELOCK_CAPABILITY).ifPresent(h -> {
                    int keyStored = h.getKeyCode() & 0xffff0000;
                    h.setKeyCode(keyStored + (value & 0xffff));
                });
                //int keyStored = ((StrongBoxTile) tileEntity).getKeyCode() & 0xffff0000;
                //((StrongBoxTile) tileEntity).setKeyCode(keyStored + (value & 0xffff));
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return (getKeyCode() >> 16) & 0xffff;
                //return (((StrongBoxTile) tileEntity).getKeyCode() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                tileEntity.getCapability(CapabilityNukeLock.NUKELOCK_CAPABILITY).ifPresent(h -> {
                    int keyStored = h.getKeyCode() & 0x0000ffff;
                    h.setKeyCode(keyStored | (value << 16));
                });
                //int keyStored = ((StrongBoxTile) tileEntity).getCoinValue() & 0x0000ffff;
                //((StrongBoxTile) tileEntity).setKeyCode(keyStored | (value << 16));
            }
        });
    }

    public int getCoins() {
        return ((StrongBoxTile) tileEntity).wallet.GetBalance();
    }
    public int getKeyCode() {
        INukeLock tileLock = ((StrongBoxTile) tileEntity).getCapability(CapabilityNukeLock.NUKELOCK_CAPABILITY).orElse(new DefaultNukeLock());
        return tileLock.getKeyCode();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, Registration.STRONGBOX.get());
    }

    // If the slot is clicked, this is called. (Once on server and once I client I think)
    // Handle any special functionality you need for clicking a slot here
    // Handle key slot 1 to make sure they don't try to drop a key in the slot
    // Handle coins slots(3-8) to allow withdraw coins by clicking
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        // If one of the coin slots is clicked, attempt to withdraw coins to mouse
        PlayerInventory playerinventory = player.inventory;
        if (slotId == 1) {
            // If the slot is empty, don't let them try to put something in the programmed key slot
            if (!playerinventory.getItemStack().isEmpty()) {
                return playerinventory.getItemStack();
            }
        } else if (slotId == 9) {
            // Only allow locks here
            if (playerinventory.getItemStack().isEmpty()) {
                // If nothing in their mouse then let them pickup anything in the slot
                return super.slotClick(slotId, dragType, clickTypeIn, player);
            } else {
                // If something in there hand it must be a lock or no go
                if (playerinventory.getItemStack().getItem() == Registration.IRONLOCK.get()) {
                    return super.slotClick(slotId, dragType, clickTypeIn, player);
                } else {
                    return playerinventory.getItemStack();
                }
            }
        } else if (slotId >= 3 && slotId <= 8) {
            // Get coin slots
            if (dragType != 0) { return ItemStack.EMPTY; }
            // Add one coin type to whatever is on the mouse
            if (clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) {
                if (playerinventory.getItemStack().getCount() == playerinventory.getItemStack().getMaxStackSize()) { return ItemStack.EMPTY; }

                int moveAmt = 1;
                if (clickTypeIn == ClickType.QUICK_MOVE) { moveAmt = 64; } // Move a stack

                ItemStack newStack = ItemStack.EMPTY;
                if (slotId == 3) {
                    newStack = new ItemStack(Registration.IRONCOIN.get());
                }
                if (slotId == 4) {
                    newStack = new ItemStack(Registration.COPPERCOIN.get());
                }
                if (slotId == 5) {
                    newStack = new ItemStack(Registration.GOLDCOIN.get());
                }
                if (slotId == 6) {
                    newStack = new ItemStack(Registration.OBSIDIANCOIN.get());
                }
                if (slotId == 7) {
                    newStack = new ItemStack(Registration.DIAMONDCOIN.get());
                }
                if (slotId == 8) {
                    newStack = new ItemStack(Registration.EMERALDCOIN.get());
                }
                if (playerinventory.getItemStack().getItem() != newStack.getItem() && playerinventory.getItemStack().isEmpty() != true) { return ItemStack.EMPTY; } // Something already on the mouse that isn't the coin type for the slot we clicked
                if (moveAmt == 64) { moveAmt = newStack.getMaxStackSize() - playerinventory.getItemStack().getCount(); } // Correct to actual max stack size just in case we are trying to grab a stack
                newStack.setCount(moveAmt);
                newStack = ((StrongBoxTile) tileEntity).wallet.getMoney(newStack);
                if (playerinventory.getItemStack().isEmpty() == false) { newStack.grow(playerinventory.getItemStack().getCount()); }
                playerinventory.setItemStack(newStack);
                return newStack;
            }
            return ItemStack.EMPTY;
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    /* Handle when stack in slot is shift clicked */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack stackReturn = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stackReturn = slotStack.copy();
            // If shift click the key slots or the coin slot, try to move what's there to player inventory
            if (index >= 0 && index <= 2) {
                if (!this.mergeItemStack(slotStack, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(slotStack, stackReturn);
            } else {
                if (slotStack.getItem() == Registration.IRONCOIN.get() || slotStack.getItem() == Registration.COPPERCOIN.get() || slotStack.getItem() == Registration.GOLDCOIN.get() || slotStack.getItem() == Registration.OBSIDIANCOIN.get() ||slotStack.getItem() == Registration.DIAMONDCOIN.get() || slotStack.getItem() == Registration.EMERALDCOIN.get()) {
                    // Try to shift click into drop slot
                    if (!this.mergeItemStack(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 36) {
                    // shift clicking player inventory - merge to hotbar
                    if (!this.mergeItemStack(slotStack, 36, 45, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 45 && !this.mergeItemStack(slotStack, 9, 36, false)) {
                    // shift click from hotbar - merge to player inventory
                    return ItemStack.EMPTY;
                }
            }

            // if shift click and stack in slot is empty, put empty back in slot else call slot changed
            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            // if stack in slot count == count of whatever is in itemstack then return empty stack
            if (slotStack.getCount() == stackReturn.getCount()) {
                return ItemStack.EMPTY;
            }
            // Not sure what this is supposed to do beyond calling onSlotChanged
            slot.onTake(playerIn, slotStack);
        }

        // return itemstack
        return stackReturn;
    }


    // Add a line of slots - index=start slot index, x =start x pos, y=start y pos
    // amount=number of slots to add, dx=distance between slots(from slot left side to slot left side)
    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    // Add a box of slots
    // index=start slot index, x =start x pos, y=start y pos, horAmount=Number of slots across to add
    // dx=distance between slots(horiz), verAmount=Number of rows of slots, dy=distance between rows
    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    // layout inventory slots and then hotbar slots
    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory slots, 3 rows of 9 slots, 27 slots total, indexes 9-35
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar player inventory - 1 row of 9 slots, indexes 0-8
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }
}
