package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.screen.slot.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class EnchantmentCanvasItem extends Item {
	
	public EnchantmentCanvasItem(Settings settings) {
		super(settings);
	}
	
	/**
	 * clicked onto another stack
	 */
	@Override
	public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
		if (clickType == ClickType.RIGHT) {
			ItemStack otherStack = slot.getStack();
			if (otherStack.getCount() == 1 && tryExchangeEnchantments(stack, otherStack, player)) {
				if (player != null) {
					playExchangeSound(player);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * itemStack is right-clicked onto this
	 */
	@Override
	public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		if (clickType == ClickType.RIGHT && otherStack.getCount() == 1 && slot.canTakePartial(player)) {
			if (tryExchangeEnchantments(stack, otherStack, player)) {
				if (player != null) {
					playExchangeSound(player);
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean tryExchangeEnchantments(ItemStack canvasStack, ItemStack targetStack, @Nullable Entity receiver) {
		Optional<Item> itemLock = getItemBoundTo(canvasStack);
		if (itemLock.isPresent() && !targetStack.isOf(itemLock.get())) {
			return false;
		}
		
		Map<Enchantment, Integer> canvasEnchantments = EnchantmentHelper.fromNbt(EnchantedBookItem.getEnchantmentNbt(canvasStack));
		Map<Enchantment, Integer> targetEnchantments = EnchantmentHelper.fromNbt(targetStack.getEnchantments());
		if (canvasEnchantments.isEmpty() && targetEnchantments.isEmpty()) {
			return false;
		}
		
		boolean drop = false;
		if (canvasStack.getCount() >= 1) {
			canvasStack = canvasStack.split(1);
			drop = true;
		}
		
		// if the canvas received enchantments: bind it to the other stack
		if (itemLock.isEmpty() && !targetEnchantments.isEmpty()) {
			bindTo(canvasStack, targetStack);
		}
		SpectrumEnchantmentHelper.setStoredEnchantments(targetEnchantments, canvasStack);
		EnchantmentHelper.set(canvasEnchantments, targetStack);
		
		if (drop && receiver != null) {
			if (receiver instanceof PlayerEntity player) {
				player.getInventory().offerOrDrop(canvasStack);
			} else {
				receiver.dropStack(canvasStack);
			}
		}
		
		return true;
	}
	
	private void playExchangeSound(Entity entity) {
		entity.playSound(SoundEvents.BLOCK_GRINDSTONE_USE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		Optional<Item> boundItem = getItemBoundTo(stack);
		if (boundItem.isPresent()) {
			tooltip.add(Text.translatable("item.spectrum.enchantment_canvas.tooltip.bound_to").append(boundItem.get().getName()));
		} else {
			tooltip.add(Text.translatable("item.spectrum.enchantment_canvas.tooltip.not_bound"));
			tooltip.add(Text.translatable("item.spectrum.enchantment_canvas.tooltip.not_bound2"));
		}
		ItemStack.appendEnchantments(tooltip, EnchantedBookItem.getEnchantmentNbt(stack));
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return !EnchantedBookItem.getEnchantmentNbt(stack).isEmpty();
	}
	
	public static void unbind(ItemStack stack) {
		NbtCompound nbt = stack.getOrCreateNbt();
		nbt.remove("BoundItem");
		nbt.remove("StoredEnchantments");
		stack.setNbt(nbt);
	}
	
	private static void bindTo(ItemStack enchantmentExchangerStack, ItemStack targetStack) {
		NbtCompound nbt = enchantmentExchangerStack.getOrCreateNbt();
		nbt.putString("BoundItem", Registries.ITEM.getId(targetStack.getItem()).toString());
		enchantmentExchangerStack.setNbt(nbt);
	}
	
	private static Optional<Item> getItemBoundTo(ItemStack enchantmentExchangerStack) {
		NbtCompound nbt = enchantmentExchangerStack.getNbt();
		if (nbt == null || !nbt.contains("BoundItem", NbtElement.STRING_TYPE)) {
			return Optional.empty();
		}
		String targetItemString = nbt.getString("BoundItem");
		return Optional.of(Registries.ITEM.get(Identifier.tryParse(targetItemString)));
	}
	
}
