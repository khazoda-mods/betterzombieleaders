package com.khazoda.betterzombieleaders;

//? if fabric {
import net.fabricmc.api.ModInitializer;

public final class BetterZombieLeadersFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BetterZombieLeaders.init();
    }
}
//?}
