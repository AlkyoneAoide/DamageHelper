package gg.rosie;

import net.minecraft.entity.damage.DamageSource;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class CustomItemCrits {
	// A map of identifiers to functions, to run when a crit is hit
	// with the item the identifier corresponds to.
	// The function is passed the DamageSource and (float) amount of damage that
	// applyDamage normally gets.
	// Note that this is only additive, and does not touch existing crit handling
	// code.
	// Also, it is a consumer and therefore has a guaranteed void return type.
	protected static HashMap<String, BiConsumer<DamageSource, Float>> CUSTOM_CRIT_LIST = new HashMap<>();

	// Add a custom crit effect to an arbitrary item
	// This is strictly additive, it gets injected
	// in addition to existing code
	// ident is a minecraft-style identifier in string form
	// i.e. "minecraft:wooden_sword"
	// func is a two-parameter function that returns void
	// param 1 is a DamageSource and param 2 is a float (damage amount)
	// same as what is given to the "LivingEntity.applyDamage"
	public static void add(String ident, BiConsumer<DamageSource, Float> func) {
		CUSTOM_CRIT_LIST.put(ident, func);
	}

	public static BiConsumer<DamageSource, Float> get(String ident) {
		return CUSTOM_CRIT_LIST.get(ident);
	}
}
