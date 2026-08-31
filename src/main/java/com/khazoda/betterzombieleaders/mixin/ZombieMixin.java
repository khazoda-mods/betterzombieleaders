package com.khazoda.betterzombieleaders.mixin;

import com.khazoda.betterzombieleaders.LeaderZombie;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.monster.zombie.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Zombie.class)
abstract class ZombieMixin {
  @ModifyReturnValue(method = "getBaseExperienceReward", at = @At("RETURN"))
  private int betterzombieleaders$increaseLeaderExperience(int experience) {
    Zombie zombie = (Zombie) (Object) this;
    double strength = LeaderZombie.strength(zombie);
    return strength > 0.0 ? LeaderZombie.randomRounded(zombie, experience * (1.0 + strength)) : experience;
  }
}
