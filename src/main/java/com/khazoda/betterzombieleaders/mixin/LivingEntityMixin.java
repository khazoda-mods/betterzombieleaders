package com.khazoda.betterzombieleaders.mixin;

import com.khazoda.betterzombieleaders.LeaderZombie;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.khazoda.betterzombieleaders.BetterZombieLeaders.ID;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {
  @Unique
  private static final ResourceKey<Attribute> MAX_HEALTH = Attributes.MAX_HEALTH.unwrapKey().orElseThrow();
  @Unique
  private static final ResourceKey<LootTable> LEADER_BONUS_LOOT = ResourceKey.create(Registries.LOOT_TABLE, ID("entities/leader_zombie_bonus"));

  @Inject(method = "onAttributeUpdated", at = @At("TAIL"))
  private void betterzombieleaders$updateLeaderScale(Holder<Attribute> attribute, CallbackInfo callback) {
    if (attribute.is(MAX_HEALTH) && (Object) this instanceof Zombie zombie) LeaderZombie.updateScale(zombie);
  }

  @ModifyReturnValue(method = "getVoicePitch", at = @At("RETURN"))
  private float betterzombieleaders$lowerLeaderVoice(float pitch) {
    return (Object) this instanceof Zombie zombie ? LeaderZombie.voicePitch(zombie, pitch) : pitch;
  }

  @Inject(method = "dropCustomDeathLoot", at = @At("TAIL"))
  private void betterzombieleaders$dropLeaderBonus(ServerLevel level, DamageSource source, boolean killedByPlayer, CallbackInfo callback) {
    if (!((Object) this instanceof Zombie zombie)) return;

    double strength = LeaderZombie.strength(zombie);
    int rolls = LeaderZombie.randomRounded(zombie, strength);
    for (int roll = 0; roll < rolls; roll++) {
      zombie.dropFromLootTable(level, source, killedByPlayer, LEADER_BONUS_LOOT);
    }
  }
}
