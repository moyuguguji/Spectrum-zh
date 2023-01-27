package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.bottomless_bundle.*;
import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.food.beverages.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import io.wispforest.owo.itemgroup.*;
import io.wispforest.owo.itemgroup.gui.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;

import java.util.*;

public class SpectrumItemGroups {
	
	private static final Identifier ITEM_GROUP_BACKGROUND_TEXTURE_IDENTIFIER = SpectrumCommon.locate("textures/gui/item_group.png");
	private static final Identifier ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER = SpectrumCommon.locate("textures/gui/item_group_button.png");
	
	public static final OwoItemGroup ITEM_GROUP_GENERAL = new OwoItemGroup(SpectrumCommon.locate("general")) {
		
		@Override
		protected void setup() {
			setCustomTexture(ITEM_GROUP_BACKGROUND_TEXTURE_IDENTIFIER);
			
			addTab(Icon.of(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST), "general", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			addTab(Icon.of(SpectrumItems.BEDROCK_PICKAXE), "equipment", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			addTab(Icon.of(SpectrumItems.RESTORATION_TEA), "consumables", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			addTab(Icon.of(SpectrumBlocks.CITRINE_BLOCK), "resources", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			
			addButton(ItemGroupButton.discord("https://discord.com/invite/EXU9XFXT8a")); // TODO: Add item group background texture, as soon as owo supports it
			addButton(ItemGroupButton.github("https://github.com/DaFuqs/Spectrum"));
			addButton(ItemGroupButton.curseforge("https://www.curseforge.com/minecraft/mc-mods/spectrum"));
			addButton(ItemGroupButton.modrinth("https://modrinth.com/mod/spectrum"));
		}
		
		@Override
		public ItemStack createIcon() {
			return SpectrumBlocks.PEDESTAL_BASIC_AMETHYST.asItem().getDefaultStack();
		}
		
	};
	
	public static final OwoItemGroup ITEM_GROUP_BLOCKS = new OwoItemGroup(SpectrumCommon.locate("blocks")) {
		
		@Override
		protected void setup() {
			setCustomTexture(ITEM_GROUP_BACKGROUND_TEXTURE_IDENTIFIER);
			
			addTab(Icon.of(SpectrumBlocks.MOONSTONE_CHISELED_CALCITE), "decoration", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			addTab(Icon.of(SpectrumBlocks.LIME_LOG), "colored_wood", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			addTab(Icon.of(SpectrumBlocks.getMobHead(SpectrumSkullBlock.SpectrumSkullBlockType.PUFFERFISH)), "mob_heads", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			addTab(Icon.of(SpectrumItems.BOTTOMLESS_BUNDLE), "predefined_items", null, ITEM_GROUP_BUTTON_TEXTURE_IDENTIFIER);
			
			addButton(ItemGroupButton.discord("https://discord.gg/VHUPpHrj")); // TODO: Add item group background texture, as soon as owo supports it
			addButton(ItemGroupButton.github("https://github.com/DaFuqs/Spectrum"));
			addButton(ItemGroupButton.curseforge("https://www.curseforge.com/minecraft/mc-mods/spectrum"));
			addButton(ItemGroupButton.modrinth("https://modrinth.com/mod/spectrum"));
		}
		
		@Override
		public void appendStacks(DefaultedList<ItemStack> stacks) {
			super.appendStacks(stacks);
			
			if (this.getSelectedTab() == ITEM_GROUP_BLOCKS.getTab(3)) {
				// fully filled Knowledge Gem
				stacks.add(KnowledgeGemItem.getKnowledgeDropStackWithXP(10000, false));
				stacks.add(SpectrumItems.MIDNIGHT_ABERRATION.getStableStack());
				
				for (InkColor color : InkColor.all()) {
					stacks.add(SpectrumItems.INK_FLASK.getFullStack(color));
				}
				stacks.add(SpectrumItems.INK_ASSORTMENT.getFullStack());
				stacks.add(SpectrumItems.PIGMENT_PALETTE.getFullStack());
				stacks.add(SpectrumItems.ARTISTS_PALETTE.getFullStack());
				stacks.add(SpectrumItems.HEARTSINGERS_REWARD_RING.getFullStack());
				stacks.add(SpectrumItems.GLOVES_OF_DAWNS_GRASP.getFullStack());
				stacks.add(SpectrumItems.SHIELDGRASP_AMULET.getFullStack());
				stacks.add(SpectrumItems.RING_OF_PURSUIT.getFullStack());
				
				// Bottomless Bundles willed with useful, basic materials
				stacks.add(BottomlessBundleItem.getWithBlockAndCount(Items.COBBLESTONE.getDefaultStack(), 20000));
				stacks.add(BottomlessBundleItem.getWithBlockAndCount(Items.STONE.getDefaultStack(), 20000));
				stacks.add(BottomlessBundleItem.getWithBlockAndCount(Items.DEEPSLATE.getDefaultStack(), 20000));
				stacks.add(BottomlessBundleItem.getWithBlockAndCount(Items.OAK_PLANKS.getDefaultStack(), 20000));
				stacks.add(BottomlessBundleItem.getWithBlockAndCount(Items.SAND.getDefaultStack(), 20000));
				stacks.add(BottomlessBundleItem.getWithBlockAndCount(Items.GRAVEL.getDefaultStack(), 20000));
				stacks.add(BottomlessBundleItem.getWithBlockAndCount(Items.ARROW.getDefaultStack(), 20000));
				
				// Fully Enchanted Enchanter Enchantables
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.BOTTOMLESS_BUNDLE, Enchantments.POWER, SpectrumEnchantments.VOIDING));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.ENDER_SPLICE, Enchantments.UNBREAKING, SpectrumEnchantments.RESONANCE));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.EXCHANGING_STAFF, Enchantments.FORTUNE));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.EXCHANGING_STAFF, Enchantments.SILK_TOUCH));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.KNOWLEDGE_GEM, Enchantments.EFFICIENCY, Enchantments.QUICK_CHARGE));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.NATURES_STAFF, Enchantments.EFFICIENCY));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.RADIANCE_STAFF, Enchantments.INFINITY));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.GLEAMING_PIN, Enchantments.POWER));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.SEVEN_LEAGUE_BOOTS, Enchantments.POWER));
				stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(SpectrumItems.TAKE_OFF_BELT, Enchantments.POWER, Enchantments.FEATHER_FALLING));
				
				// Infused Beverage Variants
				if (SpectrumCommon.minecraftServer != null) {
					for (ITitrationBarrelRecipe recipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.TITRATION_BARREL)) {
						ItemStack output = recipe.getOutput().copy();
						if (output.getItem() instanceof BeverageItem) {
							output.setCount(1);
							stacks.add(output);
						}
					}
					
					// Enchanted books with the max upgrade level available via Enchantment Upgrading
					HashMap<Enchantment, Integer> highestEnchantmentLevels = new HashMap<>();
					for (EnchantmentUpgradeRecipe enchantmentUpgradeRecipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.ENCHANTMENT_UPGRADE)) {
						Enchantment enchantment = enchantmentUpgradeRecipe.getEnchantment();
						int destinationLevel = enchantmentUpgradeRecipe.getEnchantmentDestinationLevel();
						if (highestEnchantmentLevels.containsKey(enchantment)) {
							if (highestEnchantmentLevels.get(enchantment) < destinationLevel) {
								highestEnchantmentLevels.put(enchantment, destinationLevel);
							}
						} else {
							highestEnchantmentLevels.put(enchantment, destinationLevel);
						}
					}
					for (Map.Entry<Enchantment, Integer> s : highestEnchantmentLevels.entrySet()) {
						if (s.getValue() > s.getKey().getMaxLevel()) {
							stacks.add(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(s.getKey(), s.getValue())));
						}
					}
					
					// all memories that have spirit instiller recipes
					Item memoryItem = SpectrumBlocks.MEMORY.asItem();
					for (SpiritInstillerRecipe recipe : SpectrumCommon.minecraftServer.getRecipeManager().listAllOfType(SpectrumRecipeTypes.SPIRIT_INSTILLING)) {
						if (recipe.getOutput().isOf(memoryItem)) {
							stacks.add(recipe.getOutput());
						}
					}
				}
			}
		}
		
		@Override
		public ItemStack createIcon() {
			return SpectrumBlocks.MOONSTONE_CHISELED_CALCITE.asItem().getDefaultStack();
		}
		
	};
	
}
