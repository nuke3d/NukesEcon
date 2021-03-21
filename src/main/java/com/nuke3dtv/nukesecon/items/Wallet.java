package com.nuke3dtv.nukesecon.items;

import com.nuke3dtv.nukesecon.capabilities.WalletProvider;
import com.nuke3dtv.nukesecon.setup.ModSetup;
import com.nuke3dtv.nukesecon.capabilities.CoinWallet;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.nuke3dtv.nukesecon.setup.Registration.*;

public class Wallet extends Item {

    private ItemStackHandler itemHandler = createHandler();

    // Never create lazy optionals in getCapability. Always place them as fields in the tile entity:
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public CoinWallet wallet = new CoinWallet(0);

    public Wallet() {
        super(new Properties()
                .maxStackSize(1)
                .group(ModSetup.ITEM_GROUP));
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(7) {

        @Override
        protected void onContentsChanged(int slot) {
            if (slot == 0) {
           } else if (slot == 1) {
                // Not sure if I need anything here or not yet
            } else if (slot == 2) {
                if (!this.getStackInSlot(slot).isEmpty()) {
                    this.setStackInSlot(slot, acceptStack(this.getStackInSlot(slot))); // absorb the stack
                }
            } else if (slot == 9) {
            }
            // To make sure the TE persists when the chunk is saved later we need to
            // mark it dirty every time the item handler changes
            //markDirty();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            // You can drop only coins into certain slots
            // You can only drop keys in the key slots
            if(slot == 0) return stack.getItem() == STRONGBOX_KEY.get(); // key in
            if(slot == 1) return stack.getItem() == STRONGBOX_KEY.get(); // key out
            if(slot == 3) return stack.getItem() == IRONCOIN.get();      // iron coin slot
            if(slot == 4) return stack.getItem() == COPPERCOIN.get();    // copper coin slot
            if(slot == 5) return stack.getItem() == GOLDCOIN.get();      // gold coin slot
            if(slot == 6) return stack.getItem() == OBSIDIANCOIN.get();  // obsidian coin slot
            if(slot == 7) return stack.getItem() == DIAMONDCOIN.get();   // diamond coin slot
            if(slot == 8) return stack.getItem() == EMERALDCOIN.get();   // emerald coin slot
            if(slot == 9) return stack.getItem() == IRONLOCK.get();      // lock slot
            // add coin slot
            return (stack.getItem() == IRONCOIN.get() || stack.getItem() == COPPERCOIN.get() || stack.getItem() == GOLDCOIN.get() || stack.getItem() == OBSIDIANCOIN.get() || stack.getItem() == DIAMONDCOIN.get() || stack.getItem() == EMERALDCOIN.get());
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            // handle coin in slot
            if (slot == 3) {
                if (stack.getItem() != IRONCOIN.get() && stack.getItem() != COPPERCOIN.get() && stack.getItem() != GOLDCOIN.get() && stack.getItem() != OBSIDIANCOIN.get() && stack.getItem() != DIAMONDCOIN.get() && stack.getItem() != EMERALDCOIN.get()) {
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

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new WalletProvider();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flags) {
        list.add(new TranslationTextComponent("message.wallet"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        INamedContainerProvider containerProvider = new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("screen.nukesecon.wallet");
            }

            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new WalletContainer(i, playerEntity.world, playerInventory, playerEntity, this  );
            }
        };

        ItemStack inStack = playerIn.getHeldItem(handIn);
        NetworkHooks.openGui((ServerPlayerEntity) playerIn, containerProvider);
        return ActionResult.resultSuccess(inStack);
    }

    private ItemStack acceptStack(ItemStack stack) {
        int coinStart = wallet.GetBalance();
        stack = wallet.addMoney(stack);
        //if (coinStart != wallet.GetBalance()) { markDirty(); }
        return stack;
    }

}
