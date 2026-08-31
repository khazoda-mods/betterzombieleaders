package com.khazoda.betterzombieleaders.mixin;

import com.khazoda.betterzombieleaders.LeaderZombie;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ZombieVillager.class)
abstract class ZombieVillagerMixin {
  @ModifyReturnValue(method = "getVoicePitch", at = @At("RETURN"))
  private float betterzombieleaders$lowerLeaderVoice(float pitch) {
    return LeaderZombie.voicePitch((ZombieVillager) (Object) this, pitch);
  }
}
