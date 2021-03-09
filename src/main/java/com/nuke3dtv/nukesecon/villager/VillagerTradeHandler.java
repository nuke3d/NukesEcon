package com.nuke3dtv.nukesecon.villager;

import com.google.common.collect.Lists;
import com.nuke3dtv.nukesecon.setup.Registration;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.*;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class VillagerTradeHandler {

    @SubscribeEvent
    public static void onVillagerTradesEvent(VillagerTradesEvent event) {
        boolean deleteEmeralds = true; // Make this in to a setting
        
        if (event.getType() == VillagerProfession.FARMER) {
            // Delete Emerald Trades
            if (deleteEmeralds) { DeleteEmeraldTrades(event); }
            // Wheat for Money
            event.getTrades().get(1).add(new NukeTradeThingForMoney(12, new ItemStack(Items.WHEAT, 6), 4, 1));
            event.getTrades().get(2).add(new NukeTradeThingForMoney(24, new ItemStack(Items.WHEAT, 12), 4, 1));
            event.getTrades().get(3).add(new NukeTradeThingForMoney(48, new ItemStack(Items.WHEAT, 24), 4, 1));
            event.getTrades().get(4).add(new NukeTradeThingForMoney(96, new ItemStack(Items.WHEAT, 48), 4, 1));
            event.getTrades().get(5).add(new NukeTradeThingForMoney(128, new ItemStack(Items.WHEAT, 64), 4, 1));
            // Money for Bonemeal
            event.getTrades().get(1).add(new NukeTradeMoneyForThing(new ItemStack(Items.BONE_MEAL.getItem(), 6), ItemStack.EMPTY, 12, 4, 1));
            event.getTrades().get(2).add(new NukeTradeMoneyForThing(new ItemStack(Items.BONE_MEAL.getItem(), 12), ItemStack.EMPTY, 24, 4, 1));
            event.getTrades().get(3).add(new NukeTradeMoneyForThing(new ItemStack(Items.BONE_MEAL.getItem(), 24), ItemStack.EMPTY, 48, 4, 1));
            event.getTrades().get(4).add(new NukeTradeMoneyForThing(new ItemStack(Items.BONE_MEAL.getItem(), 48), ItemStack.EMPTY, 96, 4, 1));
            event.getTrades().get(5).add(new NukeTradeMoneyForThing(new ItemStack(Items.BONE_MEAL.getItem(), 64), ItemStack.EMPTY, 128, 4, 1));
        }
    }
    
    private static VillagerTradesEvent DeleteEmeraldTrades(VillagerTradesEvent event) {
        // spin through all the trades and if there is an emerald in it, delete it
        for (Int2ObjectMap<List<VillagerTrades.ITrade>> : event.getTrades()) {
            
        }
    }
}

// maxUses is maximum number of this trade that can happen per day
class NukeTradeThingForThing implements VillagerTrades.ITrade {

    private final ItemStack tradeStack1;
    private final ItemStack tradeStack2;
    private final ItemStack rewardStack1;
    private final int maxUses;
    private final int xpValue;

    public NukeTradeThingForThing(ItemStack tradeStackIn, ItemStack tradeStackIn2, ItemStack rewardStackIn, int maxUsesIn, int xpValueIn) {
        this.tradeStack1 = tradeStackIn;
        this.tradeStack2 = tradeStackIn2;
        this.rewardStack1 = rewardStackIn;
        this.maxUses = maxUsesIn;
        this.xpValue = xpValueIn;
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity trader, Random rand) {
        return new MerchantOffer(tradeStack1, tradeStack2, rewardStack1, this.maxUses, this.xpValue, 0.2F);
    }
}

class NukeTradeThingForMoney implements VillagerTrades.ITrade {
    private final ItemStack tradeStack1;
    private final ItemStack tradeStack2;
    private final ItemStack rewardStack1;
    private final int maxUses;
    private final int xpValue;

