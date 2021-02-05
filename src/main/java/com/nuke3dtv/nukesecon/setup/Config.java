package com.nuke3dtv.nukesecon.setup;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String SUBCATEGORY_STRONGBOX = "strongbox";

    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.IntValue STRONGBOX_MAXCOINS;

    static {

        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        CLIENT_BUILDER.pop();

        setupStrongBoxConfig(SERVER_BUILDER, CLIENT_BUILDER);

        //SERVER_BUILDER.pop();


        SERVER_CONFIG = SERVER_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupStrongBoxConfig(ForgeConfigSpec.Builder SERVER_BUILDER, ForgeConfigSpec.Builder CLIENT_BUILDER) {
        SERVER_BUILDER.comment("StrongBox settings").push(SUBCATEGORY_STRONGBOX);
        STRONGBOX_MAXCOINS = SERVER_BUILDER.comment("Maximum coins per strongbox")
                .defineInRange("maxPower", 1000000000, 0, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }


}
