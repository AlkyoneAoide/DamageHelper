package gg.rosie;

import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.damage.DamageSource;

import java.util.*;
import java.util.function.BiConsumer;

public class DamageHelper implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	// public static final Logger LOGGER = LoggerFactory.getLogger("damagehelper");

	@Override
	public void onInitialize() {}

	public static class ItemCrits {
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

	public static class Immunity {
		// list of entity ID (not uuid) and corresponding list of damage sources
		// (as identifiers: minecraft:lightning_bolt or minecraft:zombie) to have immunity to
		protected static HashMap<Integer, ArrayList<String>> IMMUNITY_LIST = new HashMap<>();

		// Add immunity from damageSource to entity id
		public static void add(int id, String damageSource) {
			if (IMMUNITY_LIST.containsKey(id)) {
				IMMUNITY_LIST.get(id).add(damageSource);
			} else {
				IMMUNITY_LIST.put(id, new ArrayList<>(Collections.singletonList(damageSource)));
			}
		}

		// Remove only one damageSource from id
		public static void remove(int id, String damageSource) {
			if (IMMUNITY_LIST.containsKey(id)) {
				IMMUNITY_LIST.get(id).remove(damageSource);
			}
		}

		// Remove id from the list entirely
		public static void remove(int id) {
            IMMUNITY_LIST.remove(id);
		}

		// get the list of immunities the entity id should have
		public static ArrayList<String> get(int id) {
			return IMMUNITY_LIST.get(id);
		}
	}
}
