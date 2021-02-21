package com.nuke3dtv.nukesecon.datagen;

import com.nuke3dtv.nukesecon.NukesEcon;
import com.nuke3dtv.nukesecon.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, NukesEcon.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleTexture(Registration.IRONLOCK.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"),
                "layer0", new ResourceLocation(NukesEcon.MODID, "item/ironlock"));
        singleTexture(Registration.STRONGBOX_KEY.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"),
                "layer0", new ResourceLocation(NukesEcon.MODID, "item/strongbox_key"));
        singleTexture(Registration.WOODCOIN.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"),
                "layer0", new ResourceLocation(NukesEcon.MODID, "item/woodcoin"));
        singleTexture(Registration.IRONCOIN.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"),
                "layer0", new ResourceLocation(NukesEcon.MODID, "item/ironcoin"));
        singleTexture(Registration.COPPERCOIN.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"),
                "layer0", new ResourceLocation(NukesEcon.MODID, "item/coppercoin"));
        singleTexture(Registration.GOLDCOIN.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"),
                "layer0", new ResourceLocation(NukesEcon.MODID, "item/goldcoin"));
        singleTexture(Registration.DIAMONDCOIN.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"),
                "layer0", new ResourceLocation(NukesEcon.MODID, "item/diamondcoin"));
        singleTexture(Registration.EMERALDCOIN.get().getRegistryName().getPath(), new ResourceLocation("item/handheld"),
                "layer0", new ResourceLocation(NukesEcon.MODID, "item/emeraldcoin"));
        withExistingParent(Registration.STRONGBOX_ITEM.get().getRegistryName().getPath(), new ResourceLocation(NukesEcon.MODID, "block/strongbox"));
    }
}
