package com.nuke3dtv.nukesecon.datagen;

import com.nuke3dtv.nukesecon.setup.Registration;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableProvider {

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        lootTables.put(Registration.STRONGBOX.get(), createStandardTable("strongbox", Registration.STRONGBOX.get()));
    }
}
