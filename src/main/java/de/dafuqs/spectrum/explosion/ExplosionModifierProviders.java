package de.dafuqs.spectrum.explosion;

import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ExplosionModifierProviders {
	
	protected static Map<Item, ExplosionModifier> PROVIDERS = new Object2ObjectOpenHashMap<>();
	
	public static @Nullable ExplosionModifier get(ItemStack stack) {
		return PROVIDERS.getOrDefault(stack.getItem(), null);
	}
	
	public static void registerModifier(ItemConvertible provider, ExplosionModifier behavior) {
		PROVIDERS.put(provider.asItem(), behavior);
	}
	
	public static void register() {
		registerModifier(SpectrumBlocks.INCANDESCENT_AMALGAM, SpectrumExplosionEffects.AMALGAM_MODIFIER);
		registerModifier(SpectrumItems.STORM_STONE, SpectrumExplosionEffects.LIGHTNING);
		registerModifier(SpectrumItems.NEOLITH, SpectrumExplosionEffects.MAGIC);
		registerModifier(SpectrumItems.MIDNIGHT_CHIP, SpectrumExplosionEffects.LOOTING);
		registerModifier(SpectrumItems.MIDNIGHT_ABERRATION, SpectrumExplosionEffects.LOOTING);
		registerModifier(SpectrumItems.REFINED_BLOODSTONE, SpectrumExplosionEffects.PRIMORDIAL_FIRE);
		registerModifier(Items.CHORUS_FRUIT, SpectrumExplosionEffects.STARRY_MODIFIER);
	}
	
}
