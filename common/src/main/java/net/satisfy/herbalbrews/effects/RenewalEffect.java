package net.satisfy.herbalbrews.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class RenewalEffect extends MobEffect {
    public RenewalEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00FF00);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player) {
            entity.level().getEntitiesOfClass(Player.class, entity.getBoundingBox().inflate(10.0), this::isAffectedEntity)
                    .forEach(player -> {
                        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 50, amplifier, false, false, false));
                        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 50, amplifier, false, false, false));
                    });
        }
    }

    private boolean isAffectedEntity(Player player) {
        return player.isAlive() && !player.isCreative();
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
