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

        // Would like to move all the below into a config file so trades for different villagers can be modified that way

        boolean deleteEmeralds = false; // Make this in to a setting

        int baseCost; // base number of items/coins the villager wants
        int baseRewards; // Base number of items/coins the villager will give
        int baseMaxUses;
        int baseXPValue;

        // ARMORER
        if (event.getType() == VillagerProfession.ARMORER) {
            // Delete Emerald Trades
            if (deleteEmeralds) { DeleteEmeraldTrades(event); }

            // Iron Helmet for Money
            baseCost = 50 ; // base number of items to trade per trade
            baseRewards = 1; // multiplier for number of items wanted to items rewarded
            baseMaxUses = 4;
            baseXPValue = 1;
            for (int i=1; i<=5; i++) {
                int wantedCount = baseCost * i;
                int rewardCount = baseRewards * i;
                event.getTrades().get(i).add(new NukeTradeThingForMoney(wantedCount, new ItemStack(Items.IRON_HELMET, rewardCount), baseMaxUses, baseXPValue));
            }
            // Iron Chestplate for Money
            baseCost = 80 ; // base number of items to trade per trade
            baseRewards = 1; // multiplier for number of items wanted to items rewarded
            baseMaxUses = 4;
            baseXPValue = 1;
            for (int i=1; i<=5; i++) {
                int wantedCount = baseCost * i;
                int rewardCount = baseRewards * i;
                event.getTrades().get(i).add(new NukeTradeThingForMoney(wantedCount, new ItemStack(Items.IRON_CHESTPLATE, rewardCount), baseMaxUses, baseXPValue));
            }
            // Iron Leggings for Money
            baseCost = 70 ; // base number of items to trade per trade
            baseRewards = 1; // multiplier for number of items wanted to items rewarded
            baseMaxUses = 4;
            baseXPValue = 1;
            for (int i=1; i<=5; i++) {
                int wantedCount = baseCost * i;
                int rewardCount = baseRewards * i;
                event.getTrades().get(i).add(new NukeTradeThingForMoney(wantedCount, new ItemStack(Items.IRON_LEGGINGS, rewardCount), baseMaxUses, baseXPValue));
            }
            // Iron Boots for Money
            baseCost = 40 ; // base number of items to trade per trade
            baseRewards = 1; // multiplier for number of items wanted to items rewarded
            baseMaxUses = 4;
            baseXPValue = 1;
            for (int i=1; i<=5; i++) {
                int wantedCount = baseCost * i;
                int rewardCount = baseRewards * i;
                event.getTrades().get(i).add(new NukeTradeThingForMoney(wantedCount, new ItemStack(Items.IRON_BOOTS, rewardCount), baseMaxUses, baseXPValue));
            }
            // Money for Iron Ingots
            baseCost = 10;
            baseRewards = 25;
            for (int i=1; i<=5; i++) {
                int wantedCount = baseCost * i;
                int rewardCount = baseRewards * i;
                event.getTrades().get(i).add(new NukeTradeMoneyForThing(new ItemStack(Items.IRON_INGOT, wantedCount), ItemStack.EMPTY, rewardCount, baseMaxUses, baseXPValue));
            }
        }
        // FARMER
        if (event.getType() == VillagerProfession.FARMER) {
            // Delete Emerald Trades
            if (deleteEmeralds) { DeleteEmeraldTrades(event); }

            // Wheat for Money
            baseCost = 12 ; // base number of items to trade per trade
            baseRewards = 6; // multiplier for number of items wanted to items rewarded
            baseMaxUses = 4;
            baseXPValue = 1;
            for (int i=1; i<=5; i++) {
                int wantedCount = baseCost * i;
                int rewardCount = baseRewards * i;
                event.getTrades().get(i).add(new NukeTradeThingForMoney(wantedCount, new ItemStack(Items.WHEAT, rewardCount), baseMaxUses, baseXPValue));
            }
            // Money for Bonemeal
            baseCost = 4;
            baseRewards = 1;
            for (int i=1; i<=5; i++) {
                int wantedCount = baseCost * i;
                int rewardCount = baseRewards * i;
                event.getTrades().get(i).add(new NukeTradeMoneyForThing(new ItemStack(Items.BONE_MEAL, wantedCount), ItemStack.EMPTY, rewardCount, baseMaxUses, baseXPValue));
            }
        }
    }

    // Putting this part of the villager trades on hold as there is no easy way to selectively delete trades
    private static VillagerTradesEvent DeleteEmeraldTrades(VillagerTradesEvent event) {
        // spin through all the trades and if there is an emerald in it, delete it
        /*for (Int2ObjectMap<List<?>> : event.getTrades()) {
            
        }
        for (int i=1; i<= event.getTrades().size(); i++) {
            for (int ii=1; i<= event.getTrades().get(i).size(); ii++) {
                VillagerTrades.ITrade inTrade = event.getTrades().get(i).get(ii);
                if (inTrade. == Items.EMERALD || )
            }
        }*/
        return event;
    }

    // Villager Trades Money For Things (Buys stuff with money) - Not sure if/when this will be used but while I'm here...
    protected void addNewTrade(VillagerTradesEvent event, int inVillagerLevel, Item inWantedItem, int inWantedCount, int inReward, int inMaxUses, int inXPValue) {
        this.addNewTrade(event, inVillagerLevel, new ItemStack(inWantedItem, inWantedCount), inReward, inMaxUses, inXPValue);
    }
    protected void addNewTrade(VillagerTradesEvent event, int inVillagerLevel, ItemStack inWantedStack, int inReward, int inMaxUses, int inXPValue) {
        this.addNewTrade(event, inVillagerLevel, inWantedStack, ItemStack.EMPTY, inReward, inMaxUses, inXPValue);
    }
    protected void addNewTrade(VillagerTradesEvent event, int inVillagerLevel, ItemStack inWantedStack, ItemStack inWantedStack2, int inReward, int inMaxUses, int inXPValue) {
        event.getTrades().get(inVillagerLevel).add(new NukeTradeMoneyForThing(inWantedStack, inWantedStack2, inReward, inMaxUses, inXPValue));
    }
    // Villager Trades Things For Money
    protected void addNewTrade(VillagerTradesEvent event, int inVillagerLevel, int inWanted, Item inRewardItem, int inRewardCount, int inMaxUses, int inXPValue) {
        this.addNewTrade(event, inVillagerLevel, inWanted, new ItemStack(inRewardItem, inRewardCount), inMaxUses, inXPValue);
    }
    protected void addNewTrade(VillagerTradesEvent event, int inVillagerLevel, int inWanted, ItemStack inRewardStack, int inMaxUses, int inXPValue) {
        event.getTrades().get(inVillagerLevel).add(new NukeTradeThingForMoney(inWanted, inRewardStack, inMaxUses, inXPValue));
    }
    // Villager Trades Things for Things
    protected void addNewTrade(VillagerTradesEvent event, int inVillagerLevel, ItemStack inWanted, ItemStack inRewardStack, int inMaxUses, int inXPValue) {
        this.addNewTrade(event, inVillagerLevel, inWanted, ItemStack.EMPTY, inRewardStack, inMaxUses, inXPValue);
    }
    protected void addNewTrade(VillagerTradesEvent event, int inVillagerLevel, ItemStack inWanted, ItemStack inWanted2, ItemStack inRewardStack, int inMaxUses, int inXPValue) {
        event.getTrades().get(inVillagerLevel).add(new NukeTradeThingForThing(inWanted, inWanted2, inRewardStack, inMaxUses, inXPValue));
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
