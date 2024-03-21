package gg.rosie.mixin.entities;

import gg.rosie.DamageHelper;
import gg.rosie.DamageHelperUtils;
import gg.rosie.injected_interfaces.ILivingEntityMixin;
import gg.rosie.network.DamageHelperPacket;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

import net.minecraft.item.ItemStack;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;

import net.minecraft.util.Identifier;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ILivingEntityMixin {
	@Unique
	private boolean crit;

	LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	// @Inject(method = "damage", at = @At("HEAD"))
	// private void damageFirst(DamageSource source, float amount,
	// CallbackInfoReturnable<Boolean> cir) {
	// if (source instanceof ProjectileDamageSource && source.getAttacker()
	// instanceof ILivingEntityMixin livingEntityMixin && source.getSource()
	// instanceof PersistentProjectileEntity projectile) {
	// livingEntityMixin.setCritical(projectile.isCritical());
	// }
	// }

	// this gets injected to the first line inside of if(!invulnerable) in
	// applyDamage due to the shift by 1
	@Inject(method = "applyDamage", at = @At(value = "HEAD", shift = At.Shift.BY, by = 1))
	private void applyDamage(DamageSource source, float amount, CallbackInfo ci) {
		applyDamageInject(source, amount, source.getAttacker(), this);
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

	// Remember this runs for the entity being attacked (target),
	// and not for the attacking entity (attacker)
	@Override
	public void applyDamageInject(DamageSource source, float amount, Entity attacker, Entity target) {
		if (LivingEntityMixin.class.isAssignableFrom(attacker.getClass()) &&
				(((LivingEntityMixin) attacker).isCritical())) {
			LivingEntityMixin attackerAsMixin = (LivingEntityMixin) attacker;

			// You can't attack with an offhand item,
			// but this is the only way to get items in general,
			// so we need to enumerate both and then pick the first one.
			ArrayList<ItemStack> handItems = new ArrayList<>();
			for (ItemStack item : attackerAsMixin.getHandItems()) {
				handItems.add(item);
			}

			Identifier itemIdent = DamageHelperUtils.IdentifierFromItem(handItems.get(0).getItem());

			if (itemIdent == null) {
				return;
			}

			String identString = itemIdent.toString();
			DamageHelperUtils.QuadConsumer<DamageSource, Float, Entity, Entity> toRun = DamageHelper.ItemCrits.get(identString);

			if (toRun != null) {
				toRun.accept(source, amount, attacker, target);
			}

			attackerAsMixin.setCritical(false);
		}
	}

	@Override
	public boolean isCritical() {
		return this.crit;
	}
}
