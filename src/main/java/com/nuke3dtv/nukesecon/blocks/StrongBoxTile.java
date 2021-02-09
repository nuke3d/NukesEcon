package com.nuke3dtv.nukesecon.blocks;

import com.nuke3dtv.nukesecon.setup.Config;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.nuke3dtv.nukesecon.setup.Registration.*;

public class StrongBoxTile extends TileEntity {

    private ItemStackHandler itemHandler = createHandler();

    // Never create lazy optionals in getCapability. Always place them as fields in the tile entity:
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    // This holds the amount of coins we have in the StrongBox
    private int coin;

    public StrongBoxTile() {
        super(STRONGBOX_TILE.get());
    }

    @Override
    public void remove() {
        super.remove();
        handler.invalidate();
    }

    @Override
    public void read(CompoundNBT tag) {
        itemHandler.deserializeNBT(tag.getCompound("inv"));

        coin = tag.getInt("coin");
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("inv", itemHandler.serializeNBT());

        tag.putInt("coin", coin);
        return super.write(tag);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(7) {

            @Override
            protected void onContentsChanged(int slot) {
                if (slot == 0) {
                    if (!this.getStackInSlot(slot).isEmpty()) {
                        this.setStackInSlot(slot, acceptStack(this.getStackInSlot(slot))); // absorb the stack
                    }
                }
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                // You can drop only coins into certain slots
                if(slot == 1) return stack.getItem() == EMERALDCOIN.get();
                if(slot == 2) return stack.getItem() == DIAMONDCOIN.get();
                if(slot == 3) return stack.getItem() == GOLDCOIN.get();
                if(slot == 4) return stack.getItem() == COPPERCOIN.get();
                if(slot == 5) return stack.getItem() == IRONCOIN.get();
                if(slot == 6) return stack.getItem() == WOODCOIN.get();
                return (stack.getItem() == WOODCOIN.get() || stack.getItem() == IRONCOIN.get() || stack.getItem() == COPPERCOIN.get() || stack.getItem() == GOLDCOIN.get() || stack.getItem() == DIAMONDCOIN.get() || stack.getItem() == EMERALDCOIN.get());
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (slot == 1) {
                    if (stack.getItem() != WOODCOIN.get() && stack.getItem() != IRONCOIN.get() && stack.getItem() != COPPERCOIN.get() && stack.getItem() != GOLDCOIN.get() && stack.getItem() != DIAMONDCOIN.get() && stack.getItem() != EMERALDCOIN.get()) {
                        return stack;
                    }
                }
                if (slot > 1 && slot < 7) {
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
      return super.getCapability(cap, side);
    }

    // returns number of coins that could not be added
    public int addCoin(int inCoin, int inCoinValue, boolean simulate) {
        int coinsBack = addCoin(inCoin * inCoinValue, simulate);
        if (coinsBack == 0) { return 0; }
        return coinsBack / inCoinValue;
    }
    // returns amount that could not be added
    public int addCoin(int inCoin, boolean simulate) {
        int newCoin = coin;
        newCoin += inCoin;

        if((inCoin + newCoin) > Config.STRONGBOX_MAXCOINS.get()) {
            int retVal = newCoin - Config.STRONGBOX_MAXCOINS.get();
            newCoin = Config.STRONGBOX_MAXCOINS.get();
            if (simulate == false) {
                coin = newCoin;
                markDirty();
            }
            return retVal;
        } else {
            if (simulate == false) {
                coin = newCoin;
                markDirty();
            }
            return 0;
        }
    }

    // getCoin will return the full amount of coins or whatever amount it could return based on what is in the StrongBox
    public int getCoin(int inCoin, int inCoinValue, boolean simulate) {
        int amtWithdrawn = getCoin(inCoin * inCoinValue, simulate);
        if (amtWithdrawn == 0) { return 0; }
        // return any that is too small for the current coin value
        int amtBack = amtWithdrawn % inCoinValue;
        if (amtBack > 0) addCoin(amtBack, simulate);
        return amtWithdrawn / inCoinValue;
    }
    public int getCoin(int inCoin, boolean simulate) {
        if((coin - inCoin) < 0) {
            int retVal = coin;
            if (simulate == false) {
                coin = 0;
                markDirty();
            }
            return retVal;
        } else {
            if (simulate == false) {
                coin -= inCoin;
                markDirty();
            }
            return inCoin;
        }
    }

    private ItemStack acceptStack(ItemStack stack) {
        int coinStart = coin;
        if (stack.getItem() == WOODCOIN.get()) { coin += WOODCOIN.get().GetCoinValue() * stack.getCount(); }
        if (stack.getItem() == IRONCOIN.get()) { coin += IRONCOIN.get().GetCoinValue() * stack.getCount(); }
        if (stack.getItem() == COPPERCOIN.get()) { coin += COPPERCOIN.get().GetCoinValue() * stack.getCount(); }
        if (stack.getItem() == GOLDCOIN.get()) { coin += GOLDCOIN.get().GetCoinValue() * stack.getCount(); }
        if (stack.getItem() == DIAMONDCOIN.get()) { coin += DIAMONDCOIN.get().GetCoinValue() * stack.getCount(); }
        if (stack.getItem() == EMERALDCOIN.get()) { coin += EMERALDCOIN.get().GetCoinValue() * stack.getCount(); }
        if (coinStart != coin) { markDirty(); }
        return ItemStack.EMPTY;
    }
    public int getCoinValue() { return coin; }
    public void setCoinValue(int inCoin) {
        coin = inCoin;
        markDirty();
    }
}

