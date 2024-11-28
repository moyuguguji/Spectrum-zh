package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.world.gen.feature.*;

public record RandomBlockProximityPatchFeatureConfig(Integer tries, Integer xzSpread, Integer ySpread,
													 RegistryEntryList<Block> blocksToCheckFor, Integer blockScanRange,
													 RegistryEntry<PlacedFeature> closeToBlockFeature,
													 RegistryEntry<PlacedFeature> fallbackFeature) implements FeatureConfig {
	
	public static final Codec<RandomBlockProximityPatchFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			Codecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter(RandomBlockProximityPatchFeatureConfig::tries),
			Codecs.NONNEGATIVE_INT.fieldOf("xz_spread").orElse(7).forGetter(RandomBlockProximityPatchFeatureConfig::xzSpread),
			Codecs.NONNEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter(RandomBlockProximityPatchFeatureConfig::ySpread),
			RegistryCodecs.entryList(RegistryKeys.BLOCK).fieldOf("blocks_to_find").forGetter(RandomBlockProximityPatchFeatureConfig::blocksToCheckFor),
			Codecs.NONNEGATIVE_INT.fieldOf("block_scan_range").orElse(1).forGetter(RandomBlockProximityPatchFeatureConfig::ySpread),
			PlacedFeature.REGISTRY_CODEC.fieldOf("close_to_block_feature").forGetter(RandomBlockProximityPatchFeatureConfig::closeToBlockFeature),
			PlacedFeature.REGISTRY_CODEC.fieldOf("fallback_feature").forGetter(RandomBlockProximityPatchFeatureConfig::fallbackFeature)
	).apply(instance, RandomBlockProximityPatchFeatureConfig::new));

}
