package gg.rosie.mixin;

import gg.rosie.helper_classes.CustomItemCrits;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiConsumer;

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
        // You can't attack with an offhand item,
        // but this is the only way to get items in general,
        // so we need to enumerate both and then pick the first one.
        ArrayList<ItemStack> handItems = new ArrayList<>();
        for (ItemStack item: source.getAttacker().getHandItems()) {
            handItems.add(item);
        }

        // How to get the RegistryEntry in a non-deprecated way
        Optional<RegistryEntry.Reference<Item>> r = Registries.ITEM.getEntry(Item.getRawId(handItems.get(0).getItem()));

        // How to get the Identifier from the RegistryEntry
        Identifier itemIdent = r.get().getKey().get().getValue();

        // this gets injected to the first line inside of if(!invulnerable) in applyDamage
        // NOTE: remember to also inject this method into PlayerEntity when its implementation is finished here
        if (this.isCritical()) {
            BiConsumer<DamageSource, Float> toRun = CustomItemCrits.get(itemIdent);
            if (toRun != null) {
                toRun.accept(source, amount);
            }
        }
    }
}
