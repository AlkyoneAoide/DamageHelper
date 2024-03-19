package gg.rosie.mixin.entities;

import gg.rosie.mixin.entities.LivingEntityMixin;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 1), ordinal = 2)
    private boolean attack(boolean bl3) {
        if (this.isCritical()) {
            bl3 = true;
        } else if (bl3) {
            this.setCritical(true);
        }

        return bl3;
    }

    @Inject(method = "applyDamage", at = @At(value = "HEAD", shift = At.Shift.BY, by = 1))
    private void applyDamage(DamageSource source, float amount, CallbackInfo ci) {
        applyDamageInject(source, amount);
    }
}
