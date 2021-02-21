package com.nuke3dtv.nukesecon.data;

import com.google.gson.JsonObject;
import com.nuke3dtv.nukesecon.setup.Registration;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.loot.conditions.*;
import net.minecraftforge.common.loot.*;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class LootTableModifier extends LootModifier {

    private final int numRolls;
    private final int woodChance;
    private final int ironChance;
    private final int copperChance;
    private final int goldChance;
    private final int diamondChance;
    private final int emeraldChance;
    private Random ran = new Random();

    public LootTableModifier(ILootCondition[] conditionsIn, int inNumRolls, float inWoodChance, float inIronChance,
    float inCopperChance, float inGoldChance, float inDiamondChance, float inEmeraldChance) {
        super(conditionsIn);
        numRolls = inNumRolls;
        woodChance = (int) inWoodChance * 1000000;
        ironChance = (int) inIronChance * 1000000;
        copperChance = (int) inCopperChance * 1000000;
        goldChance = (int) inGoldChance * 1000000;
        diamondChance = (int) inDiamondChance * 1000000;
        emeraldChance = (int) inEmeraldChance * 1000000;
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        // Add up to 10 coins with a decreasing amount of probability per role for every successful coin
        // Once we settle on how we are going to calculate this, move this to config files

        int numWood = 0;
        int numIron = 0;
        int numCopper = 0;
        int numGold = 0;
        int numDiamond = 0;
        int numEmerald = 0;

        for (int i=1; i <= numRolls; i++) {
            int newRoll = ran.nextInt(1000000) + 1;
            if (newRoll <= woodChance) {
                numWood += 1;
                continue;
            }
            newRoll -= woodChance;
            if (newRoll <= ironChance) {
                numIron += 1;
                continue;
            }
            newRoll -= ironChance;
            if (newRoll <= copperChance) {
                numCopper += 1;
                continue;
            }
            newRoll -= copperChance;
            if (newRoll <= goldChance) {
                numGold += 1;
                continue;
            }
            newRoll -= goldChance;
            if (newRoll <= diamondChance) {
                numDiamond += 1;
                continue;
            }
            newRoll -= diamondChance;
            if (newRoll <= emeraldChance) {
                numEmerald += 1;
                continue;
            }
            newRoll -= emeraldChance;
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
        if (numDiamond > 0) {
            generatedLoot.add(new ItemStack(Registration.DIAMONDCOIN.get(), numDiamond));
        }
        if (numEmerald > 0) {
            generatedLoot.add(new ItemStack(Registration.EMERALDCOIN.get(), numEmerald));
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<LootTableModifier> {
        @Override
        public LootTableModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            int numRolls = JSONUtils.getInt(object, "numRolls");
            float woodChance = JSONUtils.getFloat(object, "woodChance");
            float ironChance = JSONUtils.getFloat(object, "ironChance");
            float copperChance = JSONUtils.getFloat(object, "copperChance");
            float goldChance = JSONUtils.getFloat(object, "goldChance");
            float diamondChance = JSONUtils.getFloat(object, "diamondChance");
            float emeraldChance = JSONUtils.getFloat(object, "emeraldChance");
            return new LootTableModifier(conditionsIn, numRolls, woodChance, ironChance,
                    copperChance, goldChance, diamondChance, emeraldChance);
        }

        @Override
        public JsonObject write(LootTableModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
