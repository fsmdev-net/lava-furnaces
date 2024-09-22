package net.fsmdev.lavafurnaces.mixin;

import net.fsmdev.lavafurnaces.FurnaceInterface;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin (AbstractFurnaceBlockEntity.class)
public abstract class FurnaceMixin implements FurnaceInterface {

    @Accessor
    public abstract int getBurnTime();

    @Accessor
    public abstract void setFuelTime(int fuelTime);

    @Accessor
    public abstract void setBurnTime(int burnTime);

    @Accessor
    public abstract DefaultedList<ItemStack> getInventory();

    @Accessor
    public abstract void setInventory(DefaultedList<ItemStack> inventory);

    @Accessor
    public abstract RecipeManager.MatchGetter<SingleStackRecipeInput, ? extends AbstractCookingRecipe> getMatchGetter();

    @Shadow
    private static boolean canAcceptRecipeOutput(
            DynamicRegistryManager registryManager, @Nullable RecipeEntry<?> recipe, DefaultedList<ItemStack> slots, int count
    ) { return false; };

    @Inject(method="tick", at=@At("HEAD"))
    private static void tick(World world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        BlockPos blockBelowPos = pos.add(new Vec3i(0, -1, 0));
        FluidState fluidState = world.getFluidState(blockBelowPos);
        Fluid fluidBelow = fluidState.getFluid();
        if (fluidBelow instanceof LavaFluid && fluidState.isStill()) {
            FurnaceInterface thisFurnace = ((FurnaceInterface)blockEntity);
             if (thisFurnace.getBurnTime() <= 0) {
                 DefaultedList<ItemStack> furnaceInventory = thisFurnace.getInventory();
                 if (furnaceInventory.get(1) == ItemStack.EMPTY) {
                     RecipeEntry<?> recipeEntry = ((FurnaceInterface) blockEntity).getMatchGetter().getFirstMatch(new SingleStackRecipeInput(((FurnaceInterface) blockEntity).getInventory().getFirst()), world).orElse(null);
                     if (canAcceptRecipeOutput(world.getRegistryManager(), recipeEntry, furnaceInventory, 1)) {
                         furnaceInventory.set(1, new ItemStack(Items.OAK_LOG));
                     }
                 }
             }
        }
    }
}
