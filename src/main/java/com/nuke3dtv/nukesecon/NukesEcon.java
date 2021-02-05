package com.nuke3dtv.nukesecon;

import com.nuke3dtv.nukesecon.setup.ClientSetup;
import com.nuke3dtv.nukesecon.setup.Config;
import com.nuke3dtv.nukesecon.setup.ModSetup;
import com.nuke3dtv.nukesecon.setup.Registration;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NukesEcon.MODID)
public class NukesEcon {

    public static final String MODID = "nukesecon";

    private static final Logger LOGGER = LogManager.getLogger();

    public NukesEcon() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

        Registration.init();

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }
}
