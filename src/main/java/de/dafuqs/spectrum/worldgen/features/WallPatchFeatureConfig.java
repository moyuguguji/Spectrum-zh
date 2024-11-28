package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.block.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.*;

import java.util.*;

public class WallPatchFeatureConfig implements FeatureConfig {
	
	public static final Codec<WallPatchFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
				Registries.BLOCK.getCodec().fieldOf("block").forGetter((config) -> config.block),
				Codec.intRange(1, 64).fieldOf("search_range").orElse(10).forGetter((config) -> config.searchRange),
				Codec.BOOL.fieldOf("can_place_on_floor").orElse(false).forGetter((config) -> config.placeOnFloor),
				Codec.BOOL.fieldOf("can_place_on_ceiling").orElse(false).forGetter((config) -> config.placeOnCeiling),
				Codec.BOOL.fieldOf("can_place_on_wall").orElse(false).forGetter((config) -> config.placeOnWalls),
				IntProvider.NON_NEGATIVE_CODEC.fieldOf("width").forGetter((config) -> config.width),
				IntProvider.NON_NEGATIVE_CODEC.fieldOf("height").forGetter((config) -> config.height),
				RegistryCodecs.entryList(RegistryKeys.BLOCK).fieldOf("can_be_placed_on").forGetter((config) -> config.canPlaceOn))
				.apply(instance, WallPatchFeatureConfig::new)
	);
	
	public final Block block;
	public final int searchRange;
	public final boolean placeOnFloor;
	public final boolean placeOnCeiling;
	public final boolean placeOnWalls;
	public final IntProvider width;
	public final IntProvider height;
	public final RegistryEntryList<Block> canPlaceOn;
	private final ObjectArrayList<Direction> directions;
	
	public WallPatchFeatureConfig(Block block, Integer searchRange, Boolean placeOnFloor, Boolean placeOnCeiling, Boolean placeOnWalls, IntProvider width, IntProvider height, RegistryEntryList<Block> canPlaceOn) {
		this.block = block;
		this.searchRange = searchRange;
		this.placeOnFloor = placeOnFloor;
		this.placeOnCeiling = placeOnCeiling;
		this.placeOnWalls = placeOnWalls;
		this.width = width;
		this.height = height;
		this.canPlaceOn = canPlaceOn;
		this.directions = new ObjectArrayList<>(6);
		if (placeOnCeiling) {
			this.directions.add(Direction.UP);
		}
		if (placeOnFloor) {
			this.directions.add(Direction.DOWN);
		}
		if (placeOnWalls) {
			Direction.Type.HORIZONTAL.forEach(this.directions::add);
		}
	}
	
	public List<Direction> shuffleDirections(Random random, Direction excluded) {
		return Util.copyShuffled(this.directions.stream().filter((direction) -> direction != excluded), random);
	}
	
	public List<Direction> shuffleDirections(Random random) {
		return Util.copyShuffled(this.directions, random);
	}
	
}
