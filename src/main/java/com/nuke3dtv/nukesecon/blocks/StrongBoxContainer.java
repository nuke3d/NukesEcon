package com.nuke3dtv.nukesecon.blocks;

import com.nuke3dtv.nukesecon.items.WoodCoin;
import com.nuke3dtv.nukesecon.setup.Registration;
import com.nuke3dtv.nukesecon.tools.CustomEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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
                addSlot(new SlotItemHandler(h, 1, 38, 23));  // Emerald slot
                addSlot(new SlotItemHandler(h, 2, 56, 23));  // Diamond slot
                addSlot(new SlotItemHandler(h, 3, 74, 23));  // Gold slot
                addSlot(new SlotItemHandler(h, 4, 92, 23)); // Copper slot
                addSlot(new SlotItemHandler(h, 5, 110, 23)); // Iron slot
                addSlot(new SlotItemHandler(h, 6, 128, 23)); // Wood slot
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
                return ((StrongBoxTile) tileEntity).getCoin() & 0xffff;
            }

            @Override
            public void set(int value) {
                int coinStored = ((StrongBoxTile) tileEntity).getCoin() & 0xffff0000;
                ((StrongBoxTile) tileEntity).setCoin(coinStored + (value & 0xffff));
            }
        });
        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return (((StrongBoxTile) tileEntity).getCoin() >> 16) & 0xffff;
            }

            @Override
            public void set(int value) {
                int coinStored = ((StrongBoxTile) tileEntity).getCoin() & 0x0000ffff;
                ((StrongBoxTile) tileEntity).setCoin(coinStored | (value << 16));
            }
        });
    }

    public int getCoins() {
        return ((StrongBoxTile) tileEntity).getCoin();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, Registration.STRONGBOX.get());
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
