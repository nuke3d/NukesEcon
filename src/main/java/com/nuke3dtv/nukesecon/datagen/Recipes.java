package com.nuke3dtv.nukesecon.datagen;

import com.nuke3dtv.nukesecon.items.IronLock;
import com.nuke3dtv.nukesecon.setup.Registration;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(Registration.IRONLOCK.get())
                .patternLine(" ##")
                .patternLine(" xx")
                .patternLine(" xx")
                .key('x', Tags.Items.INGOTS_IRON)
                .key('#', Tags.Items.NUGGETS_IRON)
                .setGroup("nukesecon")
                .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.IRON_ORE))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.STRONGBOX_KEY.get())
                .patternLine(" ##")
                .patternLine(" ##")
                .patternLine(" xx")
                .key('x', Tags.Items.INGOTS_IRON)
                .key('#', Tags.Items.NUGGETS_IRON)
                .setGroup("nukesecon")
                .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.IRON_ORE))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Registration.STRONGBOX.get())
                .patternLine("xxx")
                .patternLine("x#x")
                .patternLine("xxx")
                .key('x', Tags.Items.INGOTS_IRON)
                .key('#', Registration.IRONLOCK.get())
                .setGroup("nukesecon")
                .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.IRON_ORE))
                .build(consumer);
    }
}
