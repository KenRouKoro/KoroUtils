package cn.korostudio.multidimensionalmanager.data;

import cn.korostudio.koroutilslib.KoroUtilsLib;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;
import lombok.Data;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.UnmodifiableLevelProperties;

import java.util.OptionalLong;

@Data
public class DimensionData {
    long fixedTime;
    boolean hasSkyLight;
    boolean hasCeiling;
    boolean ultrawarm;
    boolean natural;
    double coordinateScale;
    boolean bedWorks;
    boolean respawnAnchorWorks;
    int minY;
    int height;
    int logicalHeight;
    int infiniburn;
    int effects;
    float ambientLight;
    boolean piglinSafe;
    boolean hasRaids;
    int[] monsterSpawnLightTest;
    int monsterSpawnBlockLightLimit;
    String chunkGeneratorSettingsKey;
    String id;
    long seed;
    String type;
    String biome_key;

    public static DimensionOptions getMinecraftDimensionType(DimensionData data){
        TagKey<Block> infiniburn;
        switch (data.infiniburn){
            case 1 -> infiniburn = BlockTags.INFINIBURN_NETHER;
            case 2 -> infiniburn = BlockTags.INFINIBURN_END;
            default -> infiniburn = BlockTags.INFINIBURN_OVERWORLD;
        }
        Identifier effects;
        switch (data.effects){
            case 1 -> effects = DimensionTypes.THE_NETHER_ID;
            case 2 -> effects = DimensionTypes.THE_END_ID;
            default -> effects = DimensionTypes.OVERWORLD_ID;
        }
        DimensionType dimensionType = new DimensionType(data.fixedTime>0?OptionalLong.of(data.fixedTime):OptionalLong.empty(),data.hasSkyLight,data.hasCeiling,data.ultrawarm,data.natural,data.coordinateScale,data.bedWorks,data.respawnAnchorWorks,data.minY,data.height,data.logicalHeight,infiniburn,effects,data.ambientLight,new DimensionType.MonsterSettings(data.piglinSafe,data.hasRaids, UniformIntProvider.create(data.monsterSpawnLightTest[0], data.monsterSpawnLightTest[1]),data.monsterSpawnBlockLightLimit));
        RegistryKey<DimensionOptions> dimensionOptionsRegistryKey = RegistryKey.of(Registry.DIMENSION_KEY, new Identifier(data.id));
        //ChunkGeneratorSettings registryEntry = BuiltinRegistries.CHUNK_GENERATOR_SETTINGS.get(new Identifier(data.chunkGeneratorSettingsKey));

        RegistryKey<DimensionType> DimensionTypeRegistryKey = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, new Identifier(data.id));
        RegistryEntry<DimensionType> registry =  BuiltinRegistries.add(new SimpleRegistry(Registry.DIMENSION_TYPE_KEY, Lifecycle.stable(), null), DimensionTypeRegistryKey,dimensionType);

        ChunkGenerator generator = null;

        MultiNoiseBiomeSource multiNoiseBiomeSource = MultiNoiseBiomeSource.Preset.OVERWORLD.getBiomeSource(BuiltinRegistries.BIOME);
        RegistryEntry<ChunkGeneratorSettings> registryEntry = BuiltinRegistries.CHUNK_GENERATOR_SETTINGS.getOrCreateEntry(ChunkGeneratorSettings.OVERWORLD);
        RegistryEntry<ChunkGeneratorSettings> registryEntry2 = BuiltinRegistries.CHUNK_GENERATOR_SETTINGS.getOrCreateEntry(ChunkGeneratorSettings.LARGE_BIOMES);
        RegistryEntry<ChunkGeneratorSettings> registryEntry3 = BuiltinRegistries.CHUNK_GENERATOR_SETTINGS.getOrCreateEntry(ChunkGeneratorSettings.AMPLIFIED);

        switch (data.type){
            case "minecraft:singe_biome_surface"->{
                RegistryKey<Biome> key = RegistryKey.of(Registry.BIOME_KEY,new Identifier(data.biome_key));//Registry.BIOME_KEY.tryCast().get();
                generator = new NoiseChunkGenerator(BuiltinRegistries.STRUCTURE_SET, BuiltinRegistries.NOISE_PARAMETERS, new FixedBiomeSource(BuiltinRegistries.BIOME.getOrCreateEntry((RegistryKey<Biome>)key)), registryEntry);
            }
            case "minecraft:amplified"->generator = new NoiseChunkGenerator(BuiltinRegistries.STRUCTURE_SET, BuiltinRegistries.NOISE_PARAMETERS, multiNoiseBiomeSource, registryEntry3);
            case "minecraft:large_biomes"-> generator = new NoiseChunkGenerator(BuiltinRegistries.STRUCTURE_SET, BuiltinRegistries.NOISE_PARAMETERS, multiNoiseBiomeSource, registryEntry2);
            case "minecraft:default" ->   generator = new NoiseChunkGenerator(BuiltinRegistries.STRUCTURE_SET, BuiltinRegistries.NOISE_PARAMETERS, multiNoiseBiomeSource, registryEntry);
            case "minecraft:nether" -> generator = new NoiseChunkGenerator(BuiltinRegistries.STRUCTURE_SET, BuiltinRegistries.NOISE_PARAMETERS, (BiomeSource)MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(BuiltinRegistries.BIOME), BuiltinRegistries.CHUNK_GENERATOR_SETTINGS.getOrCreateEntry(ChunkGeneratorSettings.NETHER));
            case "minecraft:the_end" ->generator = new NoiseChunkGenerator(BuiltinRegistries.STRUCTURE_SET, BuiltinRegistries.NOISE_PARAMETERS, new TheEndBiomeSource(BuiltinRegistries.BIOME), BuiltinRegistries.CHUNK_GENERATOR_SETTINGS.getOrCreateEntry(ChunkGeneratorSettings.END));
            case "minecraft:flat"->generator = new FlatChunkGenerator( BuiltinRegistries.STRUCTURE_SET,FlatChunkGeneratorConfig.getDefaultConfig( BuiltinRegistries.BIOME, BuiltinRegistries.STRUCTURE_SET));
            default ->{

            }
        }

        return new DimensionOptions(registry,generator);
    }

    public static void createWorld(DimensionData data){
        RegistryKey<World> worldKey = RegistryKey.of(Registry.WORLD_KEY, new Identifier(data.id));
        RegistryKey.of(Registry.DIMENSION_KEY, new Identifier(data.id));
        ServerWorldProperties serverWorldProperties = KoroUtilsLib.getMinecraftServer().getSaveProperties().getMainWorldProperties();
        UnmodifiableLevelProperties unmodifiableLevelProperties = new UnmodifiableLevelProperties(KoroUtilsLib.getMinecraftServer().getSaveProperties(), serverWorldProperties);
        ServerWorld serverWorld = new ServerWorld(KoroUtilsLib.getMinecraftServer(), KoroUtilsLib.getMinecraftServer().getWorkerExecutor(), KoroUtilsLib.getMinecraftServer().getSession(), unmodifiableLevelProperties, worldKey, getMinecraftDimensionType(data), KoroUtilsLib.getMinecraftServer().getWorldGenerationProgressListener(), false, data.seed, ImmutableList.of(), false);
        KoroUtilsLib.getMinecraftServer().getWorldsMap().put(worldKey,serverWorld);

    }
}
