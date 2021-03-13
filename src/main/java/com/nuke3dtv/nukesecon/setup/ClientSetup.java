package com.nuke3dtv.nukesecon.setup;

import com.nuke3dtv.nukesecon.NukesEcon;
import com.nuke3dtv.nukesecon.blocks.StrongBoxScreen;
import com.nuke3dtv.nukesecon.items.WalletScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = NukesEcon.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(Registration.STRONGBOX_CONTAINER.get(), StrongBoxScreen::new);
        ScreenManager.registerFactory(Registration.WALLET_CONTAINER.get(), WalletScreen::new);
    }

    @SubscribeEvent
    public void onTooltipPre(RenderTooltipEvent.Pre event) {
        Item item = event.getStack().getItem();
        if (item.getRegistryName().getNamespace().equals(NukesEcon.MODID)) {
            event.setMaxWidth(200);
        }
    }
}
