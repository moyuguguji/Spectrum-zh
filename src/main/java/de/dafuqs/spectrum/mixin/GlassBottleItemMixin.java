package de.dafuqs.spectrum.mixin;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(GlassBottleItem.class)
public abstract class GlassBottleItemMixin {
	
	@Shadow
	protected abstract ItemStack fill(ItemStack stack, PlayerEntity player, ItemStack outputStack);
	
	@Inject(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"),
			cancellable = true,
			locals = LocalCapture.CAPTURE_FAILHARD)
	public void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, List<AreaEffectCloudEntity> list, ItemStack handStack, BlockHitResult areaEffectCloudEntity, BlockPos blockPos) {
		BlockState blockState = world.getBlockState(blockPos);
		
		if (blockState.isOf(SpectrumBlocks.FADING)
				&& SpectrumCommon.CONFIG.CanBottleUpFading
				&& AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_FADING)) {
			
			world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(TypedActionResult.success(this.fill(handStack, user, SpectrumItems.BOTTLE_OF_FADING.getDefaultStack()), world.isClient()));
			
		} else if (blockState.isOf(SpectrumBlocks.FAILING)
				&& SpectrumCommon.CONFIG.CanBottleUpFailing
				&& AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_FAILING)) {
			
			world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(TypedActionResult.success(this.fill(handStack, user, SpectrumItems.BOTTLE_OF_FAILING.getDefaultStack()), world.isClient()));
			
		} else if (blockState.isOf(SpectrumBlocks.RUIN)
				&& SpectrumCommon.CONFIG.CanBottleUpRuin
				&& AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_RUIN)) {
			
			world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(TypedActionResult.success(this.fill(handStack, user, SpectrumItems.BOTTLE_OF_RUIN.getDefaultStack()), world.isClient()));
			
		} else if (blockState.isOf(SpectrumBlocks.FORFEITURE)
				&& SpectrumCommon.CONFIG.CanBottleUpForfeiture
				&& AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.UNLOCK_BOTTLE_OF_FORFEITURE)) {
			
			world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			cir.setReturnValue(TypedActionResult.success(this.fill(handStack, user, SpectrumItems.BOTTLE_OF_FORFEITURE.getDefaultStack()), world.isClient()));
		}
	}
	
}
