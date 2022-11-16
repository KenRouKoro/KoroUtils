package cn.korostudio.sodiumsightdistanceunlocked.client;

import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.GameOptions;

@Environment(EnvType.CLIENT)
public class SodiumSightDistanceUnlockedClient implements ClientModInitializer {
    @Getter
    @Setter
    protected static GameOptions gameOptions;
    @Override
    public void onInitializeClient() {

    }
}
