package com.khazoda.betterzombieleaders;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;

import static com.khazoda.betterzombieleaders.BetterZombieLeaders.ID;

public final class LeaderZombie {
  private static final Identifier LEADER_BONUS = Identifier.withDefaultNamespace("leader_zombie_bonus");
  private static final Identifier LEADER_SCALE = ID("leader_scale");

  private LeaderZombie() {
  }

  public static double strength(Zombie zombie) {
    AttributeInstance maxHealth = zombie.getAttribute(Attributes.MAX_HEALTH);
    if (maxHealth == null) return 0.0;

    AttributeModifier leaderBonus = maxHealth.getModifier(LEADER_BONUS);
    if (leaderBonus == null) return 0.0;

    return leaderBonus.amount();
  }

  public static int randomRounded(Zombie zombie, double value) {
    int wholeNumber = (int) value;
    return wholeNumber + (zombie.getRandom().nextDouble() < value - wholeNumber ? 1 : 0);
  }

  public static float voicePitch(Zombie zombie, float pitch) {
    double strength = strength(zombie);
    return strength > 0.0 ? pitch * (float) (0.95 - strength * 0.05) : pitch;
  }

  public static void updateScale(Zombie zombie) {
    double strength = strength(zombie);
    AttributeInstance scale = zombie.getAttribute(Attributes.SCALE);
    if (scale == null) return;

    if (strength > 0.0) {
      scale.addOrReplacePermanentModifier(new AttributeModifier(LEADER_SCALE, 0.05 + strength * 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }
  }
}
