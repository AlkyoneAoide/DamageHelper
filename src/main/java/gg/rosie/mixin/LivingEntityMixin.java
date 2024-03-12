package gg.rosie.mixin;

import gg.rosie.helper_classes.CustomItemCrits;
import gg.rosie.injected_interfaces.ILivingEntityMixin;
import gg.rosie.network.DamageHelperPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.function.BiConsumer;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ILivingEntityMixin {
    @Unique
    private boolean crit;

    LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

//    @Inject(method = "damage", at = @At("HEAD"))
//    private void damageFirst(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
//        if (source instanceof ProjectileDamageSource && source.getAttacker() instanceof ILivingEntityMixin livingEntityMixin && source.getSource() instanceof PersistentProjectileEntity projectile) {
//            livingEntityMixin.setCritical(projectile.isCritical());
//        }
//    }

//    @Inject(method = "damage", at = @At("RETURN"))
//    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
//        if (source.getAttacker() instanceof ILivingEntityMixin livingEntityMixin) {
//            livingEntityMixin.setCritical(false);
//        }
//    }

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

    @Override
    public void setCritical(boolean flag) {
        this.crit = flag;

        if (!this.getWorld().isClient()) {
            PacketByteBuf byteBuf = new DamageHelperPacket(this.getId(), this.crit).write(PacketByteBufs.create());
            Objects.requireNonNull(this.getWorld().getServer()).getPlayerManager()
                    .sendToAll(new CustomPayloadS2CPacket(DamageHelperPacket.PACKET_NAME, byteBuf));
        }
    }

    @Override
    public boolean isCritical() {
        return this.crit;
    }
}

