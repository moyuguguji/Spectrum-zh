package de.dafuqs.spectrum.enchantments;

import com.sammy.malum.common.item.curiosities.weapons.scythe.MalumScytheItem;
import de.dafuqs.spectrum.compat.SpectrumIntegrationPacks;
import de.dafuqs.spectrum.items.tools.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;

public class InventoryInsertionEnchantment extends SpectrumEnchantment {
	
	public InventoryInsertionEnchantment(Rarity weight, Identifier unlockAdvancementIdentifier, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.DIGGER, slotTypes, unlockAdvancementIdentifier);
	}
	
	@Override
	public int getMinPower(int level) {
		return 15;
	}
	
	@Override
	public int getMaxPower(int level) {
		return super.getMinPower(level) + 30;
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	@Override
	public boolean canAccept(Enchantment other) {
		return super.canAccept(other);
	}
	
	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		Item item = stack.getItem();
		
		return super.isAcceptableItem(stack)
				|| EnchantmentTarget.WEAPON.isAcceptableItem(item)
				|| EnchantmentTarget.TRIDENT.isAcceptableItem(item)
				|| EnchantmentTarget.BOW.isAcceptableItem(item)
				|| EnchantmentTarget.CROSSBOW.isAcceptableItem(item)
				|| stack.getItem() instanceof ShearsItem
				|| stack.getItem() instanceof SpectrumFishingRodItem
				|| (SpectrumIntegrationPacks.isIntegrationPackActive(SpectrumIntegrationPacks.MALUM_ID) && stack.getItem() instanceof MalumScytheItem);
	}
	
}