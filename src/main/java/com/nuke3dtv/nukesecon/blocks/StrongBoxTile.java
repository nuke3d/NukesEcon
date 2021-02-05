package com.nuke3dtv.nukesecon.blocks;

import com.nuke3dtv.nukesecon.setup.Config;
import com.nuke3dtv.nukesecon.setup.Registration;
import com.nuke3dtv.nukesecon.tools.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

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

        coin = tag.getInt("counter");
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("inv", itemHandler.serializeNBT());

        tag.putInt("counter", coin);
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
                return (stack.getItem() == WOODCOIN.get() || stack.getItem() == IRONCOIN.get() || stack.getItem() == COPPERCOIN.get() || stack.getItem() == GOLDCOIN.get() || stack.getItem() == DIAMONDCOIN.get() || stack.getItem() == EMERALDCOIN.get());
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (stack.getItem() != WOODCOIN.get() && stack.getItem() != IRONCOIN.get() && stack.getItem() != COPPERCOIN.get() && stack.getItem() != GOLDCOIN.get() && stack.getItem() != DIAMONDCOIN.get() && stack.getItem() != EMERALDCOIN.get()) {
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

    // Coin Handling
    // addcoin will return the leftover amount if we could not add it all
    public int addCoin(int inCoin) {
        coin += inCoin;

        if((inCoin + coin) > Config.STRONGBOX_MAXCOINS.get()) {
            int retVal = coin - Config.STRONGBOX_MAXCOINS.get();
            coin = Config.STRONGBOX_MAXCOINS.get();
            markDirty();
            return retVal;
        } else {
            markDirty();
            return 0;
        }
    }
    // subCoin will return the leftover amount if we did not have enough coins to subtract that much
    public int subCoin(int inCoin) {
        if((coin - inCoin) < 0) {
            int retVal = inCoin - coin;
            coin = 0;
            markDirty();
            return retVal;
        } else {
            coin -= inCoin;
            markDirty();
            return 0;
        }
    }
    private ItemStack acceptStack(ItemStack stack) {
        int coinStart = coin;
        if (stack.getItem() == WOODCOIN.get()) { coin += 1 * stack.getCount(); }
        if (stack.getItem() == IRONCOIN.get()) { coin += 10 * stack.getCount(); }
        if (stack.getItem() == COPPERCOIN.get()) { coin += 100 * stack.getCount(); }
        if (stack.getItem() == GOLDCOIN.get()) { coin += 1000 * stack.getCount(); }
        if (stack.getItem() == DIAMONDCOIN.get()) { coin += 10000 * stack.getCount(); }
        if (stack.getItem() == EMERALDCOIN.get()) { coin += 100000 * stack.getCount(); }
        if (coinStart != coin) { markDirty(); }
        return ItemStack.EMPTY;
    }
    public int getCoin() { return coin; }
    public void setCoin(int inCoin) {
        coin = inCoin;
        markDirty();
    }
}

