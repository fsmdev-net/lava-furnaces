package net.fsmdev.lavafurnaces;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.gen.Accessor;

public interface FurnaceInterface {
    default int getBurnTime() {
        return 0;
    }

    default void setFuelTime(int fuelTime) {};
    default void setBurnTime(int burnTime) {};

    default RecipeManager.MatchGetter<SingleStackRecipeInput, ? extends AbstractCookingRecipe> getMatchGetter() {
        return null;
    }

    default DefaultedList<ItemStack> getInventory() {
        return null;
    }

    default void setInventory(DefaultedList<ItemStack> inventory) {

    }
}
