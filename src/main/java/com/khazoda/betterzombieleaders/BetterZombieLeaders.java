package com.khazoda.betterzombieleaders;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BetterZombieLeaders {
  public static final String MOD_ID = "betterzombieleaders";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  public static Identifier ID(String path) {
    return Identifier.fromNamespaceAndPath(MOD_ID, path);
  }

  private BetterZombieLeaders() {
  }

  public static void init() {
    LOGGER.info("- better zombie leaders loaded -");
  }
}
