package gg.rosie.injected_interfaces;

import net.minecraft.entity.damage.DamageSource;

public interface ILivingEntityMixin {
    void setCritical(boolean flag);

    void applyDamageInject(DamageSource source, float amount);

    boolean isCritical();
}
