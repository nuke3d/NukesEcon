package com.nuke3dtv.nukesecon.blocks;

import com.nuke3dtv.nukesecon.data.CapabilityNukeLock;
import com.nuke3dtv.nukesecon.data.DefaultNukeLock;
import com.nuke3dtv.nukesecon.data.INukeLock;
import com.nuke3dtv.nukesecon.items.*;
import com.nuke3dtv.nukesecon.setup.Registration;
import com.nuke3dtv.nukesecon.tools.CustomEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class StrongBoxContainer extends Container {

    private TileEntity tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;

    public StrongBoxContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(Registration.STRONGBOX_CONTAINER.get(), windowId);
        tileEntity = world.getTileEntity(pos);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 141, 3));  // Key In
                addSlot(new SlotItemHandler(h, 1, 159, 3));  // Key Out
                addSlot(new SlotItemHandler(h, 2, 9, 23));  // Coin deposit
                // Not sure why I didn't call addSlotRange to begin with
                addSlotRange(h, 3, 38, 23, 6, 18);
                    //addSlot(new SlotItemHandler(h, 3, 38, 23));  // Wood slot
                    //addSlot(new SlotItemHandler(h, 4, 56, 23));  // Iron slot
                    //addSlot(new SlotItemHandler(h, 5, 74, 23));  // Copper slot
                    //addSlot(new SlotItemHandler(h, 6, 92, 23)); // Gold slot
                    //addSlot(new SlotItemHandler(h, 7, 110, 23)); // Diamond slot
                    //addSlot(new SlotItemHandler(h, 8, 128, 23)); // Emerald slot
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
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return ((StrongBoxTile) tileEntity).getCoinValue() & 0xffff;
            }

            @Override
            public void set(int value) {
                int coinStored = ((StrongBoxTile) tileEntity).getCoinValue() & 0xffff0000;
                ((StrongBoxTile) tileEntity).setCoinValue(coinStored + (value & 0xffff));
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return (((StrongBoxTile) tileEntity).getCoinValue() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                int coinStored = ((StrongBoxTile) tileEntity).getCoinValue() & 0x0000ffff;
                ((StrongBoxTile) tileEntity).setCoinValue(coinStored | (value << 16));
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
        return ((StrongBoxTile) tileEntity).getCoinValue();
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
        } else if (slotId >= 3 && slotId <= 8) {
            // Get coin slots
            if (dragType != 0) { return ItemStack.EMPTY; }
            // Add one coin type to whatever is on the mouse
            if (clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) {
                if (playerinventory.getItemStack().getCount() == playerinventory.getItemStack().getMaxStackSize()) { return ItemStack.EMPTY; }

                int moveAmt = 1;
                if (clickTypeIn == ClickType.QUICK_MOVE) { moveAmt = 64; }

                ItemStack newStack = ItemStack.EMPTY;
                int coinValue = 0;
                if (slotId == 3) {
                    newStack = new ItemStack(Registration.WOODCOIN.get());
                    coinValue = Registration.WOODCOIN.get().GetCoinValue();
                }
                if (slotId == 4) {
                    newStack = new ItemStack(Registration.IRONCOIN.get());
                    coinValue = Registration.IRONCOIN.get().GetCoinValue();
                }
                if (slotId == 5) {
                    newStack = new ItemStack(Registration.COPPERCOIN.get());
                    coinValue = Registration.COPPERCOIN.get().GetCoinValue();
                }
                if (slotId == 6) {
                    newStack = new ItemStack(Registration.GOLDCOIN.get());
                    coinValue = Registration.GOLDCOIN.get().GetCoinValue();
                }
                if (slotId == 7) {
                    newStack = new ItemStack(Registration.DIAMONDCOIN.get());
                    coinValue = Registration.DIAMONDCOIN.get().GetCoinValue();
                }
                if (slotId == 8) {
                    newStack = new ItemStack(Registration.EMERALDCOIN.get());
                    coinValue = Registration.EMERALDCOIN.get().GetCoinValue();
                }

                if (playerinventory.getItemStack().getItem() != newStack.getItem() && playerinventory.getItemStack().isEmpty() != true) { return ItemStack.EMPTY; }
                if (moveAmt == 64) { moveAmt = newStack.getMaxStackSize(); } // Correct to actual max stack size just in case we are trying to grab a stack
                int afterSet = ((StrongBoxTile) tileEntity).getCoin(moveAmt, coinValue, false);
                newStack.setCount(afterSet);
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
                if (slotStack.getItem() == Registration.WOODCOIN.get() || slotStack.getItem() == Registration.IRONCOIN.get() || slotStack.getItem() == Registration.COPPERCOIN.get() || slotStack.getItem() == Registration.GOLDCOIN.get() || slotStack.getItem() == Registration.DIAMONDCOIN.get() || slotStack.getItem() == Registration.EMERALDCOIN.get()) {
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