    public NukeTradeThingForMoney(int inCoins, ItemStack rewardStackIn, int maxUsesIn, int xpValueIn) {
        this.rewardStack1 = rewardStackIn;
        this.maxUses = maxUsesIn;
        this.xpValue = xpValueIn;

        ItemStack stack1 = ItemStack.EMPTY;
        ItemStack stack2 = ItemStack.EMPTY;
        // Figure out closest value in two stacks of coins
        if (inCoins <= 0) { inCoins = 1; } // Must be at least 1 coin
        int coinsLeft = inCoins;

        int emeraldCoins = coinsLeft / Registration.EMERALDCOIN.get().GetCoinValue();
        coinsLeft -= (emeraldCoins * Registration.EMERALDCOIN.get().GetCoinValue());
        if (emeraldCoins > 0) {
            if (stack1 == ItemStack.EMPTY) {
                stack1 = new ItemStack(Registration.EMERALDCOIN.get(), emeraldCoins);
            } else if (stack2 == ItemStack.EMPTY) {
                stack2 = new ItemStack(Registration.EMERALDCOIN.get(), emeraldCoins);
            }
        }

        int diamondCoins = coinsLeft / Registration.DIAMONDCOIN.get().GetCoinValue();
        coinsLeft -= (diamondCoins * Registration.DIAMONDCOIN.get().GetCoinValue());
        if (diamondCoins > 0) {
            if (stack1 == ItemStack.EMPTY) {
                stack1 = new ItemStack(Registration.DIAMONDCOIN.get(), diamondCoins);
            } else if (stack2 == ItemStack.EMPTY) {
                stack2 = new ItemStack(Registration.DIAMONDCOIN.get(), diamondCoins);
            }
        }

        int goldCoins = coinsLeft / Registration.GOLDCOIN.get().GetCoinValue();
        coinsLeft -= (goldCoins * Registration.GOLDCOIN.get().GetCoinValue());
        if (goldCoins > 0) {
            if (stack1 == ItemStack.EMPTY) {
                stack1 = new ItemStack(Registration.GOLDCOIN.get(), goldCoins);
            } else if (stack2 == ItemStack.EMPTY) {
                stack2 = new ItemStack(Registration.GOLDCOIN.get(), goldCoins);
            }
        }

        int copperCoins = coinsLeft / Registration.COPPERCOIN.get().GetCoinValue();
        coinsLeft -= (copperCoins * Registration.COPPERCOIN.get().GetCoinValue());
        if (copperCoins > 0) {
            if (stack1 == ItemStack.EMPTY) {
                stack1 = new ItemStack(Registration.COPPERCOIN.get(), copperCoins);
            } else if (stack2 == ItemStack.EMPTY) {
                stack2 = new ItemStack(Registration.COPPERCOIN.get(), copperCoins);
            }
        }

        int ironCoins = coinsLeft / Registration.IRONCOIN.get().GetCoinValue();
        coinsLeft -= (ironCoins * Registration.IRONCOIN.get().GetCoinValue());
        if (ironCoins > 0) {
            if (stack1 == ItemStack.EMPTY) {
                stack1 = new ItemStack(Registration.IRONCOIN.get(), ironCoins);
            } else if (stack2 == ItemStack.EMPTY) {
                stack2 = new ItemStack(Registration.IRONCOIN.get(), ironCoins);
            }
        }

        int woodCoins = coinsLeft / Registration.WOODCOIN.get().GetCoinValue();
        if (woodCoins > 0) {
            if (stack1 == ItemStack.EMPTY) {
                stack1 = new ItemStack(Registration.WOODCOIN.get(), woodCoins);
            } else if (stack2 == ItemStack.EMPTY) {
                stack2 = new ItemStack(Registration.WOODCOIN.get(), woodCoins);
            }
        }

        this.tradeStack1 = stack1;
        this.tradeStack2 = stack2;
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity trader, Random rand) {
        return new MerchantOffer(tradeStack1, tradeStack2, rewardStack1, this.maxUses, this.xpValue, 0.2F);
    }
}

class NukeTradeMoneyForThing implements VillagerTrades.ITrade {
    private final ItemStack tradeStack1;
    private final ItemStack tradeStack2;
    private final ItemStack rewardStack1;
    private final int maxUses;
    private final int xpValue;

    public NukeTradeMoneyForThing(ItemStack tradeStackIn, ItemStack tradeStackIn2, int inCoins, int maxUsesIn, int xpValueIn) {
        this.tradeStack1 = tradeStackIn;
        this.tradeStack2 = tradeStackIn2;
        this.maxUses = maxUsesIn;
        this.xpValue = xpValueIn;

        ItemStack stack1 = ItemStack.EMPTY;
        // Figure out closest value in one stack of coins
        if (inCoins <= 0) { inCoins = 1; } // Must be at least 1 coin
        int coinsLeft = inCoins;

        int emeraldCoins = coinsLeft / Registration.EMERALDCOIN.get().GetCoinValue();
        coinsLeft -= (emeraldCoins * Registration.EMERALDCOIN.get().GetCoinValue());
        if (emeraldCoins > 0 && stack1 == ItemStack.EMPTY) { stack1 = new ItemStack(Registration.EMERALDCOIN.get(), emeraldCoins); }

        int diamondCoins = coinsLeft / Registration.DIAMONDCOIN.get().GetCoinValue();
        coinsLeft -= (diamondCoins * Registration.DIAMONDCOIN.get().GetCoinValue());
        if (diamondCoins > 0 && stack1 == ItemStack.EMPTY) { stack1 = new ItemStack(Registration.DIAMONDCOIN.get(), diamondCoins); }

        int goldCoins = coinsLeft / Registration.GOLDCOIN.get().GetCoinValue();
        coinsLeft -= (goldCoins * Registration.GOLDCOIN.get().GetCoinValue());
        if (goldCoins > 0 && stack1 == ItemStack.EMPTY) { stack1 = new ItemStack(Registration.GOLDCOIN.get(), goldCoins); }

        int copperCoins = coinsLeft / Registration.COPPERCOIN.get().GetCoinValue();
        coinsLeft -= (copperCoins * Registration.COPPERCOIN.get().GetCoinValue());
        if (copperCoins > 0 && stack1 == ItemStack.EMPTY) { stack1 = new ItemStack(Registration.COPPERCOIN.get(), copperCoins); }

        int ironCoins = coinsLeft / Registration.IRONCOIN.get().GetCoinValue();
        coinsLeft -= (ironCoins * Registration.IRONCOIN.get().GetCoinValue());
        if (ironCoins > 0 && stack1 == ItemStack.EMPTY) { stack1 = new ItemStack(Registration.IRONCOIN.get(), ironCoins); }

        int woodCoins = coinsLeft / Registration.WOODCOIN.get().GetCoinValue();
        if (woodCoins > 0 && stack1 == ItemStack.EMPTY) { stack1 = new ItemStack(Registration.WOODCOIN.get(), woodCoins); }

        this.rewardStack1 = stack1;
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity trader, Random rand) {
        return new MerchantOffer(tradeStack1, tradeStack2, rewardStack1, this.maxUses, this.xpValue, 0.2F);
    }
}
