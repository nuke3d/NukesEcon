package com.nuke3dtv.nukesecon.data;

import com.google.common.collect.Table;
import com.google.gson.JsonObject;
import com.nuke3dtv.nukesecon.setup.Registration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class LootTableModifier extends LootModifier {

    private Random ran = new Random();

    public LootTableModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        // Add up to 10 coins with a decreasing amount of probability per role for every successful coin
        // Once we settle on how we are going to calculate this, move this to config files
        int maxCoins = 10; // Maximum coins per loot
        int coinChance = 50; // Starting chance of coin
        int dimAmt = 5;
        int rangeMin = 1;
        int rangeMax = 100;
        int numWood = 0;
        int numIron = 0;
        int numCopper = 0;
        int numGold = 0;
        for (int i=1; i < maxCoins; i++) {
            int newRoll = ran.nextInt(rangeMax - rangeMin + 1) + rangeMin;
            if (newRoll <= coinChance) {
                // Winner winner chicken dinner! Now let's see what you won
                // 1-90000 wood, 90001-99900 iron, 99901-99999 silver, 100000 gold
                int randCoin = ran.nextInt(100001);
                if (randCoin <= 90000) {
                    numWood += 1;
                } else if (randCoin <= 99900) {
                    numIron += 1;
                } else if (randCoin <= 99999) {
                    numCopper += 1;
                } else {
                    numGold += 1;
                }
            }
        }

        // What the system giveth, the system sometimes taketh away
        // No more than 1 gold coin allowed per
        if (numGold > 1) {
            numCopper += numGold - 1;
            numGold = 1;
        }
        // No more than 5 copper allowed per
        if (numCopper > 5) {
            numIron += numCopper - 5;
            numCopper = 5;
        }
        // Now add the stacks
        if (numWood > 0) {
            generatedLoot.add(new ItemStack(Registration.WOODCOIN.get(), numWood));
        }
        if (numIron > 0) {
            generatedLoot.add(new ItemStack(Registration.IRONCOIN.get(), numIron));
        }
        if (numCopper > 0) {
            generatedLoot.add(new ItemStack(Registration.COPPERCOIN.get(), numCopper));
        }
        if (numGold > 0) {
            generatedLoot.add(new ItemStack(Registration.GOLDCOIN.get(), numGold));
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<LootTableModifier> {
        @Override
        public LootTableModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            return new LootTableModifier(conditionsIn);
        }

    }
}
