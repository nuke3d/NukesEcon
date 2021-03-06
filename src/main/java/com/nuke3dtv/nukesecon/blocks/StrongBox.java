package com.nuke3dtv.nukesecon.blocks;

import com.nuke3dtv.nukesecon.data.CapabilityNukeLock;
import com.nuke3dtv.nukesecon.data.DefaultNukeLock;
import com.nuke3dtv.nukesecon.data.INukeLock;
import com.nuke3dtv.nukesecon.data.NukeLockProvider;
import com.nuke3dtv.nukesecon.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

public class StrongBox extends Block {

    public StrongBox() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2.0f)
                .setLightLevel(state -> 14 / 16)
        );
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new StrongBoxTile();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof StrongBoxTile) {
                INukeLock tileLock = ((StrongBoxTile) tileEntity).getCapability(CapabilityNukeLock.NUKELOCK_CAPABILITY).orElse(new DefaultNukeLock());
                boolean openBox = false; // This will be true if we can open the strongbox after all checks made

                // Check that player has the key in inventory or in a container within their inventory. (Future keyring functionality?)
                // First check player inventory
                if (tileLock.getLocked() == false) openBox = true; // Always unlock if box is unlocked
                //if (((StrongBoxTile) tileEntity).isBoxLocked() == false) openBox = true; // Always unlock if box is unlocked
                for (int i=0; i <= player.inventory.getSizeInventory(); i++) {
                    INukeLock keyKey = player.inventory.getStackInSlot(i).getCapability(CapabilityNukeLock.NUKELOCK_CAPABILITY).orElse(new DefaultNukeLock());
                    if (tileLock.getKeyCode() == keyKey.getInverseOf(keyKey.getKeyCode())) {
                        // We found a key and it matches the inverse of our strongbox code! Open the gates!
                        openBox = true;
                        break;
                    }
                }
                if (openBox) {
                    INamedContainerProvider containerProvider = new INamedContainerProvider() {
                        @Override
                        public ITextComponent getDisplayName() {
                            return new TranslationTextComponent("screen.nukesecon.strongbox");
                        }

                        @Override
                        public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                            return new StrongBoxContainer(i, world, pos, playerInventory, playerEntity);
                        }
                    };
                    NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getPos());
                }

            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }
}
