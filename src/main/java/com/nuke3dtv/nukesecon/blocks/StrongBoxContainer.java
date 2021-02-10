package com.nuke3dtv.nukesecon.blocks;

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
                addSlot(new SlotItemHandler(h, 0, 9, 23));  // Coin deposit
                addSlot(new SlotItemHandler(h, 1, 38, 23));  // Wood slot
                addSlot(new SlotItemHandler(h, 2, 56, 23));  // Iron slot
                addSlot(new SlotItemHandler(h, 3, 74, 23));  // Copper slot
                addSlot(new SlotItemHandler(h, 4, 92, 23)); // Gold slot
                addSlot(new SlotItemHandler(h, 5, 110, 23)); // Diamond slot
                addSlot(new SlotItemHandler(h, 6, 128, 23)); // Emerald slot
            });
        }
        layoutPlayerInventorySlots(10, 70);
        trackCoins();
    }

    // Setup syncing of coins from server to client so that the GUI can show the amount of power in the block
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

    public int getCoins() {
        return ((StrongBoxTile) tileEntity).getCoinValue();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, Registration.STRONGBOX.get());
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        // If one of the coin slots is clicked, attempt to withdraw coins to mouse
        PlayerInventory playerinventory = player.inventory;
        if (slotId >= 1 && slotId <= 6) {
            if (dragType != 0) { return ItemStack.EMPTY; }
            // Add one coin type to whatever is on the mouse
            if (clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) {
                if (playerinventory.getItemStack().getCount() == playerinventory.getItemStack().getMaxStackSize()) { return ItemStack.EMPTY; }

                int moveAmt = 1;
                if (clickTypeIn == ClickType.QUICK_MOVE) { moveAmt = 64; }

                ItemStack newStack = ItemStack.EMPTY;
                int coinValue = 0;
                if (slotId == 1) {
                    newStack = new ItemStack(Registration.WOODCOIN.get());
                    coinValue = Registration.WOODCOIN.get().GetCoinValue();
                }
                if (slotId == 2) {
                    newStack = new ItemStack(Registration.IRONCOIN.get());
                    coinValue = Registration.IRONCOIN.get().GetCoinValue();
                }
                if (slotId == 3) {
                    newStack = new ItemStack(Registration.COPPERCOIN.get());
                    coinValue = Registration.COPPERCOIN.get().GetCoinValue();
                }
                if (slotId == 4) {
                    newStack = new ItemStack(Registration.GOLDCOIN.get());
                    coinValue = Registration.GOLDCOIN.get().GetCoinValue();
                }
                if (slotId == 5) {
                    newStack = new ItemStack(Registration.DIAMONDCOIN.get());
                    coinValue = Registration.DIAMONDCOIN.get().GetCoinValue();
                }
                if (slotId == 6) {
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

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();
            if (index == 0) {
                if (!this.mergeItemStack(stack, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemstack);
            } else {
                if (stack.getItem() == Registration.WOODCOIN.get() || stack.getItem() == Registration.IRONCOIN.get() || stack.getItem() == Registration.COPPERCOIN.get() || stack.getItem() == Registration.GOLDCOIN.get() || stack.getItem() == Registration.DIAMONDCOIN.get() || stack.getItem() == Registration.EMERALDCOIN.get()) {
                    if (!this.mergeItemStack(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 28) {
                    if (!this.mergeItemStack(stack, 34, 43, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 43 && !this.mergeItemStack(stack, 1, 34, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }



    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }
}
