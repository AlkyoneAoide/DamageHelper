package gg.rosie.mixin;

import gg.rosie.injected_interfaces.IEntityMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntityMixin {

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    private void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        RegistryKey<DamageType> damageTypeKey = source.getTypeRegistryEntry().getKey().get();

        if(source.getAttacker() instanceof LightningEntity || source.getSource() instanceof LightningEntity || Objects.equals(damageTypeKey.getValue().toString(), "minecraft:lightning_bolt")) {
            System.out.println("Immune to lightning");
            cir.setReturnValue(true);
        }
    }
}
