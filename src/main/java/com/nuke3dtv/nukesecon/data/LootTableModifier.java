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
    private final int mult = 100000;
    private final int mult2;
    private Random ran = new Random();

    public LootTableModifier(ILootCondition[] conditionsIn, int inNumRolls, int inWoodChance, int inIronChance,
    int inCopperChance, int inGoldChance, int inDiamondChance, int inEmeraldChance) {
        super(conditionsIn);
        mult2 = mult / 100;
        numRolls = inNumRolls;
        woodChance = inWoodChance * mult2;
        ironChance = inIronChance * mult2;
        copperChance = inCopperChance * mult2;
        goldChance = inGoldChance * mult2;
        diamondChance = inDiamondChance * mult2;
        emeraldChance = inEmeraldChance * mult2;
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
            int newRoll = ran.nextInt(mult) + 1;
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
            int woodChance = JSONUtils.getInt(object, "woodChance");
            int ironChance = JSONUtils.getInt(object, "ironChance");
            int copperChance = JSONUtils.getInt(object, "copperChance");
            int goldChance = JSONUtils.getInt(object, "goldChance");
            int diamondChance = JSONUtils.getInt(object, "diamondChance");
            int emeraldChance = JSONUtils.getInt(object, "emeraldChance");
            return new LootTableModifier(conditionsIn, numRolls, woodChance, ironChance,
                    copperChance, goldChance, diamondChance, emeraldChance);
        }

        @Override
        public JsonObject write(LootTableModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
