package com.nuke3dtv.nukesecon.blocks;

import com.nuke3dtv.nukesecon.capabilities.nukelock.CapabilityNukeLock;
import com.nuke3dtv.nukesecon.capabilities.nukelock.DefaultNukeLock;
import com.nuke3dtv.nukesecon.capabilities.nukelock.INukeLock;
import com.nuke3dtv.nukesecon.capabilities.CoinWallet;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;

import static com.nuke3dtv.nukesecon.setup.Registration.*;

public class StrongBoxTile extends TileEntity {

    @CapabilityInject(INukeLock.class)

    private ItemStackHandler itemHandler = createHandler();
    private DefaultNukeLock nukeHandler = new DefaultNukeLock();

    // Never create lazy optionals in getCapability. Always place them as fields in the tile entity:
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private LazyOptional<INukeLock> lockHandler = LazyOptional.of(() -> nukeHandler);

    private Random ran = new Random();

    // This holds the amount of coins we have in the StrongBox
    // Making this public to expose its methods/functions
    public CoinWallet wallet = new CoinWallet(0);

    public StrongBoxTile() {
        super(STRONGBOX_TILE.get());
            // We will create unique id here and send it out to client
            nukeHandler.setKeyCode(ran.nextInt(1000000000));
    }

    @Override
    public void remove() {
        super.remove();
        handler.invalidate();
        lockHandler.invalidate();
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        itemHandler.deserializeNBT(tag.getCompound("inv"));
        nukeHandler.deserializeNBT(tag.getCompound("nukelock"));

        wallet.SetBalance(tag.getInt("coin"));

        super.read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("inv", itemHandler.serializeNBT());
        tag.put("nukelock", nukeHandler.serializeNBT());

        tag.putInt("coin", wallet.GetBalance());
        return super.write(tag);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(10) {

            @Override
            protected void onContentsChanged(int slot) {
                if (slot == 0) {
                    // key to be coded
                    // only if there is a key in slot 0 and no key already in slot 1
                    if (this.getStackInSlot(slot).getItem() == STRONGBOX_KEY.get() && this.getStackInSlot(1).isEmpty()) {
                        INukeLock keyKey = this.getStackInSlot(slot).getCapability(CapabilityNukeLock.NUKELOCK_CAPABILITY).orElse(new DefaultNukeLock());
                        keyKey.setKeyCode(nukeHandler.getInverseOf(nukeHandler.getKeyCode()));
                        nukeHandler.setLocked(false);

                        this.setStackInSlot(1, this.getStackInSlot(slot));
                        this.setStackInSlot(slot, ItemStack.EMPTY);
                    }
                } else if (slot == 1) {
                    // Not sure if I need anything here or not yet
               } else if (slot == 2) {
                    if (!this.getStackInSlot(slot).isEmpty()) {
                        this.setStackInSlot(slot, acceptStack(this.getStackInSlot(slot))); // absorb the stack
                    }
                } else if (slot == 9) {
                    // If there is a lock here then the box is locked
                    if (this.getStackInSlot(9).getItem() == IRONLOCK.get()) {
                        nukeHandler.setLocked(true);
                    } else {
                        nukeHandler.setLocked(false);
                    }
                }
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                // You can drop only coins into certain slots
                // You can only drop keys in the key slots
                if(slot == 0) return stack.getItem() == STRONGBOX_KEY.get(); // key in
                if(slot == 1) return stack.getItem() == STRONGBOX_KEY.get(); // key out
                if(slot == 3) return stack.getItem() == WOODCOIN.get();      // wood coin slot
                if(slot == 4) return stack.getItem() == IRONCOIN.get();      // iron coin slot
                if(slot == 5) return stack.getItem() == COPPERCOIN.get();    // copper coin slot
                if(slot == 6) return stack.getItem() == GOLDCOIN.get();      // gold coin slot
                if(slot == 7) return stack.getItem() == DIAMONDCOIN.get();   // diamond coin slot
                if(slot == 8) return stack.getItem() == EMERALDCOIN.get();   // emerald coin slot
                if(slot == 9) return stack.getItem() == IRONLOCK.get();      // lock slot
                // add coin slot
                return (stack.getItem() == WOODCOIN.get() || stack.getItem() == IRONCOIN.get() || stack.getItem() == COPPERCOIN.get() || stack.getItem() == GOLDCOIN.get() || stack.getItem() == DIAMONDCOIN.get() || stack.getItem() == EMERALDCOIN.get());
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                // handle coin in slot
                if (slot == 3) {
                    if (stack.getItem() != WOODCOIN.get() && stack.getItem() != IRONCOIN.get() && stack.getItem() != COPPERCOIN.get() && stack.getItem() != GOLDCOIN.get() && stack.getItem() != DIAMONDCOIN.get() && stack.getItem() != EMERALDCOIN.get()) {
                        return stack;
                    }
                }
                if (slot > 3 && slot < 9) {
                    if (stack.isEmpty()) {
                        // Clicking or shift clicking with nothing in hand should grab 1 or a stack of coins
                    }

                        return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        if (cap == CapabilityNukeLock.NUKELOCK_CAPABILITY) {
            return lockHandler.cast();
        }
      return super.getCapability(cap, side);
    }

    // returns amount that could not be added
    public int addCoin(int inCoin) {
        int coinBack = wallet.AddToBalance(inCoin);
        if (coinBack != inCoin) { markDirty(); }
        return coinBack;
    }

    // getCoin will return the full amount of coins or whatever amount it could return based on what is in the StrongBox
    public int getCoin(int inCoin) {
        int coinBack = wallet.SubFromBalance(inCoin);
        if (coinBack > 0) { markDirty(); }
        return coinBack;
    }

    private ItemStack acceptStack(ItemStack stack) {
        int coinStart = wallet.GetBalance();
        stack = wallet.addMoney(stack);
        if (coinStart != wallet.GetBalance()) { markDirty(); }
        return stack;
    }
    public int getCoinValue() { return wallet.GetBalance(); }
    public void setCoinValue(int inCoin) {
        wallet.SetBalance(inCoin);
        markDirty();
    }
}

