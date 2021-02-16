package com.nuke3dtv.nukesecon.datagen;

import com.nuke3dtv.nukesecon.NukesEcon;
import com.nuke3dtv.nukesecon.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Function;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, NukesEcon.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerStrongBox();
    }

    private void registerStrongBox() {
        ResourceLocation txt = new ResourceLocation(NukesEcon.MODID, "block/strongbox");
        BlockModelBuilder modelFirstblock = models().cube("strongbox", txt, txt, new ResourceLocation(NukesEcon.MODID, "block/strongbox_front"), txt, txt, txt);
        orientedBlock(Registration.STRONGBOX.get(), state -> {
                return modelFirstblock;
        });
    }



     private void orientedBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction dir = state.get(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(dir.getAxis() == Direction.Axis.Y ?  dir.getAxisDirection().getOffset() * -90 : 0)
                            .rotationY(dir.getAxis() != Direction.Axis.Y ? ((dir.getHorizontalIndex() + 2) % 4) * 90 : 0)
                            .build();
                });
    }

}
