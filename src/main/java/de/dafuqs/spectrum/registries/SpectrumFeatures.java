package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.worldgen.features.*;
import net.minecraft.registry.*;
import net.minecraft.world.gen.feature.*;

public class SpectrumFeatures {

	public static Feature<WeightedRandomFeatureConfig> WEIGHTED_RANDOM_FEATURE;
	public static Feature<GeodeFeatureConfig> AIR_CHECK_GEODE;
	public static Feature<RandomBudsFeaturesConfig> RANDOM_BUDS;
	public static Feature<OreFeatureConfig> AIR_CHECK_DISK;
	public static Feature<GilledFungusFeatureConfig> GILLED_FUNGUS;
	public static Feature<GilledFungusFeatureConfig> GIANT_GILLED_FUNGUS;
	public static Feature<NephriteBlossomFeatureConfig> NEPHRITE_BLOSSOM;
	public static Feature<JadeiteLotusFeatureConfig> JADEITE_LOTUS;
	public static Feature<TriStateVineFeatureConfig> TRISTATE_VINE;
	public static Feature<BlockStateFeatureConfig> PILLAR;
	public static Feature<ColumnsFeatureConfig> COLUMNS;
	public static Feature<CrystalFormationFeatureFeatureConfig> BLOB;
	public static Feature<RandomBlockProximityPatchFeatureConfig> RANDOM_BLOCK_PROXIMITY_PATCH;
	public static Feature<FossilFeatureConfig> EXPOSED_FOSSIL;
	public static Feature<WallPatchFeatureConfig> WALL_PATCH;
	public static Feature<AshDunesFeatureConfig> ASH_DUNES;

	public static void register() {
		WEIGHTED_RANDOM_FEATURE = registerFeature("weighted_random_feature", new WeightedRandomFeature(WeightedRandomFeatureConfig.CODEC));
		AIR_CHECK_GEODE = registerFeature("air_check_geode", new SolidBlockCheckGeodeFeature(GeodeFeatureConfig.CODEC));
		RANDOM_BUDS = registerFeature("random_buds", new RandomBudsFeature(RandomBudsFeaturesConfig.CODEC));
		AIR_CHECK_DISK = registerFeature("air_check_disk", new AirCheckDiskFeature(OreFeatureConfig.CODEC));
		GILLED_FUNGUS = registerFeature("gilled_fungus", new GilledFungusFeature(GilledFungusFeatureConfig.CODEC));
		GIANT_GILLED_FUNGUS = registerFeature("giant_gilled_fungus", new GiantGilledFungusFeature(GilledFungusFeatureConfig.CODEC));
		NEPHRITE_BLOSSOM = registerFeature("nephrite_blossom", new NephriteBlossomFeature(NephriteBlossomFeatureConfig.CODEC));
		JADEITE_LOTUS = registerFeature("jadeite_lotus", new JadeiteLotusFeature(JadeiteLotusFeatureConfig.CODEC));
		TRISTATE_VINE = registerFeature("tristate_vine", new TriStateVineFeature(TriStateVineFeatureConfig.CODEC));
		PILLAR = registerFeature("pillar", new PillarFeature(BlockStateFeatureConfig.CODEC));
		COLUMNS = registerFeature("columns", new ColumnsFeature(ColumnsFeatureConfig.CODEC));
		BLOB = registerFeature("crystal_formation", new CrystalFormationFeature(CrystalFormationFeatureFeatureConfig.CODEC));
		RANDOM_BLOCK_PROXIMITY_PATCH = registerFeature("random_block_proximity_patch", new RandomBlockProximityPatchFeature(RandomBlockProximityPatchFeatureConfig.CODEC));
		EXPOSED_FOSSIL = registerFeature("exposed_fossil", new ExposedFossilFeature(FossilFeatureConfig.CODEC));
		WALL_PATCH = registerFeature("wall_patch", new WallPatchFeature(WallPatchFeatureConfig.CODEC));
		ASH_DUNES = registerFeature("ash_dunes", new AshDunesFeature(AshDunesFeatureConfig.CODEC));
	}
	
	private static <C extends FeatureConfig, F extends Feature<C>> F registerFeature(String name, F feature) {
		return Registry.register(Registries.FEATURE, SpectrumCommon.locate(name), feature);
	}
	
}
