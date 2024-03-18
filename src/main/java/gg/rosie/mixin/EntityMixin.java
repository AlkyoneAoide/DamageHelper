package gg.rosie.mixin;

import gg.rosie.DamageHelper;
import gg.rosie.DamageHelperUtils;
import gg.rosie.injected_interfaces.IEntityMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntityMixin {

    @Shadow public abstract int getId();

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    private void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        List<String> immunities = DamageHelper.Immunity.get(this.getId());

        if (immunities != null) {
            String damageSource = "";
            Identifier mobDamageIdent = null;

            if (source.getTypeRegistryEntry().getKey().isPresent()) {
                Optional<RegistryKey<DamageType>> damageTypeKey = source.getTypeRegistryEntry().getKey();
                damageSource = damageTypeKey.get().getValue().toString();
            }

            if (source.getAttacker() != null) {
                mobDamageIdent = DamageHelperUtils.IdentifierFromEntity(source.getAttacker());
            }

            if (immunities.contains(damageSource)) {
                System.out.println("Immune to " + damageSource);
                cir.setReturnValue(true);
            } else if (mobDamageIdent != null && immunities.contains(mobDamageIdent.toString())) {
                System.out.println("Immune to " + mobDamageIdent);
                cir.setReturnValue(true);
            }
        }
    }
}
