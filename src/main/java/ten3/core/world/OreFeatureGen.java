package ten3.core.world;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import ten3.init.BlockInit;

import java.util.List;

public class OreFeatureGen {
	private static final ConfiguredFeature<?, ?> OVERWORLD_WOOL_ORE_CONFIGURED_FEATURE = new ConfiguredFeature<>
			(Feature.ORE, new OreConfiguration(
					BlockTags.STONE_ORE_REPLACEABLES.,
					Blocks.WHITE_WOOL.defaultBlockState(),
					9));

	public static PlacedFeature OVERWORLD_WOOL_ORE_PLACED_FEATURE = new PlacedFeature(
			RegistryEntry.of(OVERWORLD_WOOL_ORE_CONFIGURED_FEATURE),
			Arrays.asList(
					CountPlacementModifier.of(20), // number of veins per chunk
					SquarePlacementModifier.of(), // spreading horizontally
					HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.fixed(64))
			)); // height

    static boolean alreadySetup;
    static void setup() {
        List<OreConfiguration.TargetBlockState> tinList =
                List.of(OreConfiguration.target(
                                STONE_ORE_REPLACEABLES,
                                BlockInit.getBlock("tin_ore").defaultBlockState()
                        ),
                        OreConfiguration.target(
                                DEEPSLATE_ORE_REPLACEABLES,
                                BlockInit.getBlock("deep_tin_ore").defaultBlockState()
                        ));
        List<OreConfiguration.TargetBlockState> nickelList =
                List.of(OreConfiguration.target(
                                STONE_ORE_REPLACEABLES,
                                BlockInit.getBlock("nickel_ore").defaultBlockState()
                        ),
                        OreConfiguration.target(
                                DEEPSLATE_ORE_REPLACEABLES,
                                BlockInit.getBlock("deep_nickel_ore").defaultBlockState()
                        ));
        tin_ore_cfg = FeatureUtils.register("tin_ore", Feature.ORE,
                new OreConfiguration(tinList, 10));
        nickel_ore_cfg = FeatureUtils.register("nickel_ore", Feature.ORE,
                new OreConfiguration(nickelList, 8, 0.2F));


        TIN = PlacementUtils.register("ore_tin", tin_ore_cfg,
                commonOrePlacement(20, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80),
                        VerticalAnchor.belowTop(48))));
        NKL = PlacementUtils.register("ore_nickel", nickel_ore_cfg,
                commonOrePlacement(7, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80),
                        VerticalAnchor.belowTop(12))));
    }

    public static Holder<ConfiguredFeature<OreConfiguration, ?>> tin_ore_cfg;
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> nickel_ore_cfg;

    public static Holder<PlacedFeature> TIN;
    public static Holder<PlacedFeature> NKL;

    private static List<PlacementModifier> orePlacement(PlacementModifier modifier1, PlacementModifier modifier2) {
        return List.of(modifier1, InSquarePlacement.spread(), modifier2, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
        return orePlacement(CountPlacement.of(count), modifier);
    }

}
