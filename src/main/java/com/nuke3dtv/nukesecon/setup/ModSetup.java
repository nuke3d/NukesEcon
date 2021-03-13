package com.nuke3dtv.nukesecon.setup;

import com.nuke3dtv.nukesecon.NukesEcon;
import com.nuke3dtv.nukesecon.capabilities.CapabilityNukeLock;
import com.nuke3dtv.nukesecon.network.Networking;
import com.nuke3dtv.nukesecon.villager.VillagerTradeHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = NukesEcon.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final ItemGroup ITEM_GROUP = new ItemGroup("nukesecon") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registration.EMERALDCOIN.get());
        }
    };

    public static void init(final FMLCommonSetupEvent event) {
        Networking.registerMessages();
        CapabilityNukeLock.register();

        MinecraftForge.EVENT_BUS.addListener(VillagerTradeHandler::onVillagerTradesEvent);
        //MinecraftForge.EVENT_BUS.addListener(NukeLockEventHandler::onAttachCapabilitiesEvent);
        //MinecraftForge.EVENT_BUS.addListener(NukeLockEventHandler::onAttackEvent);
        //MinecraftForge.EVENT_BUS.addListener(NukeLockEventHandler::onDeathEvent);
    }
}
